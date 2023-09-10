const express = require('express');
const path = require('path');

const app = express();
const port = process.env.PORT || 3000;

app.use(express.static(path.join(__dirname, 'public')));

app.get('/executeFunction', (_req, res) => {
    someFunction();
    res.send('Function executed on the server.');
});

// Start the server
app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});

// Define your function to be executed on button click
function someFunction() {
    // Your custom logic here
    var amqp = require('amqplib/callback_api');
  amqp.connect('amqp://localhost', function(error0, connection) {
    if (error0) {
      console.log("XD1");
      throw error0;
    }
    connection.createChannel(function(error1, channel) {
      if (error1) {
        console.log("XD2");
        throw error1;
      }
      var queue = 'command_queue';
      var msg = 'Hello :)';

      channel.assertQueue(queue, {
        durable: true
      });

      channel.sendToQueue(queue, Buffer.from(msg));
      console.log("\n[x] Sent %s", msg);
    });
    setTimeout(function() {
          connection.close();
          process.exit(0);
      }, 500);
  });
}