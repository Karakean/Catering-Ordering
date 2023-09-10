#!/usr/bin/env node

async function sendToQueue() {
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
};

const orderForm = document.getElementById('order-form');
orderForm.addEventListener('submit', (e) => {
  console.log("XD");
  sendToQueue();
});



