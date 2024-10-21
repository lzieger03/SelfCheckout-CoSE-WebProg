document.addEventListener("DOMContentLoaded", () => {
    const logoutButton = document.getElementById("logout-button");
    const addProductBtn = document.getElementById("add-product-btn");
    const productPopup = document.getElementById("product-popup");
    const productPopupClose = document.getElementById("product-popup-close");
    const productForm = document.getElementById("product-form");
    const productTableBody = document.querySelector("#product-table tbody");
    const productPopupTitle = document.getElementById("product-popup-title");

    const productPopupImage = document.getElementById("product-popup-image");
    const productPopupImageUpload = document.getElementById("product-popup-image-upload");

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
            if (product.id) { // Ensure the product has an ID
                const img = document.createElement("img");
                img.src = `http://localhost:8080/product/image?id=${product.id}`;
                img.alt = product.name;
                img.width = 50;
                img.height = 50;
                tdImg.appendChild(img);
            } else {
                tdImg.textContent = "No Image";
            }
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
            editBtn.addEventListener("click", () => {
                isEditMode = true;
                openEditProductPopup(product.id);
            });
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
        productPopupTitle.textContent = "Add New Product";
        productForm.reset();
        productPopup.style.display = "flex";

        //create a product id thats 13 digits long only numbers, unique and set it to the input field
        // the id is not allowed to start with 0
        // the id is not allowed to be 0000000000000
        // must be 13 digits long
        let productId = Math.floor(Math.random() * 1e13).toString().padStart(13, '1');
        // if the id starts with 0, remove the 0 and add a 1 to the beginning
        if (productId.startsWith('0')) {
            productId = productId.slice(1);
            productId = productId.padEnd(13, Math.floor(Math.random() * 10).toString());
        }   
        document.getElementById("product-id").value = productId;

        //disable the id input field and grey it out
        document.getElementById("product-id").disabled = true;
        document.getElementById("product-id").style.backgroundColor = "#ccc";

        // Clear the file input
        productPopupImageUpload.value = "";
        productPopupImage.src = "";
        productPopupImage.alt = "";

        // Add event listener for image upload
        productPopupImageUpload.addEventListener("change", (event) => {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = (e) => {
                    productPopupImage.src = e.target.result;
                    productPopupImage.style.display = 'block';
                };
                reader.readAsDataURL(file);
            } else {
                productPopupImage.src = "";
                productPopupImage.style.display = 'none';
            }
        });
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
        const imageFile = document.getElementById("product-popup-image-upload").files[0];

        if (!id || !name || isNaN(price)) {
            alert("Please fill in all required fields.");
            return;
        }

        const formData = new FormData();
        const productData = { id, name, price };
        formData.append("product", new Blob([JSON.stringify(productData)], { type: "application/json" }));

        // Append the image file only if a new image is selected
        if (imageFile) {
            formData.append("image", imageFile);
        }

        if (isEditMode) {
            // Update Product
            try {
                const response = await fetch(`http://localhost:8080/updateproduct`, {
                    method: 'PUT',
                    body: formData
                });
                if (response.ok) {
                    alert("Product updated successfully!");
                    productPopup.style.display = "none";
                    initializeProducts();
                } else {
                    const errorData = await response.json();
                    alert(`Failed to update product: ${errorData.error}`);
                }
            } catch (error) {
                console.error(error);
                alert("Error updating product.");
            }
        } else {
            // Add New Product
            try {
                const response = await fetch(`http://localhost:8080/addproduct`, {
                    method: 'POST',
                    body: formData
                });
                if (response.ok) {
                    alert("Product added successfully!");
                    productPopup.style.display = "none";
                    initializeProducts();
                } else {
                    const errorData = await response.json();
                    alert(`Failed to add product: ${errorData.error}`);
                }
            } catch (error) {
                console.error(error);
                alert("Error adding product.");
            }
        }
        // Refresh product list and close the popup
        productPopup.style.display = "none";
        await initializeProducts();
    });
    

    /**
     * Opens the edit product popup with pre-filled data.
     * @param {string} productId - The ID of the product to edit.
     */
    async function openEditProductPopup(productId) {
        const product = await getProductById(productId);
        if (product) {
            isEditMode = true;
            editProductId = product.id;
            productPopupTitle.textContent = "Edit Product";

            // Populate form fields with existing product data
            document.getElementById("product-id").value = product.id;
            document.getElementById("product-name").value = product.name;
            document.getElementById("product-price").value = product.price;

            // Disable the ID input field and grey it out
            const productIdInput = document.getElementById("product-id");
            productIdInput.disabled = true;
            productIdInput.style.backgroundColor = "#ccc";

            // Display the existing image if available
            if (product.id) { // Ensures the product has an ID
                productPopupImage.src = `http://localhost:8080/product/image?id=${product.id}`;
                productPopupImage.alt = product.name;
                productPopupImage.width = 50;
                productPopupImage.height = 50;
            } else {
                productPopupImage.src = "";
            }

            // Clear the file input to allow optional image upload
            productPopupImageUpload.value = "";

            // Add a new event listener for image uploads
            productPopupImageUpload.addEventListener("change", (event) => {
                const file = event.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = (e) => {
                        productPopupImage.src = e.target.result;
                        productPopupImage.style.display = 'block'; // Ensure the image is visible
                    };
                    reader.readAsDataURL(file);
                } else {
                    productPopupImage.src = "";
                    productPopupImage.style.display = 'none';
                }
            });

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
        try {
            if (confirm("Are you sure you want to delete this product?")) {
                try {
                const response = await fetch(`http://localhost:8080/deleteproduct?id=${productId}`, {
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
        } catch (error) {
            console.error(error);
            alert("Error deleting product.");
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
