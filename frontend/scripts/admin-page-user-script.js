document.addEventListener("DOMContentLoaded", () => {
    const addUserBtn = document.getElementById("add-user-btn");
    const userPopup = document.getElementById("user-popup");
    const userPopupClose = document.getElementById("user-popup-close");
    const userForm = document.getElementById("user-form");
    const userTableBody = document.querySelector("#user-table tbody");
    const userPopupTitle = document.getElementById("user-popup-title");

    let isEditMode = false;
    let editUserId = null;

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
            tdEmail.textContent = user.email;
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
                openEditUserPopup(user.id);
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
     * Initializes the user list by fetching from the backend.
     */
    async function initializeUsers() {
        const users = await fetchAllUsers();
        renderUsers(users);
    }

    // Open Add User Popup
    addUserBtn.addEventListener("click", () => {
        isEditMode = false;
        userPopupTitle.textContent = "Add New User";
        userForm.reset();
        userPopup.style.display = "flex";

        // Generate a unique user ID (similar logic as products)
        let userId = Math.floor(Math.random() * 1e6).toString().padStart(6, '1');
        document.getElementById("user-id").value = userId;

        // Disable the ID input field and grey it out
        const userIdInput = document.getElementById("user-id");
        userIdInput.disabled = true;
        userIdInput.style.backgroundColor = "#ccc";
    });

    // Close Popup
    userPopupClose.addEventListener("click", () => {
        userPopup.style.display = "none";
    });

    /**
     * Handles the form submission for adding or editing a user.
     * Sends POST or PUT requests based on the mode.
     */
    userForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const id = document.getElementById("user-id").value.trim();
        const username = document.getElementById("username").value.trim();
        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("password").value.trim();
        const role = document.getElementById("role").value;

        if (!id || !username || !email || !password || !role) {
            alert("Please fill in all required fields.");
            return;
        }

        const userData = { id, username, email, password, role };

        try {
            let response;
            if (isEditMode) {
                // Update User
                response = await fetch(`http://localhost:8080/updateuser`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(userData)
                });
                if (response.ok) {
                    alert("User updated successfully!");
                } else {
                    const errorData = await response.json();
                    alert(`Failed to update user: ${errorData.error}`);
                    return;
                }
            } else {
                // Add New User
                response = await fetch(`http://localhost:8080/adduser`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(userData)
                });
                if (response.ok) {
                    alert("User added successfully!");
                } else {
                    const errorData = await response.json();
                    alert(`Failed to add user: ${errorData.error}`);
                    return;
                }
            }

            userPopup.style.display = "none";
            initializeUsers();
        } catch (error) {
            console.error(error);
            alert("Error saving user.");
        }
    });

    /**
     * Opens the edit user popup with pre-filled data.
     * @param {string} userId - The ID of the user to edit.
     */
    async function openEditUserPopup(userId) {
        try {
            const response = await fetch(`http://localhost:8080/user?id=${encodeURIComponent(userId)}`);
            if (!response.ok) {
                throw new Error(`Error fetching user: ${response.statusText}`);
            }
            const user = await response.json();
            isEditMode = true;
            editUserId = user.id;
            userPopupTitle.textContent = "Edit User";

            // Populate form fields with existing user data
            document.getElementById("user-id").value = user.id;
            document.getElementById("username").value = user.username;
            document.getElementById("email").value = user.email;
            document.getElementById("role").value = user.role;

            // Disable the ID input field and grey it out
            const userIdInput = document.getElementById("user-id");
            userIdInput.disabled = true;
            userIdInput.style.backgroundColor = "#ccc";

            userPopup.style.display = "flex";
        } catch (error) {
            console.error(error);
            alert("Error fetching user data.");
        }
    }

    /**
     * Deletes a user by its ID.
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

    // Initial Render
    initializeUsers();
});