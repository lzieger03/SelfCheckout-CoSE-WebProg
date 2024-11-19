document.addEventListener("DOMContentLoaded", () => {
    const addAdminBtn = document.getElementById("add-admin-btn");
    const addCustomerBtn = document.getElementById("add-customer-btn");
    const userPopup = document.getElementById("user-popup");
    const userPopupClose = document.getElementById("user-popup-close");
    const userForm = document.getElementById("user-form");
    const userTableBody = document.querySelector("#user-table tbody");
    const userPopupTitle = document.getElementById("user-popup-title");
    const roleSelect = document.getElementById("role");
    const customerFields = document.getElementById("customer-fields");

    let isEditMode = false;
    let editUserId = null;
    let isUserListStale = localStorage.getItem("usersStale") !== "false";

    /**
     * Initializes the user management by fetching and rendering all users.
     */
    async function initializeUsers() {
        const users = await fetchAllUsers();
        renderUsers(users);
    }

    /**
     * Fetches all users from the backend API.
     * @returns {Promise<Array>} A promise that resolves to an array of users.
     */
    async function fetchAllUsers() {
        // If users are cached and not stale, return from localStorage
        if (!isUserListStale && localStorage.getItem("users")) {
            try {
                return JSON.parse(localStorage.getItem("users"));
            } catch (error) {
                console.warn("Error parsing cached users:", error);
                isUserListStale = true;
            }
        }

        try {
            const response = await fetch('http://localhost:3000/users');
            if (!response.ok) {
                throw new Error(`Error fetching users: ${response.statusText}`);
            }
            const users = await response.json();
            
            try {
                localStorage.setItem("users", JSON.stringify(users));
                localStorage.setItem("usersStale", "false");
                isUserListStale = false;
            } catch (error) {
                console.warn("Failed to cache users:", error);
            }

            return users;
        } catch (error) {
            console.error(error);
            alert("Failed to fetch users.");
            return [];
        }
    }

    /**
     * Renders the list of users in the admin table.
     * @param {Array} users - An array of user objects.
     */
    function renderUsers(users) {
        userTableBody.innerHTML = "";
        users.forEach(user => {
            const tr = document.createElement("tr");

            const tdId = document.createElement("td");
            tdId.textContent = user.id;
            tr.appendChild(tdId);

            const tdUsername = document.createElement("td");
            tdUsername.textContent = user.username;
            tr.appendChild(tdUsername);

            const tdRole = document.createElement("td");
            tdRole.textContent = user.role;
            tr.appendChild(tdRole);

            const tdActions = document.createElement("td");

            const editButton = document.createElement("button");
            editButton.textContent = "Edit";
            editButton.classList.add("edit-btn");
            editButton.addEventListener("click", () => openEditUserPopup(user.id));
            tdActions.appendChild(editButton);

            const deleteButton = document.createElement("button");
            deleteButton.textContent = "Delete";
            deleteButton.classList.add("delete-btn");
            deleteButton.addEventListener("click", () => deleteUser(user.id));
            tdActions.appendChild(deleteButton);

            tr.appendChild(tdActions);

            userTableBody.appendChild(tr);
        });
    }

    /**
     * Initializes the user management by fetching and rendering all users.
     */
    async function initializeUsers() {
        const users = await fetchAllUsers();
        renderUsers(users);
    }

    /**
     * Opens the popup for adding or editing a user.
     * @param {string} [id] - The user ID to edit. If undefined, opens in add mode.
     */
    function openUserPopup(id = null) {
        if (id) {
            isEditMode = true;
            editUserId = id;
            userPopupTitle.textContent = "Edit User";
            // Pre-fill the form with existing user data
            const users = JSON.parse(localStorage.getItem("users"));
            const user = users.find(u => u.id === id);
            if (user) {
                document.getElementById("user-id").value = user.id;
                document.getElementById("username").value = user.username;
                document.getElementById("password").value = user.password;
                roleSelect.value = user.role;
                if (user.role === "CUSTOMER") {
                    customerFields.style.display = "flex";
                    // Assuming customer-specific details are available
                    // Populate customer fields if available
                } else {
                    customerFields.style.display = "none";
                }
                document.getElementById("user-id").disabled = true; // Prevent changing ID
            }
        } else {
            isEditMode = false;
            editUserId = null;
            userPopupTitle.textContent = "Add User";
            userForm.reset();
            customerFields.style.display = "none";
            document.getElementById("user-id").disabled = false;
        }
        userPopup.style.display = "flex";
    }

    /**
     * Closes the user popup.
     */
    function closeUserPopup() {
        userPopup.style.display = "none";
    }

    /**
     * Handles the submission of the user form for adding or editing.
     * @param {Event} event - The form submission event.
     */
    userForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        const id = document.getElementById("user-id").value.trim();
        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value.trim();
        const role = roleSelect.value;

        if (!id || !username || !password || !role) {
            alert("Please provide valid user details.");
            return;
        }

        try {
            if (isEditMode) {
                // Edit existing user
                const response = await fetch(`http://localhost:3000/users/${encodeURIComponent(id)}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ id, username, password, role })
                });

                if (!response.ok) {
                    const errorMsg = await response.text();
                    throw new Error(errorMsg);
                }

                isUserListStale = true;
                localStorage.setItem("usersStale", "true");
                alert("User updated successfully.");
                closeUserPopup();
                initializeUsers(); // Refresh the user list
            } else {
                // Add new user
                const response = await fetch('http://localhost:3000/users', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ id, username, password, role })
                });

                if (!response.ok) {
                    const errorMsg = await response.text();
                    throw new Error(errorMsg);
                }

                isUserListStale = true;
                localStorage.setItem("usersStale", "true");
                alert("User added successfully.");
                closeUserPopup();
                initializeUsers(); // Refresh the user list
            }
        } catch (error) {
            console.error("Error saving user:", error);
            alert(`Error saving user: ${error.message}`);
        }
    });

    /**
     * Opens the edit popup with the user details pre-filled.
     * @param {string} id - The user ID to edit.
     */
    function openEditUserPopup(id) {
        openUserPopup(id);
    }

    /**
     * Deletes a user by their ID.
     * @param {string} userId - The ID of the user to delete.
     */
    async function deleteUser(userId) {
        if (!confirm(`Are you sure you want to delete the user "${userId}"?`)) return;

        try {
            const response = await fetch(`http://localhost:3000/users/${encodeURIComponent(userId)}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                const errorMsg = await response.text();
                throw new Error(errorMsg);
            }

            isUserListStale = true;
            localStorage.setItem("usersStale", "true");
            alert("User deleted successfully.");
            initializeUsers(); // Refresh the user list
        } catch (error) {
            console.error("Error deleting user:", error);
            alert(`Error deleting user: ${error.message}`);
        }
    }

    // Event Listeners
    addAdminBtn.addEventListener("click", () => {
        roleSelect.value = "ADMIN";
        openUserPopup();
    });
    addCustomerBtn.addEventListener("click", () => {
        roleSelect.value = "CUSTOMER";
        openUserPopup();
    });
    userPopupClose.addEventListener("click", closeUserPopup);

    /**
     * Handles role selection to show/hide customer-specific fields.
     */
    roleSelect.addEventListener("change", () => {
        if (roleSelect.value === "CUSTOMER") {
            userPopupTitle.textContent = "Add Customer";
            customerFields.style.display = "flex";
        } else {
            userPopupTitle.textContent = "Add Admin";
            customerFields.style.display = "none";
        }
    });

    /**
     * Handles customer-specific details if needed.
     * Add any additional logic here.
     */

    // Initial Render
    initializeUsers();
});
