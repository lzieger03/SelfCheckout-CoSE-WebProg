document.addEventListener("DOMContentLoaded", () => {
    const adminLoginBtn = document.getElementById("admin-login-btn");
    const adminLoginPopup = document.getElementById("admin-login-popup");
    const adminLoginPopupCloseBtn = document.getElementById("admin-login-popup-close-btn");
    const adminLoginPopupLoginBtn = document.getElementById("admin-login-popup-login-btn");

    // Admin login popup input initialization
    const adminLoginPopupUsernameInput = document.getElementById("admin-login-popup-username-input");
    const adminLoginPopupPasswordInput = document.getElementById("admin-login-popup-password-input");

    adminLoginBtn.addEventListener("click", () => {
        adminLoginPopup.style.display = "flex";
    });

    adminLoginPopupCloseBtn.addEventListener("click", () => {
        adminLoginPopup.style.display = "none";
    });

    adminLoginPopupLoginBtn.addEventListener("click", () => {
        checkAdminLoginInput();
    });


    // Admin login popup input management
    async function checkAdminLoginInput() {
        // Check with the backend if the login and password are correct
        const username = adminLoginPopupUsernameInput.value;
        const password = adminLoginPopupPasswordInput.value;

        // fetch the admin login endpoint if the input is not empty and not only spaces
        if (username.trim() && password.trim()) {
            try {
                const response = await fetch("http://localhost:8080/adminlogin", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({username, password})
                });

                if (!response.ok) {
                    if (response.status === 401) {
                        alert("Invalid login or password");
                    } else {
                        alert(`Error: ${response.statusText}`);
                    }
                    adminLoginPopupUsernameInput.value = "";
                    adminLoginPopupPasswordInput.value = "";
                    adminLoginPopupUsernameInput.focus();

                    // Optionally, handle detailed error messages
                    const errorDetails = await response.text();
                    try {
                        const errorJson = JSON.parse(errorDetails);
                        console.error(`Login Error: ${errorJson.error}`);
                    } catch {
                        console.error(`Login Error: ${errorDetails}`);
                    }
                } else {
                    setCookie('admin-username', username, 1);  // Cookie set for 1 day

                    // if everything is correct, redirect to the admin page
                    window.location.href = "./admin.html";
                }
            } catch (error) {
                console.error("Login request failed:", error);
                alert("An error occurred while processing your request. Please try again.");
            }
        } else {
            alert("Please enter a login and password");
            adminLoginPopupUsernameInput.focus();
        }

    }
   
    // Setze einen Cookie mit dem Namen 'admin-username', einem Wert und einer Ablaufzeit (z.B. 1 Tag)
    function setCookie(name, value, days) {
        let expires = "";
        if (days) {
            const date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
            expires = "; expires=" + date.toUTCString();
        }
        document.cookie = name + "=" + (value || "") + expires + "; path=/";
    }

});
