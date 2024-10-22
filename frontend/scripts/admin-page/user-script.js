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
        try {
            const response = await fetch('http://localhost:8080/allusers');
            if (!response.ok) {
                throw new Error(`Error fetching users: ${response.statusText}`);
            }
            const users = await response.json();
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

            const tdEmail = document.createElement("td");
            tdEmail.textContent = user.email || "-";
            tr.appendChild(tdEmail);

            const tdRole = document.createElement("td");
            tdRole.textContent = user.role;
            tr.appendChild(tdRole);

            const tdActions = document.createElement("td");
            const editBtn = document.createElement("button");
            editBtn.textContent = "Edit";
            editBtn.classList.add("action-btn-edit", "action-btn");
            editBtn.addEventListener("click", () => {
                isEditMode = true;
                console.log("edit user: " + user);
                editUser(user);
            });
            tdActions.appendChild(editBtn);

            const deleteBtn = document.createElement("button");
            deleteBtn.textContent = "Delete";
            deleteBtn.classList.add("action-btn-delete", "action-btn");
            deleteBtn.addEventListener("click", () => deleteUser(user.id));
            tdActions.appendChild(deleteBtn);

            tr.appendChild(tdActions);
            userTableBody.appendChild(tr);
        });
    }

    /**
     * Opens the user popup in add mode.
     * @param {string} role - The role of the user to add ("ADMIN" or "CUSTOMER").
     */
    function openAddUserPopup(role) {
        isEditMode = false;
        editUserId = null;
        userPopupTitle.textContent = `Add New ${role === "ADMIN" ? "Admin" : "Customer"}`;
        userForm.reset();
        document.getElementById("user-id").value = generateUniqueUserId();
        roleSelect.value = role;
        customerFields.style.display = role === "CUSTOMER" ? "flex" : "none";
        userPopup.style.display = "flex";
    }

    /**
     * Generates a unique user ID for a new user.
     */
    function generateUniqueUserId() {
        // Generate a unique user ID (similar logic as products)
        let userId = Math.floor(Math.random() * 1e6).toString().padStart(6, '1');
        return userId;
    }

    /**
     * Handles the click event for adding a new Admin.
     */
    addAdminBtn.addEventListener("click", () => {
        openAddUserPopup("ADMIN");
    });

    /**
     * Handles the click event for adding a new Customer.
     */
    addCustomerBtn.addEventListener("click", () => {
        openAddUserPopup("CUSTOMER");
    });

    /**
     * Closes the user popup.
     */
    userPopupClose.addEventListener("click", () => {
        userPopup.style.display = "none";
    });

    /**
     * Handles the form submission for adding or editing a user.
     * @param {Event} event - The form submission event.
     */
    userForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const id = document.getElementById("user-id").value.trim();
        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value.trim();
        const role = document.getElementById("role").value;

        // Gather customer-specific data if applicable
        let firstName = null;
        let lastName = null;
        let customerEmail = null;

        if (role === "CUSTOMER") {
            firstName = document.getElementById("first-name").value.trim();
            lastName = document.getElementById("last-name").value.trim();
            customerEmail = document.getElementById("customer-email").value.trim();

            if (!firstName || !lastName || !customerEmail) {
                alert("Please fill in all customer-specific fields.");
                return;
            }
        }

        // Prepare the payload based on role
        let payload = {
            uid: id,
            username,
            password,
            role
        };

        if (role === "CUSTOMER") {
            payload.firstName = firstName;
            payload.lastName = lastName;
            payload.email = customerEmail;
        }

        try {
            let response;
            if (isEditMode) {
                // Update existing user
                response = await fetch(`http://localhost:8080/updateuser`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
            } else {
                // Add new user based on role
                const endpoint = role === "ADMIN" ? 'addadmin' : 'addcustomer';
                response = await fetch(`http://localhost:8080/${endpoint}`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
            }

            if (!response.ok) {
                const errorMsg = await response.text();
                throw new Error(errorMsg);
            }

            alert(isEditMode ? "User updated successfully." : "User added successfully.");
            userPopup.style.display = "none";
            initializeUsers();
        } catch (error) {
            console.error(error);
            alert(`Error: ${error.message}`);
        }
    });

    /**
     * Opens the user popup in edit mode with pre-filled data.
     * @param {Object} user - The user object to edit.
     */
    async function editUser(user) {
        isEditMode = true;
        editUserId = user.id;
        userPopupTitle.textContent = "Edit User";

        // Populate form fields with existing user data
        document.getElementById("user-id").value = user.id;
        document.getElementById("username").value = user.username;
        document.getElementById("password").value = ""; // Leave password blank for security
        document.getElementById("role").value = user.role;

        if (user.role === "CUSTOMER") {
            // Fetch customer-specific details
            try {
                const response = await fetch(`http://localhost:8080/user?id=${encodeURIComponent(user.id)}`);
                if (!response.ok) {
                    throw new Error("Failed to fetch customer details.");
                }
                const customer = await response.json();
                document.getElementById("first-name").value = customer.firstName || "";
                document.getElementById("last-name").value = customer.lastName || "";
                document.getElementById("customer-email").value = customer.email || "";
                customerFields.style.display = "flex";
            } catch (error) {
                console.error(error);
                alert("Error fetching customer data.");
                customerFields.style.display = "none";
            }
        } else {
            customerFields.style.display = "none";
        }

        // Disable the ID input field and grey it out
        const userIdInput = document.getElementById("user-id");
        userIdInput.disabled = true;
        userIdInput.style.backgroundColor = "#ccc";

        userPopup.style.display = "flex";
    }

    /**
     * Deletes a user by their ID.
     * @param {string} userId - The ID of the user to delete.
     */
    async function deleteUser(userId) {
        if (!confirm(`Are you sure you want to delete the user "${userId}"?`)) return;

        try {
            const response = await fetch(`http://localhost:8080/deleteuser?id=${encodeURIComponent(userId)}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                const errorMsg = await response.text();
                throw new Error(errorMsg);
            }

            alert("User deleted successfully.");
            initializeUsers();
        } catch (error) {
            console.error("Error deleting user:", error);
            alert(`Error deleting user: ${error.message}`);
        }
    }

    /**
     * Handles the change event for the role selection to toggle customer-specific fields.
     */
    roleSelect.addEventListener("change", () => {
        if (roleSelect.value === "CUSTOMER") {
            userPopupTitle.textContent = userPopupTitle.textContent.replace("Admin", "Customer");
            customerFields.style.display = "flex";
        } else {
            userPopupTitle.textContent = userPopupTitle.textContent.replace("Customer", "Admin");
            customerFields.style.display = "none";
        }
    });

    // Initial Render
    initializeUsers();
});
