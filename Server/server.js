require('process');
const express = require('express');
const path = require('path');
const amqp = require('amqplib/callback_api');
const WebSocket = require('ws');

const connected_clients = [];
const rabbitmq_timeout_error_message = 'Channel closed by server: 406 (PRECONDITION-FAILED)';
const app = express();
const rabbitmq_host = process.env.RABBITMQ_HOST || 'localhost';
const port = process.env.GATEWAY_SERVER_PORT || 3000;
const ws_port = process.env.WEBSOCKET_PORT || 7071;
const wss = new WebSocket.Server({ port: ws_port });

var command_channel, command_result_channel, rpc_channel;
var rabbitmq_has_problems = false;

app.use(express.static(path.join(__dirname, 'public')));
app.use(express.json());

connectToRabbitmq();

process.on('uncaughtException', (error) => {
    if (error.message.includes(rabbitmq_timeout_error_message)) {
      console.error('RabbitMQ message timed out: ', error.message);
    } else {
        console.error('Uncaught Exception:', error);
        process.exit(1);
    }
});

wss.on('connection', (ws) => {
    connected_clients.push(ws);
    console.log('Client connected');
  
    ws.on('message', (message) => {
        const parsedMessage = JSON.parse(message);
        if (parsedMessage.type === 'cateringOrderingId') {
        ws.cateringOrderingId = parsedMessage.cateringOrderingId;
        console.log(`Client ${ws.cateringOrderingId} identified`);
        }
    });
  
    ws.on('close', () => {
        const index = connected_clients.indexOf(ws);
        if (index > -1) {
            connected_clients.splice(index, 1);
        }
        console.log('Client disconnected');
    });
});

function createConnection(url) {
    return new Promise((resolve, reject) => {
        amqp.connect(url, (error, connection) => {
            if (error) {
                reject(error);
            } else {
                resolve(connection);
            }
        });
    });
}

function createChannel(connection) {
    return new Promise((resolve, reject) => {
        connection.createChannel((error, channel) => {
            if (error) {
                reject(error);
            } else {
                resolve(channel);
            }
        });
    });
}

async function connectToRabbitmq() {
    publishing_connection = await createConnection(`amqp://${rabbitmq_host}`);
    console.log("Publishing connection created.");

    publishing_connection.createChannel(async (error1, ch) => {
        if (error1) {
            console.log("Error creating rpc channel:", error1);
        }
        rpc_channel = ch;
        console.log("RPC channel created");
    });

    publishing_connection.createChannel(async (error1, ch) => {
        if (error1) {
            console.log("Error creating command channel:", error1);
        }
        command_channel = ch;
        console.log("Command channel created");
    });

    const consuming_connection = await createConnection(`amqp://${rabbitmq_host}`);
    console.log("Consuming connection created.");

    command_result_channel = await createChannel(consuming_connection);
    console.log("Polling channel created");

    const queueName = 'command_response_queue';
    await command_result_channel.assertQueue(queueName, { durable: true });

    command_result_channel.consume(queueName, (message) => {
        const messageContent = message.content.toString();

        connected_clients.forEach(client => {
            console.log("Client: " + message.properties.headers['user_id']);
            if (client.cateringOrderingId == message.properties.headers['user_id']) {
                client.send(messageContent);
            }
        });
        console.log(`Received message: ${messageContent}`);
        command_result_channel.ack(message);
    });
}

async function reconnectToRabbitmq() {
    while(true) {
        try {
            await connectToRabbitmq();
            console.log("Reconnected to RabbitMQ");
            rabbitmq_has_problems = false
            return;
        } catch (error) {
            console.error('Error connecting to RabbitMQ: ', error.message);
            console.log('Next try in 30 seconds.');
            await new Promise(resolve => setTimeout(resolve, 30000));
        }
    }
}


app.get('/', (_req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

app.get('/admin', (_req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'admin.html'));
});

app.get('/loadCaterings', (_req, res) => {
    if (rabbitmq_has_problems) {
        res.status(500).send("Request rejected as the server is facing problems with RabbitMQ.");
        return;
    }
    const timeout = setTimeout(() => {
        console.log("Loading caterings timed out.");
        res.status(500).send("Error: Timeout exceeded");
    }, 5000);
    try {
        handleCateringsLoad(function([success, result]) {
            clearTimeout(timeout);
            if (success) {
                res.json(result);
            } else {
                res.status(500).send(result);
            }
        });
    } catch (e) {
        clearTimeout(timeout);
        res.status(500).send("Error with RabbitMQ.");
        rabbitmq_has_problems = true;
        reconnectToRabbitmq().then();
    } 
});

function handleCateringsLoad(callback) {
    if (!rpc_channel) {
        callback([false, "Error: RabbitMQ channel not available"]);
    }

    console.log("Loading caterings...");

    rpc_channel.assertQueue('', {
        exclusive: true
    }, function(error2, q) {
        if (error2) {
            callback([false, "Error: error with asserting queue"]);
        }

        var correlationId = generateUuid();

        rpc_channel.consume(q.queue, function(msg) {
            console.log('Received caterings: %s', msg.content.toString());
            try {
                const jsonMessage = JSON.parse(msg.content.toString());
                callback([true, jsonMessage]);
            } catch (error) {
                callback([false, msg.content.toString()]);
            } finally {
                rpc_channel.ack(msg);
            }
        });

        rpc_channel.sendToQueue('query_queue',
            Buffer.from(""), {
                headers: { 'query_type': 'read_all_caterings' },
                correlationId: correlationId,
                replyTo: q.queue
            });
    });
}

app.get('/loadOrders', (_req, res) => {
    if (rabbitmq_has_problems) {
        res.status(500).send("Request rejected as the server is facing problems with RabbitMQ.");
        return;
    }
    const timeout = setTimeout(() => {
        console.log("Loading orders timed out.");
        res.status(500).send("Error: Timeout exceeded");
    }, 5000);
    try {
        handleOrdersLoad(function([success, result]) {
            clearTimeout(timeout);
            if (success) {
                res.json(result);
            } else {
                res.status(500).send(result);
            }
        });
    } catch (e) {
        clearTimeout(timeout);
        res.status(500).send("Error with RabbitMQ.");
        rabbitmq_has_problems = true;
        reconnectToRabbitmq().then();
    } 
});

function handleOrdersLoad(callback) {
    if (!rpc_channel) {
        callback([false, "Error: RabbitMQ channel not available"]);
    }

    console.log("Loading orders...");

    rpc_channel.assertQueue('', {
        exclusive: true
    }, function(error2, q) {
        if (error2) {
            callback([false, "Error: error with asserting queue"]);
        }

        var correlationId = generateUuid();

        rpc_channel.consume(q.queue, function(msg) {
            console.log('Received orders: %s', msg.content.toString());
            try {
                const jsonMessage = JSON.parse(msg.content.toString());
                callback([true, jsonMessage]);
            } catch (error) {
                callback([false, msg.content.toString()]);
            } finally {
                rpc_channel.ack(msg);
            }
        });

        rpc_channel.sendToQueue('query_queue',
            Buffer.from(""), {
                headers: { 'query_type': 'read_all_orders' },
                correlationId: correlationId,
                replyTo: q.queue
            });
    });
}

function generateUuid() {
    return Math.random().toString() +
           Math.random().toString() +
           Math.random().toString();
}

app.post('/submitCatering', (req, res) => {
    if (rabbitmq_has_problems) {
        res.status(500).send("Request rejected as the server is facing problems with RabbitMQ.");
        return;
    }
    const timeout = setTimeout(() => {
        console.log("Submitting catering timed out.");
        res.status(500).send("Error: Timeout exceeded");
    }, 5000);
    try {
        console.log("Req: " + JSON.stringify(req.body));
        handleCateringSubmit(function([success, result]) {
            clearTimeout(timeout);
            if (success) {
                res.send(result);
            } else {
                res.status(500).send(result);
            }
        }, req);
    } catch (e) {
        console.log(e);
        clearTimeout(timeout);
        res.status(500).send("Error with RabbitMQ.");
        rabbitmq_has_problems = true;
        reconnectToRabbitmq().then();
    }
});

function handleCateringSubmit(callback, req) {
    if (!command_channel) {
        callback([false, "Error: RabbitMQ channel not available"]);
    }

    console.log("Received command from: " + req.get('Catering-Ordering-ID'));

    const queue = 'command_queue';

    command_channel.assertQueue(queue, {
        durable: true
    });

    const msg = JSON.stringify(req.body);
    const opts = { headers: { 'command_type': 'create_catering', 'user_id': req.get('Catering-Ordering-ID')}};

    command_channel.sendToQueue(queue, Buffer.from(msg), opts);
    console.log("[x] Sent:", msg);

    callback([true, "Catering submitted successfully. You can wait for the server response."]);
}

app.post('/submitOrder', (req, res) => {
    if (rabbitmq_has_problems) {
        res.status(500).send("Request rejected as the server is facing problems with RabbitMQ.");
        return;
    }
    const timeout = setTimeout(() => {
        console.log("Submitting order timed out.");
        res.status(500).send("Error: Timeout exceeded");
    }, 5000);
    try {
        handleOrderSubmit(function([success, result]) {
            clearTimeout(timeout);
            if (success) {
                res.send(result);
            } else {
                res.status(500).send(result);
            }
        }, req);
    } catch (e) {
        clearTimeout(timeout);
        res.status(500).send("Error with RabbitMQ.");
        rabbitmq_has_problems = true;
        reconnectToRabbitmq().then();
    }
});

function handleOrderSubmit(callback, req) {
    if (!command_channel) {
        callback([false, "Error: RabbitMQ channel not available"])
    }
    console.log('Received JSON data:', req.body);

    const queue = 'command_queue';

    command_channel.assertQueue(queue, {
        durable: true
    });

    const msg = JSON.stringify(req.body);
    const opts = { headers: { 'command_type': 'create_order', 'user_id': req.get('Catering-Ordering-ID') }};

    command_channel.sendToQueue(queue, Buffer.from(msg), opts);
    console.log("Order creation command sent: ", msg);

    callback([true, "Order submitted successfully. You can wait for the server response. If order will be processed properly you'll also receive an email with order details."])
}

app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});