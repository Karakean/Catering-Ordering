const express = require('express');
const path = require('path');
const amqp = require('amqplib/callback_api');
const WebSocket = require('ws');
const wss = new WebSocket.Server({ port: 7071 });
let publishing_connection;
let consuming_connection;
let command_channel_caterings;
let command_channel_orders;
let rpc_channel_caterings;
let rpc_channel_orders;
let polling_channel_command_result;

const app = express();
const port = process.env.PORT || 3000;

app.use(express.static(path.join(__dirname, 'public')));
app.use(express.json());

const connected_clients = [];

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

amqp.connect('amqp://localhost', function(error0, conn) {
    if (error0) {
        console.log("Error creating publishing/rpc connection to RabbitMQ. Error: ", error0);
        return;
    }

    console.log("Publishing connection created.");
    publishing_connection = conn;

    publishing_connection.createChannel(function(error1, ch) {
        if (error1) {
            console.log("Error creating rpc caterings channel:", error1);
        }
        rpc_channel_caterings = ch;
        console.log("RPC caterings channel created");
    });

    publishing_connection.createChannel(function(error1, ch) {
        if (error1) {
            console.log("Error creating rpc orders channel:", error1);
        }
        rpc_channel_orders = ch;
        console.log("RPC orders channel created");
    });

    publishing_connection.createChannel(function(error1, ch) {
        if (error1) {
            console.log("Error creating command channel:", error1);
        }
        command_channel_caterings = ch;
        console.log("Command channel created");
    });

    publishing_connection.createChannel(function(error1, ch) {
        if (error1) {
            console.log("Error creating command channel:", error1);
        }
        command_channel_orders = ch;
        console.log("Command channel created");
    });
});

amqp.connect('amqp://localhost', function(error0, conn) {
    if (error0) {
        console.log("Error creating consuming connection to RabbitMQ. Error: ", error0);
        return;
    }

    console.log("Consuming connection created.");
    consuming_connection = conn;

    consuming_connection.createChannel(function(error1, ch) {
        if (error1) {
            console.log("Error creating polling channel:", error1);
        }
        polling_channel_command_result = ch;
        console.log("Polling channel created");

        const queueName = 'command_response_queue';
        polling_channel_command_result.assertQueue(queueName, { durable: true });

        polling_channel_command_result.consume(queueName, function(message) {
            const messageContent = message.content.toString();

            connected_clients.forEach(client => {
                console.log("Client: " + message.properties.headers['user_id']);
                if (client.cateringOrderingId == message.properties.headers['user_id']) {
                    client.send(messageContent);
                }
            });

            console.log(`Received message: ${messageContent}`);
        }, { noAck: true });
    });
});


app.get('/', (_req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

app.get('/admin', (_req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'admin.html'));
});

app.get('/loadCaterings', (_req, res) => {
    if (!rpc_channel_caterings) {
        res.status(500).send("Error: RabbitMQ channel not available");
        return;
    }

    rpc_channel_caterings.assertQueue('', {
        exclusive: true
      }, function(error2, q) {
        if (error2) {
          throw error2;
        }

        var correlationId = generateUuid();
  
        rpc_channel_caterings.consume(q.queue, function(msg) {
            console.log('Received caterings: %s', msg.content.toString());
            res.json(JSON.parse(msg.content.toString()));
            // setTimeout(function() {
            //     connection.close();
            //     process.exit(0)
            // }, 500);
        }, {
          noAck: true
        });
  
        rpc_channel_caterings.sendToQueue('query_queue',
          Buffer.from(""),{
            headers: { 'query_type': 'read_all_caterings' },
            correlationId: correlationId,
            replyTo: q.queue });
    });
});

app.get('/loadOrders', (_req, res) => {
    if (!rpc_channel_orders) {
        res.status(500).send("Error: RabbitMQ channel not available");
        return;
    }

    rpc_channel_orders.assertQueue('', {
        exclusive: true
      }, function(error2, q) {
        if (error2) {
          throw error2;
        }

        var correlationId = generateUuid();
  
        rpc_channel_orders.consume(q.queue, function(msg) {
            console.log('Received orders: %s', msg.content.toString());
            try {
                const jsonMessage = JSON.parse(msg.content.toString())
                res.json(jsonMessage);
            } catch {
                res.status(500).send(msg.content.toString())
            }
            // setTimeout(function() {
            //     connection.close();
            //     process.exit(0)
            // }, 500);
        }, {
          noAck: true
        });
  
        rpc_channel_orders.sendToQueue('query_queue',
          Buffer.from(""),{
            headers: { 'query_type': 'read_all_orders' },
            correlationId: correlationId,
            replyTo: q.queue });
    });
});

function generateUuid() {
    return Math.random().toString() +
           Math.random().toString() +
           Math.random().toString();
}

app.post('/submitCatering', (req, res) => {
    if (!command_channel_caterings) {
        res.status(500).send("Error: RabbitMQ channel not available");
        return;
    }

    console.log("Received command from: " + req.get('Catering-Ordering-ID'));

    const queue = 'command_queue';

    command_channel_caterings.assertQueue(queue, {
        durable: true
    });

    const msg = JSON.stringify(req.body);
    const opts = { headers: { 'command_type': 'create_catering', 'user_id': req.get('Catering-Ordering-ID')}};

    command_channel_caterings.sendToQueue(queue, Buffer.from(msg), opts);
    console.log("[x] Sent:", msg);

    res.send('Catering submitted successfully');
});

app.post('/submitOrder', (req, res) => {
    if (!command_channel_orders) {
        res.status(500).send("Error: RabbitMQ channel not available");
        return;
    }
    console.log('Received JSON data:', req.body);

    //amqp.connect('amqp://localhost', function(error0, connection) {
        // if (error0) {
        //     console.log("Error connecting to RabbitMQ:", error0);
        //     res.status(500).send("Error connecting to RabbitMQ");
        //     return;
        // }
        
        //connection.createChannel(function(error1, channel) {
            // if (error1) {
            //     console.log("Error creating channel:", error1);
            //     res.status(500).send("Error creating RabbitMQ channel");
            //     return;
            // }

            const queue = 'command_queue';

            command_channel_orders.assertQueue(queue, {
                durable: true
            });

            const msg = JSON.stringify(req.body);
            const opts = { headers: { 'command_type': 'create_order', 'user_id': req.get('Catering-Ordering-ID') }};

            command_channel_orders.sendToQueue(queue, Buffer.from(msg), opts);
            console.log("Order creation command sent: ", msg);

            res.send('Order submitted successfully');
        //});

        // setTimeout(function() {
        //     connection.close();
        // }, 500);
    //});
});

app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});