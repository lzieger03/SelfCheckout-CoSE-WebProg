document.addEventListener("DOMContentLoaded", () => {
    const adminUsernameText = document.getElementById("admin-username");
    const logoutButton = document.getElementById("logout-button");
    const adminUsername = getCookie('admin-username');
    
    if (adminUsername) {
        console.log('logged in as:', adminUsername);
        adminUsernameText.textContent = adminUsernameText.textContent.replace('x', adminUsername.toUpperCase());
        localStorage.setItem('admin-username', adminUsername);
    } else {
        alert('No admin username cookie found');
        eraseCookie('admin-username');
        localStorage.setItem('admin-username', '');
        window.location.href = "./index.html";
    }
    
    // Logout Functionality
    logoutButton.addEventListener("click", () => {
        // Implement logout logic here (e.g., clear session, redirect to login page)
        alert("Logged out successfully!");
        eraseCookie('admin-username');
        window.location.href = "./index.html"; // Redirect to main page
    });

    // function to get a cookie by its name
    function getCookie(name) {
        const nameEQ = name + "=";
        const cookiesArray = document.cookie.split(';');
        for (let i = 0; i < cookiesArray.length; i++) {
            let c = cookiesArray[i];
            while (c.charAt(0) === ' ') c = c.substring(1, c.length);
            if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
        }
        return null;
    }

    function eraseCookie(name) {   
        document.cookie = name +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';  
    }
});
