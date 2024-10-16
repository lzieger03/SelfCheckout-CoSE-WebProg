document.addEventListener("DOMContentLoaded", () => {
  // Clear local storage on load
  localStorage.clear();

  //-------------------------------- Storage and Initialization --------------------------------
  const startPopup = document.getElementById("start-popup");
  const productErrorPopup = document.getElementById("itemNotFound-popup");
  const barcodeInput = document.getElementById("barcode-input");
  const barcodeList = document.getElementById("barcode-list");
  const subtotalElement = document.getElementById("price-detail-subtotal");
  const taxElement = document.getElementById("price-detail-tax");
  const totalElement = document.getElementById("price-detail-total");
  const itemNameElement = document.getElementById("product-info-itemName");
  const itemNumberElement = document.getElementById("product-info-itemNumber");
  const quantityDisplay = document.querySelector(".quantity-display");
  const productImageElement = document.getElementById("item-preview-image");
  const TAX_RATE = 0.0725;

  let currentSelectedItem = null;
  const barcodeItemMap = {};

  //-------------------------------- Input Focus Management --------------------------------
  const focusBarcodeInput = () => barcodeInput.focus();
  focusBarcodeInput(); // Focus barcode input on load

  document.addEventListener("click", (event) => {
    if (!barcodeInput.contains(event.target)) {
      focusBarcodeInput(); // Refocus input if clicking outside
    }
  });

  //-------------------------------- Helper Functions --------------------------------
  const parsePrice = (priceText) => parseFloat(priceText.replace("$", "").replace(",", "."));
  const formatPrice = (price) => price.toFixed(2).replace(".", ",") + "$";

  //-------------------------------- Product Display --------------------------------
  function displayItemOnLeft(item) {
    itemNameElement.textContent = item.querySelector(".barcode-list-productName").textContent;
    itemNumberElement.textContent = item.querySelector(".barcode-list-productBarcode").textContent;
    quantityDisplay.textContent = item.querySelector(".barcode-list-productQuantity").textContent;
    productImageElement.src = `/backend/itemPictures/${itemNumberElement.textContent}.png`;
    currentSelectedItem = item;
  }

  //-------------------------------- Popup Handling --------------------------------
  startPopup.addEventListener("click", () => (startPopup.style.display = "none")); // Close start popup
  productErrorPopup.addEventListener("click", () => (productErrorPopup.style.display = "none")); // Close error popup
  const openErrorPopup = () => (productErrorPopup.style.display = "flex"); // Show error popup

  //-------------------------------- Barcode Input Handling --------------------------------
  barcodeInput.addEventListener("input", () => {
    barcodeInput.value = barcodeInput.value.replace(/[^0-9]/g, ""); // Allow only numbers
    productErrorPopup.style.display = "none"; // Hide error popup
  });

  //-------------------------------- Barcode API Handling --------------------------------
  async function fetchProductByBarcode(barcode) {
    try {
      const response = await fetch(`http://localhost:8080/product?id=${barcode}`);
      if (!response.ok) throw new Error("Error with the server response");

      const productData = await response.json();
      if (productData.id === "1") throw new Error("Product doesn't exist");

      console.log(`Info: Product with ID: ${productData.id}, name: "${productData.name}", price: ${productData.price}$ added to cart.`);
      return productData;
    } catch (error) {
      console.error("Error at API-Call:", error);
      return null;
    }
  }

  //-------------------------------- List and Price Updates --------------------------------
  function updateTotalPrice(li) {
    const singlePrice = parsePrice(li.querySelector(".barcode-list-productSinglePrice").textContent);
    const quantity = parseInt(li.querySelector(".barcode-list-productQuantity").textContent);
    li.querySelector(".barcode-list-productTotalPrice").textContent = formatPrice(singlePrice * quantity); // Update total price
  }

  function calculateTotals() {
    let subtotal = 0;
    document.querySelectorAll(".barcode-item").forEach((li) => {
      subtotal += parsePrice(li.querySelector(".barcode-list-productTotalPrice").textContent); // Calculate subtotal
    });

    const tax = subtotal * TAX_RATE;
    const total = subtotal + tax;

    subtotalElement.textContent = formatPrice(subtotal);
    taxElement.textContent = formatPrice(tax);
    totalElement.textContent = formatPrice(total); // Update subtotal, tax, and total
  }

  //-------------------------------- List Manipulation --------------------------------
  async function addBarcodeToList(barcode) {
    if (barcodeList.children.length === 0) startPopup.style.display = "none"; // Hide start popup on first item

    document.querySelectorAll(".barcode-item.selected").forEach((item) => item.classList.remove("selected"));
    let existingItem = barcodeItemMap[barcode];

    if (existingItem) {
      const quantityElement = existingItem.querySelector(".barcode-list-productQuantity");
      quantityElement.textContent = parseInt(quantityElement.textContent) + 1; // Increment quantity
      updateTotalPrice(existingItem);
      displayItemOnLeft(existingItem);
    } else {
      const productData = await fetchProductByBarcode(barcode);
      if (!productData) return openErrorPopup();

      const li = document.createElement("li");
      li.className = "barcode-item";
      li.innerHTML = `
        <img src="/backend/itemPictures/${barcode}.png" alt="product-name" class="barcode-list-productImage">
        <p class="barcode-list-productName">${productData.name}</p>
        <p class="barcode-list-productBarcode">${productData.id}</p>
        <p class="barcode-list-productSinglePrice">${productData.price}$</p>
        <p class="barcode-list-productQuantity">1</p>
        <p class="barcode-list-productTotalPrice">${productData.price}$</p>
        <button class="delete-btn">Delete</button>
      `;

      li.querySelector(".delete-btn").addEventListener("click", () => removeBarcodeItem(li, barcode)); // Delete button handler

      li.addEventListener("click", () => {
        document.querySelectorAll(".barcode-item").forEach((item) => item.classList.remove("selected"));
        li.classList.add("selected"); // Highlight selected item
        displayItemOnLeft(li);
      });

      barcodeList.appendChild(li);
      barcodeItemMap[barcode] = li;
      displayItemOnLeft(li);
    }

    calculateTotals();
    saveBarcodes();
    barcodeList.scrollTop = barcodeList.scrollHeight; // Auto-scroll to bottom
    focusBarcodeInput();
  }

  function removeBarcodeItem(li, barcode) {
    li.remove();
    delete barcodeItemMap[barcode];
    calculateTotals();
    saveBarcodes();
    if (currentSelectedItem === li) {
      currentSelectedItem = null;
      itemNameElement.textContent = "Start scanning";
      itemNumberElement.textContent = "your items";
      quantityDisplay.textContent = "0"; // Reset item info
    }
    console.log(`Info: Removed product from the cart.`);
  }

  //-------------------------------- Quantity Adjustment --------------------------------
  const adjustQuantity = (amount) => {
    if (currentSelectedItem) {
      const quantityElement = currentSelectedItem.querySelector(".barcode-list-productQuantity");
      const newQuantity = parseInt(quantityElement.textContent) + amount;

      if (newQuantity > 0) {
        quantityElement.textContent = newQuantity;
        quantityDisplay.textContent = newQuantity;
        updateTotalPrice(currentSelectedItem);
      } else {
        const barcode = currentSelectedItem.querySelector(".barcode-list-productBarcode").textContent;
        removeBarcodeItem(currentSelectedItem, barcode);
      }

      calculateTotals();
      saveBarcodes();
    }
  };

  document.getElementById("quantity-btn-plus").addEventListener("click", () => adjustQuantity(1)); // Increment quantity
  document.getElementById("quantity-btn-minus").addEventListener("click", () => adjustQuantity(-1)); // Decrement quantity

  //-------------------------------- Barcode Input Event --------------------------------
  barcodeInput.addEventListener("keyup", (event) => {
    if (event.key === "Enter") {
      const barcode = barcodeInput.value.trim();
      if (barcode) {
        addBarcodeToList(barcode);
        barcodeInput.value = ""; // Clear input
      }
    }
  });

  //-------------------------------- Storage Handling --------------------------------
  function saveBarcodes() {
    const barcodes = Array.from(document.querySelectorAll(".barcode-item")).map((item) => ({
      barcode: item.querySelector(".barcode-list-productBarcode").textContent,
      name: item.querySelector(".barcode-list-productName").textContent,
      price: item.querySelector(".barcode-list-productSinglePrice").textContent,
      quantity: item.querySelector(".barcode-list-productQuantity").textContent,
    }));
    localStorage.setItem("barcodes", JSON.stringify(barcodes)); // Save to local storage
  }

  function loadBarcodes() {
    const storedBarcodes = localStorage.getItem("barcodes");
    if (storedBarcodes) {
      JSON.parse(storedBarcodes).forEach((item) => {
        for (let i = 0; i < item.quantity; i++) {
          addBarcodeToList(item.barcode); // Load saved items
        }
      });
    }
  }

  loadBarcodes();
});
