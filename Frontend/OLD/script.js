const form = document.getElementById('order-form');
        const productInput = document.getElementById('product');
        const quantityInput = document.getElementById('quantity');

        // Create a STOMP client instance
        const socket = new WebSocket('ws://127.0.0.1:15674/ws');
        const client = Stomp.over(socket);

        // Connect to the STOMP server
        client.connect({}, () => {
            form.addEventListener('submit', (event) => {
                event.preventDefault();

                // Get the product and quantity from the form
                const product = productInput.value;
                const quantity = quantityInput.value;

                // Create a JSON object with the order data
                const order = {
                    product: product,
                    quantity: quantity,
                };

                // Send the order data to the RabbitMQ queue
                client.send('/amq/queue/query_queue', {}, "XD");

                // Clear the form fields
                productInput.value = '';
                quantityInput.value = '';
            });
        });