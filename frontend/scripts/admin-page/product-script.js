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
            const response = await fetch('http://localhost:8080/allproducts');
            if (!response.ok) {
                throw new Error(`Error fetching products: ${response.statusText}`);
            }
            const products = await response.json();
            
            // Store products without image data to reduce storage size
            const productsForCache = products.map(product => ({
                id: product.id,
                name: product.name,
                price: product.price
                // Intentionally omitting itemImage
            }));

            try {
                localStorage.setItem("products", JSON.stringify(productsForCache));
                localStorage.setItem("productsStale", "false");
                isProductListStale = false;
            } catch (error) {
                console.warn("Failed to cache products:", error);
                // Continue even if caching fails
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

            const tdImg = document.createElement("td");
            if (product.id) {
                const img = document.createElement("img");
                img.width = 50;
                img.height = 50;
                img.alt = product.name;
                
                // Use cached image or fetch new one
                getProductImage(product.id).then(imageSrc => {
                    if (imageSrc) {
                        img.src = imageSrc;
                    } else {
                        img.src = ''; // or a default image
                        img.alt = 'No Image';
                    }
                });
                
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
        document.getElementById("product-count").textContent = `Products: ${products.length}`;
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
                    isProductListStale = true;
                    localStorage.setItem("productsStale", "true");
                    clearImageCache(id); // Clear cache for specific product
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
                    isProductListStale = true;
                    localStorage.setItem("productsStale", "true");
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
            if (product.id) {
                const imageSrc = await getProductImage(product.id);
                if (imageSrc) {
                    productPopupImage.src = imageSrc;
                    productPopupImage.alt = product.name;
                    productPopupImage.style.display = 'block';
                } else {
                    productPopupImage.src = "";
                    productPopupImage.style.display = 'none';
                }
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
                    isProductListStale = true;
                    localStorage.setItem("productsStale", "true");
                    clearImageCache(productId); // Clear cache for deleted product
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
    
    async function getProductImage(productId) {
        // Try to get from cache first
        const cachedImage = localStorage.getItem(`${IMAGE_CACHE_PREFIX}${productId}`);
        if (cachedImage && !isProductListStale) {
            return cachedImage;
        }
        
        try {
            const response = await fetch(`http://localhost:8080/product/image?id=${productId}`);
            if (!response.ok) throw new Error('Failed to fetch image');
            
            const blob = await response.blob();
            return new Promise((resolve, reject) => {
                const reader = new FileReader();
                reader.onloadend = () => {
                    // Compress image before caching
                    const img = new Image();
                    img.onload = () => {
                        const canvas = document.createElement('canvas');
                        const ctx = canvas.getContext('2d');
                        
                        // Set maximum dimensions
                        const maxWidth = 200;
                        const maxHeight = 200;
                        
                        let width = img.width;
                        let height = img.height;
                        
                        // Calculate new dimensions
                        if (width > height) {
                            if (width > maxWidth) {
                                height *= maxWidth / width;
                                width = maxWidth;
                            }
                        } else {
                            if (height > maxHeight) {
                                width *= maxHeight / height;
                                height = maxHeight;
                            }
                        }
                        
                        canvas.width = width;
                        canvas.height = height;
                        
                        // Draw and compress
                        ctx.drawImage(img, 0, 0, width, height);
                        const compressedDataUrl = canvas.toDataURL('image/jpeg', 0.6);
                        
                        try {
                            localStorage.setItem(`${IMAGE_CACHE_PREFIX}${productId}`, compressedDataUrl);
                        } catch (error) {
                            if (error.name === 'QuotaExceededError') {
                                // Clear old images if storage is full
                                clearOldImageCache();
                                try {
                                    localStorage.setItem(`${IMAGE_CACHE_PREFIX}${productId}`, compressedDataUrl);
                                } catch (e) {
                                    console.warn('Still unable to cache image after clearing:', e);
                                }
                            }
                        }
                        resolve(compressedDataUrl);
                    };
                    img.src = reader.result;
                };
                reader.onerror = reject;
                reader.readAsDataURL(blob);
            });
        } catch (error) {
            console.error('Error fetching image:', error);
            return null;
        }
    }
    
    function clearOldImageCache() {
        const imagesToKeep = 10; // Adjust this number based on your needs
        const imageKeys = [];
        
        // Collect all image keys
        for (let i = 0; i < localStorage.length; i++) {
            const key = localStorage.key(i);
            if (key.startsWith(IMAGE_CACHE_PREFIX)) {
                imageKeys.push(key);
            }
        }
        
        // Remove oldest images if we have more than imagesToKeep
        if (imageKeys.length > imagesToKeep) {
            imageKeys
                .slice(0, imageKeys.length - imagesToKeep)
                .forEach(key => localStorage.removeItem(key));
        }
    }
    
    // Add function to clear image cache (add after getProductImage function)
    function clearImageCache(productId) {
        if (productId) {
            localStorage.removeItem(`${IMAGE_CACHE_PREFIX}${productId}`);
        } else {
            // Clear all image cache
            for (let i = 0; i < localStorage.length; i++) {
                const key = localStorage.key(i);
                if (key.startsWith(IMAGE_CACHE_PREFIX)) {
                    localStorage.removeItem(key);
                }
            }
        }
    }

    // Initial Render
    initializeProducts();
});
