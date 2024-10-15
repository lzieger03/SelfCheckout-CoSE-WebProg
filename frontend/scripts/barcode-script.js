document.addEventListener("DOMContentLoaded", () => {
  // Clear local storage on load
  localStorage.clear();



  //-------------------------------- Storage and Initialization --------------------------------
  // Popup-Items
  const startPopup = document.getElementById("start-popup");
  const productErrorPopup = document.getElementById("itemNotFound-popup");

  // Barcode-list/input -Items
  const barcodeInput = document.getElementById("barcode-input");
  const barcodeList = document.getElementById("barcode-list");

  // Price/Total -Items
  const subtotalElement = document.getElementById("price-detail-subtotal");
  const taxElement = document.getElementById("price-detail-tax");
  const totalElement = document.getElementById("price-detail-total");

  // Example tax rate
  const TAX_RATE = 0.0725;

  // Left Section - Product Preview
  const itemNameElement = document.getElementById("product-info-itemName");
  const itemNumberElement = document.getElementById("product-info-itemNumber");
  const quantityDisplay = document.querySelector(".quantity-display");
  const productImageElement = document.getElementById("item-preview-image");
  
  // Store current list item
  let currentSelectedItem = null; 
  
  // Map barcodes to list items
  const barcodeItemMap = {}; 



  //-------------------------------- Input Focus Management --------------------------------
  // Focus input when page loads
  barcodeInput.focus();

  // Keep focus on input continuously
  function keepFocusOnInput() {
    setTimeout(() => {
      barcodeInput.focus();
    }, 0); // Delay for Safari (workaround)
  }

  // Refocus input on interactions
  document.addEventListener("click", (event) => {
    if (!barcodeInput.contains(event.target)) {
      keepFocusOnInput();
    }
  });



  //-------------------------------- Helper Functions --------------------------------
  function parsePrice(priceText) {
    return parseFloat(priceText.replace("$", "").replace(",", "."));
  }

  function formatPrice(price) {
    return price.toFixed(2).replace(".", ",") + "$";
  }



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
  // --- Start Popup ---
  // Close start popup
  function closeStartPopup() {
    startPopup.style.display = "none";
  }

  startPopup.addEventListener("click", closeStartPopup);

  // --- Error Popup ---
  // Open & close Errop Popup
  function openErrorPopup() {
    productErrorPopup.style.display = "flex";
  }

  function closeErrorPopup() {
    productErrorPopup.style.display = "none";
  }

  productErrorPopup.addEventListener("click", closeErrorPopup);



  //-------------------------------- Barcode Input Handling --------------------------------
  // Filter non-numeric input and refocus input
  barcodeInput.addEventListener("input", () => {
    barcodeInput.value = barcodeInput.value.replace(/[^0-9]/g, "");
    keepFocusOnInput();
    closeErrorPopup();
  });



  //-------------------------------- Barcode API Handling --------------------------------
  // Function for API-call
  async function fetchProductByBarcode(barcode) {
    try {
      const response = await fetch(
        `http://localhost:8080/product?id=${barcode}`
      );

      if (!response.ok) {
        throw new Error("Error with the server response");
      }
      const productData = await response.json();

      if (productData.id === "1") {
        throw new Error("Product doesn't exist");
      }

      console.log(`Info: Product with the ID: ${productData.id}, the name: "${productData.name}" and the price: ${productData.price}$ was added to the cart.`)
      return productData; // return productData on success
    } catch (error) {
      console.error("Error at API-Call:", error);
      return null; // Return null on error
    }
  }
  


  //-------------------------------- List and Price Updates --------------------------------
  // Update total price (qty * unit price)
  function updateTotalPrice(li) {
    const singlePrice = parsePrice(
      li.querySelector(".barcode-list-productSinglePrice").textContent
    );
    const quantity = parseInt(
      li.querySelector(".barcode-list-productQuantity").textContent
    );
    const totalPrice = singlePrice * quantity;
    li.querySelector(".barcode-list-productTotalPrice").textContent =
      formatPrice(totalPrice);
  }

  // Calculate subtotal, tax, and total
  function calculateTotals() {
    let subtotal = 0;
    document.querySelectorAll(".barcode-item").forEach((li) => {
      const totalPrice = parsePrice(
        li.querySelector(".barcode-list-productTotalPrice").textContent
      );
      subtotal += totalPrice;
    });

    const tax = subtotal * TAX_RATE;
    const total = subtotal + tax;

    subtotalElement.textContent = formatPrice(subtotal);
    taxElement.textContent = formatPrice(tax);
    totalElement.textContent = formatPrice(total);
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
  async function addBarcodeToList(barcode) {
    if (barcodeList.children.length === 0) {
      closeStartPopup();
    }

    document.querySelectorAll(".barcode-item.selected").forEach((item) => {
      item.classList.remove("selected");
    });

    let existingItem = barcodeItemMap[barcode];

    if (existingItem) {
      // increase quantity if item already exists in the list
      const quantityElement = existingItem.querySelector(
        ".barcode-list-productQuantity"
      );
      quantityElement.textContent = parseInt(quantityElement.textContent) + 1;
      updateTotalPrice(existingItem);
      displayItemOnLeft(existingItem);
    } else {
      // API-Call for Barcode data
      const productData = await fetchProductByBarcode(barcode);

      if (!productData) {
        openErrorPopup();
        return;
      }

      // create new barcode-element
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

      li.querySelector(".delete-btn").addEventListener("click", () => {
        li.remove();
        delete barcodeItemMap[barcode];
        calculateTotals();
        saveBarcodes();
        if (currentSelectedItem === li) {
          currentSelectedItem = null;
          itemNameElement.textContent = "Start scanning";
          itemNumberElement.textContent = "your items";
          quantityDisplay.textContent = "0";
        }
        console.log(`Info: Removed product from the cart.`);
      });

      li.addEventListener("click", function () {
        if (li.classList.contains("selected")) {
          li.classList.remove("selected");
          currentSelectedItem = null;
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
      barcodeItemMap[barcode] = li; // put barcode into barcode-map
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

  document
    .getElementById("quantity-btn-minus")
    .addEventListener("click", () => {
      if (!currentSelectedItem) return;

      const quantityElement = currentSelectedItem.querySelector(
        ".barcode-list-productQuantity"
      );
      const newQuantity = parseInt(quantityElement.textContent) - 1;

      if (newQuantity > 0) {
        quantityElement.textContent = newQuantity;
        quantityDisplay.textContent = newQuantity;
        updateTotalPrice(currentSelectedItem);
      } else {
        const barcode = currentSelectedItem.querySelector(
          ".barcode-list-productBarcode"
        ).textContent;
        currentSelectedItem.remove();
        delete barcodeItemMap[barcode];
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
