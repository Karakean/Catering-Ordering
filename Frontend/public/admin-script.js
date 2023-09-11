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

function handleCateringSubmit(event) {
    event.preventDefault();

    const formData = new FormData(document.getElementById('cateringForm'));

    const formObject = {};
    formData.forEach((value, key) => {
        formObject[key] = value;
    });

    console.log(formObject);

    fetch('/submitCatering', {
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

function loadOrders() {
    fetch('/loadOrders')
        .then(response => response.json())
        .then(orders => {
            const ordersDiv = document.getElementById('ordersDiv');
            const h3_elem = document.createElement('h3');
            h3_elem.textContent = "Orders:"
            const br = document.createElement('br');
            ordersDiv.appendChild(br);

            orders.forEach(order => {
                const orderInfo = document.createElement('div');
                orderInfo.innerHTML = `
                    <p>Order number: ${order.id}</p>
                    <p>Purchaser email: ${order.purchaserEmail}</p>
                    <p>Purchaser phone number: ${order.purchaserPhoneNumber}</p>
                    <p>Purchaser name: ${order.purchaserName}</p>
                    <p>Purchaser surname: ${order.purchaserSurname}</p>
                    <p>Address: ${order.address}</p>
                    <p>Preferred delivery time: ${order.preferredDeliveryTime}</p>
                    <p>Price per month (per order): ${order.pricePerMonthPerOrder}</p>
                    <p>Ordered caterings:</p>
                `;

                order.orderPositions.forEach(position => {
                    const positionInfo = document.createElement('div');
                    positionInfo.innerHTML = `
                        <p>Ordered catering: ${position.cateringName}</p>
                        <p>Quantity: ${position.quantity}</p>
                        <p>Calories: ${position.calories}</p>
                        <p>Price Per Month (Per Catering): ${position.pricePerMonthPerCatering}</p>
                        <p>Price Per Month (Per Order Position): ${position.pricePerMonthPerOrderPosition}</p>
                    `;
                    orderInfo.appendChild(positionInfo);
                });

                ordersDiv.appendChild(orderInfo);
                const br = document.createElement('br');
                ordersDiv.appendChild(br);
            });
        })
        .catch(error => {
            console.error('Error:', error);
    });
}

function loadCaterings() {
    fetch('/loadCaterings')
        .then(response => response.json())
        .then(caterings => {
            const cateringsDiv = document.getElementById('cateringsDiv');
            const h3_elem = document.createElement('h3');
            h3_elem.textContent = "Caterings:"
            const br = document.createElement('br');
            cateringsDiv.appendChild(br);

            caterings.forEach(catering => {
                const cateringInfo = document.createElement('div');
                cateringInfo.innerHTML = `
                    <p>Catering Name: ${catering.name}</p>
                    <p>Description: ${catering.description}</p>
                    <p>Calories: ${catering.calories}</p>
                    <p>Price Per Month: ${catering.pricePerMonth}</p>
                `;
                cateringsDiv.appendChild(cateringInfo);
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

function showCateringForm() {
    document.getElementById('createCateringDiv').style.display = 'block'
    document.getElementById('showCateringFormButton').style.display = 'none';
};

document.getElementById('loadOrdersButton').addEventListener('click', loadOrders);
document.getElementById('cateringForm').addEventListener('submit', handleCateringSubmit);
document.getElementById('loadCateringsButton').addEventListener('click', loadCaterings);
document.getElementById('toggleViewButton').addEventListener('click', toggleView);