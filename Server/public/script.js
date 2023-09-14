const cateringOrderingId = getOrSetCookie();
const socket = new WebSocket('ws://localhost:7071');

socket.addEventListener('open', (event) => {
    console.log('WebSocket connection opened');
    socket.send(JSON.stringify({ type: 'cateringOrderingId', cateringOrderingId }));
});
  
socket.addEventListener('message', (event) => {
    alert(event.data);
});

socket.addEventListener('error', (event) => {
    console.error('WebSocket error:', event);
});

function handleOrderSubmit(event) {
    event.preventDefault();

    const formData = new FormData(document.getElementById('orderForm'));

    const orderPositions = [];

    document.querySelectorAll('#orderPositions input').forEach(input => {
        const cateringId = input.dataset.cateringId;
        const quantity = parseInt(input.value);

        if (quantity > 0) {
            orderPositions.push({
                cateringId: parseInt(cateringId),
                quantity: quantity
            });
        }
    });

    const formObject = {};
    formData.forEach((value, key) => {
        formObject[key] = value;
    });
    formObject["orderPositions"] = orderPositions;

    console.log("Sending order:" + JSON.stringify(formObject));

    fetch('/submitOrder', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Catering-Ordering-ID': cateringOrderingId
        },
        body: JSON.stringify(formObject)
    })
    .then(response => response.text())
    .then(message => {
        console.log(message);
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function loadCaterings() {
    fetch('/loadCaterings')
        .then(async response => {
            if (response.ok) {
                return response.json(); 
            }
            const errorMessage = await response.text();
            throw new Error(`Error from server. Status: ${response.status}, message: ${errorMessage}`);
        })
        .then(caterings => {
            const orderPositionsDiv = document.getElementById('orderPositions');

            caterings.forEach(catering => {
                const cateringInfo = document.createElement('div');
                cateringInfo.className =  'order-position';
                cateringInfo.innerHTML = `
                    <p>Catering came: ${catering.name}</p>
                    <p>Description: ${catering.description}</p>
                    <p>Calories per day: ${catering.calories}</p>
                    <p>Price per month: ${catering.pricePerMonth}</p>
                `;

                const input = document.createElement('input');
                input.type = 'number';
                input.min = 0;
                input.value = 0;
                input.dataset.cateringId = catering.id;
                cateringInfo.appendChild(input)
                orderPositionsDiv.appendChild(cateringInfo);
                const br = document.createElement('br');
                orderPositionsDiv.appendChild(br);
            });
        })
        .catch(error => {
            console.error('Error:', error);
    });
};

function toggleView() {
    const currentUrl = window.location.href;
    const targetUrl = currentUrl.includes('admin.html') ? 'index.html' : 'admin.html';
    window.location.href = targetUrl;
};

document.addEventListener('DOMContentLoaded', loadCaterings);
document.getElementById('toggleViewButton').addEventListener('click', toggleView);
document.getElementById('orderForm').addEventListener('submit', handleOrderSubmit);