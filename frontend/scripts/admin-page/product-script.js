document.addEventListener("DOMContentLoaded", () => {
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
    let isProductListStale = localStorage.getItem("productsStale") !== "false";

    const IMAGE_CACHE_PREFIX = 'product_image_';

    /**
     * Fetches all products from the backend API.
     * @returns {Promise<Array>} A promise that resolves to an array of products.
     */
    async function fetchAllProducts() {
        // If products are cached and not stale, return from localStorage
        if (!isProductListStale && localStorage.getItem("products") && localStorage.getItem("products") !== "[]") {
            try {
                return JSON.parse(localStorage.getItem("products"));
            } catch (error) {
                console.warn("Error parsing cached products:", error);
                isProductListStale = true; // Mark as stale if there's an error
                localStorage.setItem("productsStale", "true");
            }
        }

        try {
            const response = await fetch('http://localhost:3000/products');
            if (!response.ok) {
                throw new Error(`Error fetching products: ${response.statusText}`);
            }
            const products = await response.json();
            
            // Store products without image data to reduce storage size
            const productsForCache = products.map(product => ({
                id: product.id,
                name: product.name,
                price: product.price
                // Intentionally omitting image
            }));

            try {
                localStorage.setItem("products", JSON.stringify(productsForCache));
                localStorage.setItem("productsStale", "false");
                isProductListStale = false;
            } catch (error) {
                console.warn("Failed to cache products:", error);
            }

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

            const tdId = document.createElement("td");
            tdId.textContent = product.id;
            tr.appendChild(tdId);

            const tdName = document.createElement("td");
            tdName.textContent = product.name;
            tr.appendChild(tdName);

            const tdPrice = document.createElement("td");
            tdPrice.textContent = product.price.toFixed(2);
            tr.appendChild(tdPrice);

            const tdImage = document.createElement("td");
            const img = document.createElement("img");
            img.src = `http://localhost:3000${product.image}`;
            img.alt = product.name;
            img.width = 50;
            tdImage.appendChild(img);
            tr.appendChild(tdImage);

            const tdActions = document.createElement("td");

            const editButton = document.createElement("button");
            editButton.textContent = "Edit";
            editButton.classList.add("edit-btn");
            editButton.addEventListener("click", () => openEditProductPopup(product.id));
            tdActions.appendChild(editButton);

            const deleteButton = document.createElement("button");
            deleteButton.textContent = "Delete";
            deleteButton.classList.add("delete-btn");
            deleteButton.addEventListener("click", () => deleteProduct(product.id));
            tdActions.appendChild(deleteButton);

            tr.appendChild(tdActions);

            productTableBody.appendChild(tr);
        });
    }

    /**
     * Initializes the product management by fetching and rendering all products.
     */
    async function initializeProducts() {
        const products = await fetchAllProducts();
        renderProducts(products);
    }

    /**
     * Opens the popup for adding or editing a product.
     * @param {string} [id] - The product ID to edit. If undefined, opens in add mode.
     */
    function openProductPopup(id = null) {
        if (id) {
            isEditMode = true;
            editProductId = id;
            productPopupTitle.textContent = "Edit Product";
            // Pre-fill the form with existing product data
            const products = JSON.parse(localStorage.getItem("products"));
            const product = products.find(p => p.id === id);
            if (product) {
                document.getElementById("product-id").value = product.id;
                document.getElementById("product-name").value = product.name;
                document.getElementById("product-price").value = product.price;
                productPopupImage.src = `http://localhost:3000${product.image}`;
                document.getElementById("product-id").disabled = true; // Prevent changing ID
            }
        } else {
            isEditMode = false;
            editProductId = null;
            productPopupTitle.textContent = "Add Product";
            productForm.reset();
            productPopupImage.src = "";
            document.getElementById("product-id").disabled = false;
        }
        productPopup.style.display = "flex";
    }

    /**
     * Closes the product popup.
     */
    function closeProductPopup() {
        productPopup.style.display = "none";
    }

    /**
     * Handles the submission of the product form for adding or editing.
     * @param {Event} event - The form submission event.
     */
    productForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        const id = document.getElementById("product-id").value.trim();
        const name = document.getElementById("product-name").value.trim();
        const price = parseFloat(document.getElementById("product-price").value);
        const imageFile = productPopupImageUpload.files[0];

        if (!id || !name || isNaN(price) || !imageFile) {
            alert("Please provide valid product details and upload an image.");
            return;
        }

        // Upload image to backend
        try {
            const imageUrl = `http://localhost:3000/images/${encodeURIComponent(imageFile.name)}`;
            // For simplicity, assume the image is already placed in the /images directory.
            // In a real backend, you'd handle file uploads differently.

            if (isEditMode) {
                // Edit existing product
                const response = await fetch(`http://localhost:3000/products/${encodeURIComponent(id)}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ id, name, price, image: `/images/${encodeURIComponent(imageFile.name)}` })
                });

                if (!response.ok) {
                    const errorMsg = await response.text();
                    throw new Error(errorMsg);
                }

                isProductListStale = true;
                localStorage.setItem("productsStale", "true");
                alert("Product updated successfully.");
                closeProductPopup();
                initializeProducts(); // Refresh the product list
            } else {
                // Add new product
                const response = await fetch('http://localhost:3000/products', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ id, name, price, image: `/images/${encodeURIComponent(imageFile.name)}` })
                });

                if (!response.ok) {
                    const errorMsg = await response.text();
                    throw new Error(errorMsg);
                }

                isProductListStale = true;
                localStorage.setItem("productsStale", "true");
                alert("Product added successfully.");
                closeProductPopup();
                initializeProducts(); // Refresh the product list
            }
        } catch (error) {
            console.error("Error saving product:", error);
            alert(`Error saving product: ${error.message}`);
        }
    });

    /**
     * Opens the edit popup with the product details pre-filled.
     * @param {string} id - The product ID to edit.
     */
    function openEditProductPopup(id) {
        openProductPopup(id);
    }

    /**
     * Deletes a product by its ID.
     * @param {string} id - The product ID to delete.
     */
    async function deleteProduct(id) {
        if (!confirm(`Are you sure you want to delete the product "${id}"?`)) return;

        try {
            const response = await fetch(`http://localhost:3000/products/${encodeURIComponent(id)}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                const errorMsg = await response.text();
                throw new Error(errorMsg);
            }

            isProductListStale = true;
            localStorage.setItem("productsStale", "true");
            alert("Product deleted successfully.");
            initializeProducts(); // Refresh the product list
        } catch (error) {
            console.error("Error deleting product:", error);
            alert(`Error deleting product: ${error.message}`);
        }
    }

    /**
     * Handles image upload and preview.
     */
    productPopupImageUpload.addEventListener("change", (event) => {
        const file = event.target.files[0];
        if (file) {
            productPopupImage.src = URL.createObjectURL(file);
        } else {
            productPopupImage.src = "";
        }
    });

    // Event Listeners
    addProductBtn.addEventListener("click", () => openProductPopup());
    productPopupClose.addEventListener("click", closeProductPopup);

    // Initial Render
    initializeProducts();
});
