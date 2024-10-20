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
    async function checkAdminLoginInput() {
        // Check with the backend if the login and password are correct
        const login = adminLoginPopupLoginInput.value;
        const password = adminLoginPopupPasswordInput.value;
        console.log(login, password);   

        // fetch the admin login endpoint if the input is not empty and not only spaces
        if (login.trim() && password.trim()) {
            try {
                const response = await fetch("http://localhost:8080/login", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ login, password })
                  });
        
                  if (!response.ok) {
                    if (response.status === 401) {
                        alert("Invalid login or password");
                    } else {
                        alert(`Error: ${response.statusText}`);
                    }
                    adminLoginPopupLoginInput.value = "";
                    adminLoginPopupPasswordInput.value = "";
                    adminLoginPopupLoginInput.focus();

                    // Optionally, handle detailed error messages
                    const errorDetails = await response.text();
                    try {
                      const errorJson = JSON.parse(errorDetails);
                      console.error(`Login Error: ${errorJson.error}`);
                    } catch {
                      console.error(`Login Error: ${errorDetails}`);
                    }
                  } else {
                    // if everything is correct, redirect to the admin page
                    window.location.href = "./admin.html";
                  }
            } catch (error) {
                console.error("Login request failed:", error);
                alert("An error occurred while processing your request. Please try again.");
            }
        } else {
            alert("Please enter a login and password");
            adminLoginPopupLoginInput.focus();
        }

    }
   
});
