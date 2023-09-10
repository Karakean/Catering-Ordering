const express = require('express');
const path = require('path');
const amqp = require('amqplib/callback_api');
let channel;

const app = express();
const port = process.env.PORT || 3000;

app.use(express.static(path.join(__dirname, 'public')));
app.use(express.json());

amqp.connect('amqp://localhost', function(error0, connection) {
    if (error0) {
        console.log("Error connecting to RabbitMQ:", error0);
    }

    connection.createChannel(function(error1, ch) {
        if (error1) {
            console.log("Error creating channel:", error1);
        }

        channel = ch;
        console.log("Connected to rabbitMQ");
    });
});


app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

// app.get('/loadOrders', (req, res) => {
//   console.log('loading...');
//   amqp.connect('amqp://localhost', function(error0, connection) {
//         if (error0) {
//             console.log("Error connecting to RabbitMQ:", error0);
//             res.status(500).send("Error connecting to RabbitMQ");
//             return;
//         }
        
//         connection.createChannel(function(error1, channel) {
//             if (error1) {
//                 console.log("Error creating channel:", error1);
//                 res.status(500).send("Error creating RabbitMQ channel");
//                 return;
//             }

//             channel.assertQueue('query_queue', {
//                 durable: true
//             });

//             channel.sendToQueue('query_queue', Buffer.from('ABC'));

//             // Set up a consumer callback
//             channel.consume('orders_queue', (message) => {
//               if (message !== null) {
//                 // Process the received message
//                 console.log('Received message:', message.content.toString());
//                 // Acknowledge the message to remove it from the queue
//                 channel.ack(message);
//               }
//             });
//         });

//         setTimeout(function() {
//             connection.close();
//         }, 500);
//     });
//     res.send('Function executed on the server.');
// });

// function sendMessageToQueue(req) {
//     const queue = 'query_queue';

//     return new Promise((resolve, reject) => {
//         channel.assertQueue(queue, { durable: true });

//         const msg = JSON.stringify(req.body);
//         const opts = { headers: { 'query_type': 'read_all_caterings' }};

//         channel.sendToQueue(queue, Buffer.from(msg), opts);
//         console.log("[x] Sent:", msg);

//         console.log('Catering reading request sent');

//         resolve();
//     });
// }

// async function consumeData() {
//     const response_queue = 'query_response_queue';

//     return new Promise((resolve, reject) => {
//         channel.assertQueue(response_queue);

//         channel.prefetch(1)

//         channel.consume(response_queue, (message) => {
//             const consumedData = JSON.parse(message.content.toString());
//             console.log("[x] Received:", consumedData);
//             channel.ack(message);
//             resolve(consumedData);
//         });
//     });
// }

app.get('/loadCaterings', (req, res) => {
    if (!channel) {
        res.status(500).send("Error: RabbitMQ channel not available");
        return;
    }

    const queue = 'query_queue';

    channel.assertQueue(queue, { durable: true });

    const msg = JSON.stringify(req.body);
    const opts = { headers: { 'query_type': 'read_all_caterings' }};

    channel.sendToQueue(queue, Buffer.from(msg), opts);
    console.log("[x] Sent:", msg);

    const response_queue = 'query_response_queue'

    var content = '';

    channel.prefetch(1);

    channel.assertQueue(response_queue, {
        durable: true
    });

    channel.consume(response_queue, (msg) => {
        content = msg.content.toString();
        console.log("[x] Received:", content);
        channel.ack(msg);
    });

    console.log(JSON.parse(content));
    res.json(JSON.parse(content));
});

app.post('/submitCatering', (req, res) => {
    if (!channel) {
        res.status(500).send("Error: RabbitMQ channel not available");
        return;
    }

    const queue = 'command_queue';

    channel.assertQueue(queue, {
        durable: true
    });

    const msg = JSON.stringify(req.body);
    const opts = { headers: { 'command_type': 'create_catering' }};

    channel.sendToQueue(queue, Buffer.from(msg), opts);
    console.log("[x] Sent:", msg);

    res.send('Catering submitted successfully');
});

// app.post('/submitOrder', (req, res) => {
//     console.log('Received JSON data:', req.body);

//     amqp.connect('amqp://localhost', function(error0, connection) {
//         if (error0) {
//             console.log("Error connecting to RabbitMQ:", error0);
//             res.status(500).send("Error connecting to RabbitMQ");
//             return;
//         }
        
//         connection.createChannel(function(error1, channel) {
//             if (error1) {
//                 console.log("Error creating channel:", error1);
//                 res.status(500).send("Error creating RabbitMQ channel");
//                 return;
//             }

//             const queue = 'command_queue';

//             channel.assertQueue(queue, {
//                 durable: true
//             });

//             const msg = JSON.stringify(req.body);

//             channel.sendToQueue(queue, Buffer.from(msg));
//             console.log("[x] Sent:", msg);

//             res.send('Order submitted successfully');
//         });

//         setTimeout(function() {
//             connection.close();
//         }, 500);
//     });
// });

// Start the server
app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});