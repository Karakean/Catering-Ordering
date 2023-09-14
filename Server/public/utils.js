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