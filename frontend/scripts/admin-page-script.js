document.addEventListener("DOMContentLoaded", () => {
    const logoutButton = document.getElementById("logout-button");
    const addProductBtn = document.getElementById("add-product-btn");
    const productPopup = document.getElementById("product-popup");
    const productPopupClose = document.getElementById("product-popup-close");
    const productForm = document.getElementById("product-form");
    const productTableBody = document.querySelector("#product-table tbody");
    const productPopupTitle = document.getElementById("product-popup-title");

    let isEditMode = false;
    let editProductId = null;

    /**
     * Fetches all products from the backend API.
     * @returns {Promise<Array>} A promise that resolves to an array of products.
     */
    async function fetchAllProducts() {
        try {
            const response = await fetch('http://localhost:8080/allproducts');
            if (!response.ok) {
                throw new Error(`Error fetching products: ${response.statusText}`);
            }
            const products = await response.json();
            return products;
        } catch (error) {
            console.error(error);
            alert("Failed to fetch products.");
            return [];
        }
    }

    /**
     * Renders the list of products in the admin table.
     * @param {Array} products - An array of product objects.
     */
    function renderProducts(products) {
        productTableBody.innerHTML = "";
        products.forEach(product => {
            const tr = document.createElement("tr");

            const tdImg = document.createElement("td");
            tdImg.innerHTML = `<img src="/backend/itemPictures/${product.id}.png" alt="${product.name}">`;
            tr.appendChild(tdImg);

            const tdId = document.createElement("td");
            tdId.textContent = product.id;
            tr.appendChild(tdId);

            const tdName = document.createElement("td");
            tdName.textContent = product.name;
            tr.appendChild(tdName);

            const tdPrice = document.createElement("td");
            tdPrice.textContent = product.price.toFixed(2);
            tr.appendChild(tdPrice);

            const tdActions = document.createElement("td");

            const editBtn = document.createElement("button");
            editBtn.textContent = "Edit";
            editBtn.classList.add("action-btn-edit", "action-btn");
            editBtn.addEventListener("click", () => openEditProductPopup(product.id));
            tdActions.appendChild(editBtn);

            const deleteBtn = document.createElement("button");
            deleteBtn.textContent = "Delete";
            deleteBtn.classList.add("action-btn-delete", "action-btn");
            deleteBtn.addEventListener("click", () => deleteProduct(product.id));
            tdActions.appendChild(deleteBtn);

            tr.appendChild(tdActions);

            productTableBody.appendChild(tr);
        });
    }

    /**
     * Initializes the product list by fetching from the backend.
     */
    async function initializeProducts() {
        const products = await fetchAllProducts();
        renderProducts(products);
    }

    // Open Add Product Popup
    addProductBtn.addEventListener("click", () => {
        isEditMode = false;
        editProductId = null;
        productPopupTitle.textContent = "Add New Product";
        productForm.reset();
        productPopup.style.display = "flex";
    });

    // Close Popup
    productPopupClose.addEventListener("click", () => {
        productPopup.style.display = "none";
    });

    /**
     * Handles the form submission for adding or editing a product.
     * Sends POST or PUT requests based on the mode.
     */
    productForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const id = document.getElementById("product-id").value.trim();
        const name = document.getElementById("product-name").value.trim();
        const price = parseFloat(document.getElementById("product-price").value);

        if (isEditMode) {
            // Update Product
            try {
                const response = await fetch(`http://localhost:8080/product`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ id, name, price })
                });
                if (response.ok) {
                    alert("Product updated successfully!");
                } else {
                    const errorData = await response.text();
                    alert(`Failed to update product: ${errorData}`);
                }
            } catch (error) {
                console.error(error);
                alert("Error updating product.");
            }
        } else {
            // Add New Product
            try {
                const response = await fetch(`http://localhost:8080/product`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ id, name, price })
                });
                if (response.ok) {
                    alert("Product added successfully!");
                } else {
                    const errorData = await response.text();
                    alert(`Failed to add product: ${errorData}`);
                }
            } catch (error) {
                console.error(error);
                alert("Error adding product.");
            }
        }

        // Refresh product list
        await initializeProducts();
        productPopup.style.display = "none";
    });

    /**
     * Opens the Edit Product Popup with pre-filled data.
     * @param {string} productId - The ID of the product to edit.
     */
    async function openEditProductPopup(productId) {
        isEditMode = true;
        editProductId = productId;
        const product = await getProductById(productId);
        if (product) {
            productPopupTitle.textContent = "Edit Product";
            document.getElementById("product-popup-image").src = `/backend/itemPictures/${product.id}.png`;
            document.getElementById("product-id").value = product.id;
            document.getElementById("product-name").value = product.name;
            document.getElementById("product-price").value = product.price;
            productPopup.style.display = "flex";
        }
    }

    /**
     * Fetches a single product by ID from the backend.
     * @param {string} productId - The ID of the product to fetch.
     * @returns {Promise<Object|null>} The product object or null if not found.
     */
    async function getProductById(productId) {
        try {
            const response = await fetch(`http://localhost:8080/product?id=${productId}`);
            if (!response.ok) {
                throw new Error(`Error fetching product: ${response.statusText}`);
            }
            const product = await response.json();
            return product;
        } catch (error) {
            console.error(error);
            alert("Failed to fetch product details.");
            return null;
        }
    }

    /**
     * Deletes a product by ID via the backend API.
     * @param {string} productId - The ID of the product to delete.
     */
    async function deleteProduct(productId) {
        if (confirm("Are you sure you want to delete this product?")) {
            try {
                const response = await fetch(`http://localhost:8080/product?id=${productId}`, {
                    method: 'DELETE'
                });
                if (response.ok) {
                    alert("Product deleted successfully!");
                    // Refresh product list
                    await initializeProducts();
                } else {
                    const errorData = await response.text();
                    alert(`Failed to delete product: ${errorData}`);
                }
            } catch (error) {
                console.error(error);
                alert("Error deleting product.");
            }
        }
    }

    // Logout Functionality
    logoutButton.addEventListener("click", () => {
        // Implement logout logic here (e.g., clear session, redirect to login page)
        alert("Logged out successfully!");
        window.location.href = "/frontend/index.html"; // Redirect to main page
    });

    // Initial Render
    initializeProducts();
});
