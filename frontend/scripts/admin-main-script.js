document.addEventListener("DOMContentLoaded", () => {
    const adminLoginBtn = document.getElementById("admin-login-btn");
    const adminLoginPopup = document.getElementById("admin-login-popup");
    const adminLoginPopupCloseBtn = document.getElementById("admin-login-popup-close-btn");
    const adminLoginPopupLoginBtn = document.getElementById("admin-login-popup-login-btn");

    // Admin login popup input initialization
    const adminLoginPopupLoginInput = document.getElementById("admin-login-popup-login-input");
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
    function checkAdminLoginInput() {
        // Check with the backend if the login and password are correct
        const login = adminLoginPopupLoginInput.value;
        const password = adminLoginPopupPasswordInput.value;
        console.log(login, password);   

        // fetch the admin login endpoint if the input is not empty and not only spaces
        if (login.trim() && password.trim()) {
            fetch("/admin/login", {
                method: "POST",
                body: JSON.stringify({ login, password }),
            })
            .then(response => response.json())
            .then(data => {
                console.log(data);
            })
            .catch(error => {
                console.error("Error checking admin login input:", error);
            });
            // if everything is correct, redirect to the admin page,
            // otherwise show an error and clear the input and password input
            if (data.success) {
                adminLoginPopup.style.display = "none";
                window.location.href = "/admin";
            } else {
                alert("Invalid login or password");
                adminLoginPopupLoginInput.value = "";
                adminLoginPopupPasswordInput.value = "";
                adminLoginPopupLoginInput.focus();
            }
        } else {
            alert("Please enter a login and password");
            adminLoginPopupLoginInput.focus();
        }

    }
   
});