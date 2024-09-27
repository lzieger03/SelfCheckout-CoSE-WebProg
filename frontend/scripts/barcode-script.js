document.addEventListener("DOMContentLoaded", () => {
  //-------------------------------- Storage and Initialization --------------------------------
  // Clear local storage on load
  localStorage.clear();

  const barcodeInput = document.getElementById("barcode-input");
  const barcodeList = document.getElementById("barcode-list");
  const startPopup = document.getElementById("start-popup");
  const subtotalElement = document.getElementById("price-detail-subtotal");
  const taxElement = document.getElementById("price-detail-tax");
  const totalElement = document.getElementById("price-detail-total");

  const TAX_RATE = 0.0725; // Example tax rate

  const itemNameElement = document.getElementById("product-info-itemName");
  const itemNumberElement = document.getElementById("product-info-itemNumber");
  const quantityDisplay = document.querySelector(".quantity-display");
  const productImageElement = document.getElementById("item-preview-image");
  let currentSelectedItem = null; // Store current list item

  // Focus input when page loads
  barcodeInput.focus();

  //-------------------------------- Input Focus Management --------------------------------
  // Keep focus on input continuously
  function keepFocusOnInput() {
    setTimeout(() => {
      barcodeInput.focus();
    }, 0); // Delay for Safari workaround
  }

  // Refocus input on interactions
  document.addEventListener("click", (event) => {
    if (!barcodeInput.contains(event.target)) {
      keepFocusOnInput();
    }
  });

  // Prevent focus loss on key press
  barcodeInput.addEventListener("keydown", () => {
    keepFocusOnInput();
  });

  //-------------------------------- Product Display --------------------------------
  // Display item details on the left
  function displayItemOnLeft(item) {
    const itemName = item.querySelector(
      ".barcode-list-productName"
    ).textContent;
    const itemBarcode = item.querySelector(
      ".barcode-list-productBarcode"
    ).textContent;
    const itemQuantity = item.querySelector(
      ".barcode-list-productQuantity"
    ).textContent;
    const itemImageSrc = `/backend/itemPictures/${itemBarcode}.png`; // Image based on barcode

    itemNameElement.textContent = itemName;
    itemNumberElement.textContent = itemBarcode;
    quantityDisplay.textContent = itemQuantity;
    productImageElement.src = itemImageSrc; // Update image

    currentSelectedItem = item;
  }

  //-------------------------------- Popup Handling --------------------------------
  // Close start popup
  function closeStartPopup() {
    startPopup.style.display = "none";
  }

  startPopup.addEventListener("click", () => {
    closeStartPopup();
  });

  //-------------------------------- Barcode Input Handling --------------------------------
  // Refocus input on edit
  barcodeInput.addEventListener("input", () => {
    keepFocusOnInput();
  });

  // Filter non-numeric input
  barcodeInput.addEventListener("input", (event) => {
    barcodeInput.value = barcodeInput.value.replace(/[^0-9]/g, "");
  });

  //-------------------------------- List and Price Updates --------------------------------
  // Check if barcode exists in list
  function findBarcodeInList(barcode) {
    return [...document.querySelectorAll(".barcode-item")].find(
      (li) =>
        li.querySelector(".barcode-list-productBarcode").textContent === barcode
    );
  }

  // Update total price (qty * unit price)
  function updateTotalPrice(li) {
    const singlePrice = parseFloat(
      li
        .querySelector(".barcode-list-productSinglePrice")
        .textContent.replace("$", "")
        .replace(",", ".")
    );
    const quantity = parseInt(
      li.querySelector(".barcode-list-productQuantity").textContent
    );
    const totalPrice = (singlePrice * quantity).toFixed(2).replace(".", ",");
    li.querySelector(
      ".barcode-list-productTotalPrice"
    ).textContent = `${totalPrice}$`;
  }

  // Calculate subtotal, tax, and total
  function calculateTotals() {
    let subtotal = 0;
    document.querySelectorAll(".barcode-item").forEach((li) => {
      const totalPrice = parseFloat(
        li
          .querySelector(".barcode-list-productTotalPrice")
          .textContent.replace("$", "")
          .replace(",", ".")
      );
      subtotal += totalPrice;
    });

    const tax = (subtotal * TAX_RATE).toFixed(2).replace(".", ",");
    const total = (subtotal * (1 + TAX_RATE)).toFixed(2).replace(".", ",");

    subtotalElement.textContent = `${subtotal.toFixed(2).replace(".", ",")}$`;
    taxElement.textContent = `${tax}$`;
    totalElement.textContent = `${total}$`;
  }

  //-------------------------------- List Manipulation --------------------------------
  // Auto-scroll to bottom
  function scrollToBottom() {
    const barcodeListContainer = document.getElementById(
      "barcode-list-container"
    );
    barcodeListContainer.scrollTop = barcodeListContainer.scrollHeight;
  }

  // Add or update scanned barcode in list
  function addBarcodeToList(barcode) {
    if (barcodeList.children.length === 0) {
      closeStartPopup();
    }

    document.querySelectorAll(".barcode-item.selected").forEach((item) => {
      item.classList.remove("selected");
    });

    const existingItem = findBarcodeInList(barcode);

    if (existingItem) {
      // Increase quantity if item exists
      const quantityElement = existingItem.querySelector(
        ".barcode-list-productQuantity"
      );
      quantityElement.textContent = parseInt(quantityElement.textContent) + 1;
      updateTotalPrice(existingItem);
      displayItemOnLeft(existingItem);
    } else {
      // Add new barcode item
      const li = document.createElement("li");
      li.className = "barcode-item";
      li.innerHTML = `
        <img src="/backend/itemPictures/${barcode}.png" alt="product-name" class="barcode-list-productImage">
        <p class="barcode-list-productName">Placeholder Apple</p>
        <p class="barcode-list-productBarcode">${barcode}</p>
        <p class="barcode-list-productSinglePrice">1,99$</p>
        <p class="barcode-list-productQuantity">1</p>
        <p class="barcode-list-productTotalPrice">1,99$</p>
        <button class="delete-btn">Delete</button>
      `;

      li.querySelector(".delete-btn").addEventListener("click", () => {
        li.remove();
      });

      li.addEventListener("click", function () {
        if (li.classList.contains("selected")) {
          li.classList.remove("selected");
          const lastItem = barcodeList.lastElementChild;
          if (lastItem) {
            displayItemOnLeft(lastItem);
          }
        } else {
          document
            .querySelectorAll(".barcode-item")
            .forEach((item) => item.classList.remove("selected"));
          li.classList.add("selected");
          displayItemOnLeft(li);
        }
      });

      barcodeList.appendChild(li);
      displayItemOnLeft(li);
    }

    calculateTotals();
    saveBarcodes();
    scrollToBottom();
    keepFocusOnInput();
  }

  //-------------------------------- Quantity Adjustment --------------------------------
  // Plus and minus buttons for left product
  document.getElementById("quantity-btn-plus").addEventListener("click", () => {
    if (currentSelectedItem) {
      const quantityElement = currentSelectedItem.querySelector(
        ".barcode-list-productQuantity"
      );
      quantityElement.textContent = parseInt(quantityElement.textContent) + 1;
      quantityDisplay.textContent = quantityElement.textContent; // refresh 
      updateTotalPrice(currentSelectedItem);
      calculateTotals();
      saveBarcodes();
    }
  });

  document.getElementById("quantity-btn-minus").addEventListener("click", () => {
    if (!currentSelectedItem) return;
  
    const quantityElement = currentSelectedItem.querySelector(".barcode-list-productQuantity");
    const newQuantity = parseInt(quantityElement.textContent) - 1;
  
    if (newQuantity > 0) {
      quantityElement.textContent = newQuantity;
      quantityDisplay.textContent = newQuantity;
      updateTotalPrice(currentSelectedItem);
    } else {
      currentSelectedItem.remove();
      currentSelectedItem = null;
      itemNameElement.textContent = "Item name";
      itemNumberElement.textContent = "Item number";
      quantityDisplay.textContent = "1";
    }
  
    calculateTotals();
    saveBarcodes();
  });
  

  //-------------------------------- Barcode Input Event --------------------------------
  barcodeInput.addEventListener("keyup", (event) => {
    if (event.key === "Enter") {
      const barcode = barcodeInput.value.trim();
      if (barcode !== "") {
        addBarcodeToList(barcode);
        barcodeInput.value = "";
      }
    }
  });

  //-------------------------------- Storage Handling --------------------------------
  // Save barcode list to local storage
  function saveBarcodes() {
    const barcodes = [];
    document.querySelectorAll(".barcode-item").forEach((item) => {
      const barcode = item.querySelector(
        ".barcode-list-productBarcode"
      ).textContent;
      const quantity = item.querySelector(
        ".barcode-list-productQuantity"
      ).textContent;
      barcodes.push({ barcode, quantity });
    });
    localStorage.setItem("barcodes", JSON.stringify(barcodes));
  }

  // Load barcode list from local storage
  function loadBarcodes() {
    const storedBarcodes = localStorage.getItem("barcodes");
    if (storedBarcodes) {
      JSON.parse(storedBarcodes).forEach((item) => {
        for (let i = 0; i < item.quantity; i++) {
          addBarcodeToList(item.barcode);
        }
      });
    }
  }

  // Restore barcode list on page load
  loadBarcodes();
});
