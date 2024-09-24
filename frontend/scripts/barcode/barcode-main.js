document.addEventListener("DOMContentLoaded", () => {
    // Clear local storage on page load
    localStorage.clear();
  
    const barcodeInput = document.getElementById("barcode-input");
    const barcodeList = document.getElementById("barcode-list");
    const startPopup = document.getElementById("start-popup");
  
    const itemNameElement = document.getElementById("product-info-itemName");
    const itemNumberElement = document.getElementById("product-info-itemNumber");
    const quantityDisplay = document.querySelector(".quantity-display");
    const productImageElement = document.getElementById("item-preview-image");
  
    let currentSelectedItem = null; // Track current selected item
  
    // Initial focus on barcode input
    barcodeInput.focus();
  
    // Maintain focus on input
    function keepFocusOnInput() {
      setTimeout(() => barcodeInput.focus(), 10);
    }
  
    // Keep focus on input after clicks, input, or keydown events
    document.addEventListener("click", (event) => {
      if (!barcodeInput.contains(event.target)) keepFocusOnInput();
    });
  
    barcodeInput.addEventListener("input", keepFocusOnInput);
    document.addEventListener("keydown", keepFocusOnInput);
    document.addEventListener("focusout", keepFocusOnInput);
  
    // Display product info in left section
    function displayItemOnLeft(item) {
      const itemName = item.querySelector(".barcode-list-productName").textContent;
      const itemBarcode = item.querySelector(".barcode-list-productBarcode").textContent;
      const itemQuantity = item.querySelector(".barcode-list-productQuantity").textContent;
  
      const itemImageSrc = `/backend/itemPictures/${itemBarcode}.png`;
  
      itemNameElement.textContent = itemName;
      itemNumberElement.textContent = itemBarcode;
      quantityDisplay.textContent = itemQuantity;
      productImageElement.src = itemImageSrc;
  
      currentSelectedItem = item;
    }
  
    // Close start popup on first scan
    function closeStartPopup() {
      startPopup.style.display = "none";
    }
  
    // Restrict barcode input to numbers
    barcodeInput.addEventListener("input", (event) => {
      barcodeInput.value = barcodeInput.value.replace(/[^0-9]/g, "");
    });
  
    // Add new barcode to the list or update existing one
    function addBarcodeToList(barcode) {
      if (barcodeList.children.length === 0) closeStartPopup();
  
      // Remove all selected classes
      document.querySelectorAll(".barcode-item.selected").forEach((item) => {
        item.classList.remove("selected");
      });
  
      const existingItem = findBarcodeInList(barcode);
  
      if (existingItem) {
        // Increase quantity if item exists
        updateQuantity(existingItem, 1);
        displayItemOnLeft(existingItem);
      } else {
        // Create a new item in the list
        const li = createListItem(barcode);
        barcodeList.appendChild(li);
        displayItemOnLeft(li);
      }
  
      calculateTotals();
      saveBarcodes();
      scrollToBottom();
      keepFocusOnInput();
    }
  
    // Handle plus/minus buttons for quantity
    document.getElementById("quantity-btn-plus").addEventListener("click", () => {
      if (currentSelectedItem) updateQuantity(currentSelectedItem, 1);
    });
  
    document.getElementById("quantity-btn-minus").addEventListener("click", () => {
      if (currentSelectedItem) updateQuantity(currentSelectedItem, -1);
    });
  
    // Barcode input event listener for "Enter" key
    barcodeInput.addEventListener("keyup", (event) => {
      if (event.key === "Enter") {
        event.preventDefault();
        const barcode = barcodeInput.value.trim();
        if (barcode) {
          addBarcodeToList(barcode);
          barcodeInput.value = ""; // Clear input after adding
        }
      }
    });
  
    loadBarcodes(); // Restore barcode list from localStorage
  });
  