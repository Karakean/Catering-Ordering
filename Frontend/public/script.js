function getOrSetCookie() {
    const cookieName = "cateringOrderingId";
    let userId = getCookie(cookieName);

    if (!userId) {
        userId = generateRandomUserId();
        setCookie(cookieName, userId);
    }

    return userId;
}
  
function generateRandomUserId() {
    return Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
}
  
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

function setCookie(name, value) {
    document.cookie = `${name}=${value}`;
}

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
        .then(response => response.json())
        .then(caterings => {
            const orderPositionsDiv = document.getElementById('orderPositions');

            caterings.forEach(catering => {
                const label = document.createElement('label');
                label.textContent = catering.name;

                const input = document.createElement('input');
                input.type = 'number';
                input.min = 0;
                input.value = 0;
                input.dataset.cateringId = catering.id;

                label.appendChild(input);
                orderPositionsDiv.appendChild(label);
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
    const targetUrl = currentUrl.includes('index.html') ? 'admin.html' : 'index.html';
    window.location.href = targetUrl;
};

document.addEventListener('DOMContentLoaded', loadCaterings);
document.getElementById('orderForm').addEventListener('submit', handleOrderSubmit);
document.getElementById('toggleViewButton').addEventListener('click', toggleView);