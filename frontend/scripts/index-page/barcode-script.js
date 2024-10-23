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

  // Coupon initialization  
  const couponButton = document.getElementById("coupon-btn");
  const couponPopup = document.getElementById("coupon-popup");
  const couponApplyButton = document.getElementById("coupon-apply-btn");
  const couponCancelButton = document.getElementById("coupon-cancel-btn");  
  const couponInput = document.getElementById("coupon-input");
  const couponCloseButton = document.getElementById("coupon-popup-close-btn");
  const couponAppliedContainer = document.getElementById("coupon-applied-container");
  const couponAppliedRemoveBtn = document.getElementById("coupon-applied-remove-btn");
  const couponAppliedText = document.getElementById("coupon-applied-text");
  const couponAppliedDiscountValue = document.getElementById("coupon-applied-discount-value");

  // Admin login initialization
  const adminLoginPopup = document.getElementById("admin-login-popup");

  //-------------------------------- Input Focus Management --------------------------------
  const focusBarcodeInput = () => barcodeInput.focus();
  focusBarcodeInput(); // Focus barcode input on load

  document.addEventListener("click", (event) => {
    if (!barcodeInput.contains(event.target)) {
      if (couponPopup.style.display === "flex") {
        couponInput.focus(); // Focus coupon input if coupon popup is open
      } else if (adminLoginPopup.style.display === "flex") {
        //focus nothing
      } else {
        focusBarcodeInput(); // Refocus input if clicking outside
      }
    }
  });

  //-------------------------------- Helper Functions --------------------------------
  const parsePrice = (priceText) =>
    parseFloat(priceText.replace("$", "").replace(",", "."));
  const formatPrice = (price) => price.toFixed(2).replace(".", ",") + "$";

  //-------------------------------- Product Display --------------------------------
  function displayItemOnLeft(item) {
    itemNameElement.textContent = item.querySelector(
      ".barcode-list-productName"
    ).textContent;
    itemNumberElement.textContent = item.querySelector(
      ".barcode-list-productBarcode"
    ).textContent;
    quantityDisplay.textContent = item.querySelector(
      ".barcode-list-productQuantity"
    ).textContent;
    productImageElement.src = `http://localhost:8080/product/image?id=${itemNumberElement.textContent}`;
    currentSelectedItem = item;
  }

  //-------------------------------- Popup Handling --------------------------------
  startPopup.addEventListener(
    "click",
    () => (startPopup.style.display = "none")
  ); // Close start popup
  productErrorPopup.addEventListener(
    "click",
    () => (productErrorPopup.style.display = "none")
  ); // Close error popup
  const openErrorPopup = () => (productErrorPopup.style.display = "flex"); // Show error popup

  //-------------------------------- Close Popups --------------------------------
  couponCloseButton.addEventListener("click", () => {
    couponPopup.style.display = "none";
  });

  //-------------------------------- Barcode Input Handling --------------------------------
  barcodeInput.addEventListener("input", () => {
    barcodeInput.value = barcodeInput.value.replace(/[^0-9]/g, ""); // Allow only numbers
    productErrorPopup.style.display = "none"; // Hide error popup
  });

  //-------------------------------- Barcode API Handling --------------------------------
  async function fetchProductByBarcode(barcode) {
    try {
      const response = await fetch(
        `http://localhost:8080/product?id=${barcode}`
      );
      if (!response.ok) throw new Error("Error with the server response");

      const productData = await response.json();
      if (productData.id === "1") throw new Error("Product doesn't exist");

      console.log(
        `Info: Product with ID: ${productData.id}, name: "${productData.name}", price: ${productData.price}$ added to cart.`
      );
      return productData;
    } catch (error) {
      console.error("Error at API-Call:", error);
      return null;
    }
  }

  //-------------------------------- List and Price Updates --------------------------------
  const discountValue = localStorage.getItem("discountValue") || 0.0;
  function updateTotalPrice(li) {
    const singlePrice = parsePrice(
      li.querySelector(".barcode-list-productSinglePrice").textContent
    );
    const quantity = parseInt(
      li.querySelector(".barcode-list-productQuantity").textContent
    );
    if (discountValue > 0) {
        li.querySelector(".barcode-list-productTotalPrice").textContent =
        formatPrice(singlePrice * quantity * (1 - (discountValue / 100))); // Update total price
   console.log(
        `Info: Total price updated to ${
          singlePrice * quantity * (1 - (discountValue / 100))
        }`
      );
    } else {
      li.querySelector(".barcode-list-productTotalPrice").textContent =
        formatPrice(singlePrice * quantity); // Update total price
    }
  }

  function calculateTotals() {
    const discountValue = parseFloat(localStorage.getItem("discountValue"));
    let subtotal = 0;
    document.querySelectorAll(".barcode-item").forEach((li) => {
      if (discountValue > 0) {
        subtotal +=
          parsePrice(
          li.querySelector(".barcode-list-productTotalPrice").textContent
        ) *
        (1 - (discountValue / 100)); // Calculate subtotal
      } else {
        subtotal +=
          parsePrice(
          li.querySelector(".barcode-list-productTotalPrice").textContent
        ); // Calculate subtotal
      }
      console.log(`Info: Subtotal updated to ${subtotal}`);
    });

    const tax = Math.round(subtotal * TAX_RATE * 100) / 100;
    const total = Math.round((subtotal + tax) * 100) / 100;

    subtotalElement.textContent = formatPrice(subtotal);
    taxElement.textContent = formatPrice(tax);
    totalElement.textContent = formatPrice(total); // Update subtotal, tax, and total
    localStorage.setItem("subtotal", subtotal.toFixed(2));
    localStorage.setItem("tax", tax.toFixed(2));
    localStorage.setItem("total", total.toFixed(2));
  }

  //-------------------------------- List Manipulation --------------------------------
  async function addBarcodeToList(barcode) {
    if (barcodeList.children.length === 0) startPopup.style.display = "none"; // Hide start popup on first item

    document
      .querySelectorAll(".barcode-item.selected")
      .forEach((item) => item.classList.remove("selected"));
    let existingItem = barcodeItemMap[barcode];

    if (existingItem) {
      const quantityElement = existingItem.querySelector(
        ".barcode-list-productQuantity"
      );
      quantityElement.textContent = parseInt(quantityElement.textContent) + 1; // Increment quantity
      updateTotalPrice(existingItem);
      displayItemOnLeft(existingItem);
    } else {
      const productData = await fetchProductByBarcode(barcode);
      if (!productData) return openErrorPopup();

      const li = document.createElement("li");
      li.className = "barcode-item";
      li.innerHTML = `
        <img src="http://localhost:8080/product/image?id=${barcode}" alt="${productData.name}" class="barcode-list-productImage">
        <p class="barcode-list-productName">${productData.name}</p>
        <p class="barcode-list-productBarcode">${productData.id}</p>
        <p class="barcode-list-productSinglePrice">${productData.price.toFixed(
          2
        )}$</p>
        <p class="barcode-list-productQuantity">1</p>
        <p class="barcode-list-productTotalPrice">${productData.price.toFixed(
          2
        )}$</p>
        <button class="delete-btn">Delete</button>
      `;

      li.querySelector(".delete-btn").addEventListener("click", () =>
        removeBarcodeItem(li, barcode)
      ); // Delete button handler

      li.addEventListener("click", () => {
        document
          .querySelectorAll(".barcode-item")
          .forEach((item) => item.classList.remove("selected"));
        li.classList.add("selected"); // Highlight selected item
        displayItemOnLeft(li);
      });

      barcodeList.appendChild(li);
      barcodeItemMap[barcode] = li;
      displayItemOnLeft(li);
    }

    calculateTotals();
    saveBarcodes();
    if (localStorage.getItem("discountCode")) {
        updateCouponAppliedDiscountValue();
    }
    barcodeList.scrollTop = barcodeList.scrollHeight; // Auto-scroll to bottom
    focusBarcodeInput();
  }

  function removeBarcodeItem(li, barcode) {
    li.remove();
    delete barcodeItemMap[barcode];
    calculateTotals();
    saveBarcodes();
    if (localStorage.getItem("discountCode")) {
        updateCouponAppliedDiscountValue();
    }
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
      const quantityElement = currentSelectedItem.querySelector(
        ".barcode-list-productQuantity"
      );
      const newQuantity = parseInt(quantityElement.textContent) + amount;

      if (newQuantity > 0) {
        quantityElement.textContent = newQuantity;
        quantityDisplay.textContent = newQuantity;
        updateTotalPrice(currentSelectedItem);
      } else {
        const barcode = currentSelectedItem.querySelector(
          ".barcode-list-productBarcode"
        ).textContent;
        removeBarcodeItem(currentSelectedItem, barcode);
      }

      calculateTotals();
      saveBarcodes();
      if (localStorage.getItem("discountCode")) {
        updateCouponAppliedDiscountValue();
      }
    }
  };

  document
    .getElementById("quantity-btn-plus")
    .addEventListener("click", () => adjustQuantity(1)); // Increment quantity
  document
    .getElementById("quantity-btn-minus")
    .addEventListener("click", () => adjustQuantity(-1)); // Decrement quantity

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

  //-------------------------------- Coupon Code Functionality -------------------------------- 
  couponButton.addEventListener("click", () => {
    couponPopup.style.display = "flex";
    couponInput.focus();
  });

  couponCancelButton.addEventListener("click", () => {
    couponInput.value = "";
    couponPopup.style.display = "none";
    focusBarcodeInput();
  });


  couponApplyButton.addEventListener("click", async () => {
    await applyCoupon();
  });

  couponInput.addEventListener("keyup", async (event) => {
    if (event.key === "Enter") {
      await applyCoupon();
    }
  }); 

  couponAppliedRemoveBtn.addEventListener("click", () => {
    localStorage.removeItem("discountCode");
    localStorage.removeItem("discountValue");
    couponAppliedContainer.style.display = "none";
    calculateTotals(); // Recalculate totals after removing discount
  });


  function updateCouponAppliedDiscountValue() {
    const discountCode = localStorage.getItem("discountCode");
    const discountValue = parseFloat(localStorage.getItem("discountValue"));

    // Calculate original subtotal without discount
    let originalSubtotal = 0;
    document.querySelectorAll(".barcode-item").forEach((li) => {
        const singlePrice = parsePrice(li.querySelector(".barcode-list-productSinglePrice").textContent);
        const quantity = parseInt(li.querySelector(".barcode-list-productQuantity").textContent);
        originalSubtotal += singlePrice * quantity;
    });

    // Update coupon applied container
    couponAppliedContainer.style.display = "grid";
    couponAppliedText.textContent = `Coupon applied: ${discountCode}`;
    couponAppliedDiscountValue.textContent = `Saved: ${formatPrice(originalSubtotal * (discountValue / 100))}`;
  }


  async function applyCoupon() {
    const couponCode = couponInput.value.trim();
    if (couponCode) {
      try {
        const response = await fetch(`http://localhost:8080/discount?code=${encodeURIComponent(couponCode)}`);
        if (!response.ok) {
          throw new Error("Invalid promo code.");
        }
        const discount = await response.json();
        console.log(`Coupon code applied: ${discount.code} with value ${discount.value}`);
        
        // Store discount information locally (you can also manage this state differently)
        localStorage.setItem("discountCode", discount.code);
        localStorage.setItem("discountValue", discount.value);

        couponInput.value = "";
        couponPopup.style.display = "none";

        // Update total price after discount is applied
        const listItems = document.querySelectorAll(".barcode-item");
        for (const item of listItems) {
          updateTotalPrice(item); 
        }  
        calculateTotals();
        saveBarcodes();
        updateCouponAppliedDiscountValue();
        focusBarcodeInput();
      } catch (error) {
        console.error("Invalid promo code:", error);
        alert("Invalid promo code. Please try again.");
      }
    } else {
      alert("Please enter a valid promo code.");
    }
  };

  //-------------------------------- Storage Handling --------------------------------
  function saveBarcodes() {
    const barcodes = Array.from(document.querySelectorAll(".barcode-item")).map(
      (item) => ({
        barcode: item.querySelector(".barcode-list-productBarcode").textContent,
        name: item.querySelector(".barcode-list-productName").textContent,
        price: item.querySelector(".barcode-list-productSinglePrice")
          .textContent,
        quantity: item.querySelector(".barcode-list-productQuantity")
          .textContent,
      })
    );
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
