document.addEventListener("DOMContentLoaded", () => {
  // --- Get DOM elements ---
  const paymentButton = document.getElementById("payment-button");
  const cartEmptyPopup = document.getElementById("cartEmpty-popup");
  const barcodeInput = document.getElementById("barcode-input");
  
  // --- Get all payment popup related elements ---
  const paymentPopup = document.getElementById("payment-popup");
  const paymentCloseButton = document.getElementById("payment-popup-close-btn");
  const paymentProceedButton = document.getElementById("payment-popup-proceed-button");
  const paymentMethods = document.querySelectorAll('input[name="payment"]');
  const paymentPopupSubtotal = document.getElementById("payment-popup-subtotal");
  const paymentPopupTax = document.getElementById("payment-popup-tax");
  const paymentPopupTotal = document.getElementById("payment-popup-total");

  // --- Get all coupon related elements ---
  const couponPopup = document.getElementById("coupon-popup");

  // --- Get receipt printing popup element ---
  const receiptIsPrintingPopup = document.getElementById("receiptIsPrinting-popup");

  // --- Check if cart is empty ---
  const isCartEmpty = () => {
    const storedProducts = localStorage.getItem("barcodes");
    return !storedProducts || JSON.parse(storedProducts).length === 0;
  };

  // --- Show appropriate popup on payment button click ---
  paymentButton.addEventListener("click", (event) => {
    event.preventDefault(); // Prevent default behavior (e.g., form submission)
    if (isCartEmpty()) {
      cartEmptyPopup.style.display = "flex";
      couponPopup.style.display = "none";
      paymentPopup.style.display = "none";
    } else {
      updatePaymentPopup();
      paymentPopup.style.display = "flex";
      cartEmptyPopup.style.display = "none";
      couponPopup.style.display = "none";
      // Disable barcode input to prevent changes during payment process
      barcodeInput.disabled = true;
    }
  });

  // --- Update payment popup ---
  function updatePaymentPopup() {
    const subtotal = parseFloat(localStorage.getItem("subtotal")) || 0.0;
    const tax = parseFloat(localStorage.getItem("tax")) || 0.0;
    const total = parseFloat(localStorage.getItem("total")) || 0.0;

    paymentPopupSubtotal.textContent = `$${subtotal.toFixed(2)}`;
    paymentPopupTax.textContent = `$${tax.toFixed(2)}`;
    paymentPopupTotal.textContent = `$${total.toFixed(2)}`;
  }

  // --- Close popups ---
  cartEmptyPopup.addEventListener("click", () => {
    cartEmptyPopup.style.display = "none";
  });

  paymentCloseButton.addEventListener("click", () => {
    paymentPopup.style.display = "none";
    couponPopup.style.display = "none";
  });

  barcodeInput.addEventListener("input", () => {
    cartEmptyPopup.style.display = "none";
  });

  // --- Enable Payment Proceed button when payment method selected ---
  paymentMethods.forEach((method) => {
    method.addEventListener("change", () => {
      if (!isCartEmpty()) {
        paymentProceedButton.disabled = false;
        paymentProceedButton.classList.add("active");
      }
    });
  });

  // --- Proceed button functionality ---
  paymentProceedButton.addEventListener("click", async (event) => {
    event.preventDefault(); // Prevent default behavior (e.g., form submission)
    if (!paymentProceedButton.disabled && !isCartEmpty()) {
      await fetchPostPrint();
      paymentPopup.style.display = "none";
    } else {
      cartEmptyPopup.style.display = "flex";
    }
  });

  // --- Send data for printing ---
  async function fetchPostPrint() {
    try {
      console.log("fetchPostPrint: Function started");

      // --- Create payload ---
      const selectedMethod = document.querySelector('input[name="payment"]:checked').value;
      console.log("Selected Payment Method:", selectedMethod);

      const storedProducts = localStorage.getItem("barcodes");
      const productsArray = JSON.parse(storedProducts) || [];
      console.log("Stored Products:", productsArray);

      // Retrieve discount information
      const discountCode = localStorage.getItem("discountCode") || "";
      const discountValue = parseFloat(localStorage.getItem("discountValue")) || 0.0;
      console.log("Discount Code:", discountCode, "Discount Value:", discountValue);

      // Prepare data for API request
      const products = productsArray.map((item) => ({
        id: String(item.barcode),
        name: item.name,
        price: parseFloat(item.price),
        quantity: Number(item.quantity)
      }));
      console.log("Prepared Products for API:", products);

      const payload = {
        paymentMethod: selectedMethod,
        cartObjects: products,
        discountCode,
        discountValue
      };
      console.log("Payload Created:", payload);

      // Show awaiting response alert
      console.log("fetchPostPrint: Awaiting response...");
      // alert("Awaiting response..."); // Optional: Remove for testing

      // Send API request
      const response = await fetch(`http://127.0.0.1:8080/print`, { // Changed to 127.0.0.1 for consistency
        keepalive: true,
        mode: 'cors',
        timeout: 10000,
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      console.log("fetchPostPrint: Received response with status:", response.status);

      if (!response.ok) {
        const errorMsg = await response.text();
        console.error("fetchPostPrint: Response not OK. Error Message:", errorMsg);
        throw new Error(errorMsg || "Network response was not ok");
      }

      const result = await response.json();
      console.log("fetchPostPrint: Response JSON:", result);

      if (result.message === "Print successful") {
        console.log("Printing successful");
        receiptIsPrintingPopup.style.display = "flex";

        // Clean up after successful print
        localStorage.removeItem("discountCode");
        localStorage.removeItem("discountValue");
        console.log("fetchPostPrint: Cleaned up discount data");

        // Show the popup for at least 5 seconds or until user clicks
        setTimeout(() => {
          receiptIsPrintingPopup.style.display = "none";
          // Optionally reload the page after 5 seconds
          window.location.reload();
        }, 5000);
      } else {
        console.error("fetchPostPrint: Unexpected message:", result.message);
        throw new Error(result.error || "Printing failed");
      }

    } catch (error) {
      console.error("Error at fetchPostPrint:", error);
      alert("Error: " + (error.message || "Network error or CORS problem"));
    }
  }

  // --- Accessibility Enhancements ---
  // Allow closing the popup with the `Escape` key
  document.addEventListener("keydown", (event) => {
    if (event.key === "Escape") {
      if (paymentPopup.style.display === "flex") {
        paymentPopup.style.display = "none";
      }
      if (cartEmptyPopup.style.display === "flex") {
        cartEmptyPopup.style.display = "none";
        couponPopup.style.display = "none";
      }
      if (receiptIsPrintingPopup.style.display === "flex") {
        receiptIsPrintingPopup.style.display = "none";
        // Optionally reload the page upon closing the popup
        // window.location.reload();
      }
    }
  });

  // --- Close Receipt Popup on Click ---
  receiptIsPrintingPopup.addEventListener("click", () => {
    receiptIsPrintingPopup.style.display = "none";
    // Optionally reload the page upon closing the popup
    window.location.reload();
  });
});
