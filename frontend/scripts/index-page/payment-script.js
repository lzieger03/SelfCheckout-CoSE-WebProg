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

  // --- Check if cart is empty ---
  const isCartEmpty = () => {
    const storedProducts = localStorage.getItem("barcodes");
    return !storedProducts || JSON.parse(storedProducts).length === 0;
  };

  // --- Show appropriate popup on payment button click ---
  paymentButton.addEventListener("click", () => {
    if (isCartEmpty()) {
      cartEmptyPopup.style.display = "flex";
      couponPopup.style.display = "none";
      paymentPopup.style.display = "none";
    } else {
      updatePaymentPopup();
      paymentPopup.style.display = "flex";
      cartEmptyPopup.style.display = "none";
      couponPopup.style.display = "none";
      // lose input focus on barcode input
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
  paymentProceedButton.addEventListener("click", () => {
    if (!paymentProceedButton.disabled && !isCartEmpty()) {
      fetchPostPrint();
      paymentPopup.style.display = "none";
    } else {
      cartEmptyPopup.style.display = "flex";
    }
  });

  // --- Send data for printing ---
  function fetchPostPrint() {
    try {
      // --- Create payload ---
      const selectedMethod = document.querySelector('input[name="payment"]:checked').value;
      const storedProducts = localStorage.getItem("barcodes");
      const productsArray = JSON.parse(storedProducts) || [];
      
      // Retrieve discount information
      const discountCode = localStorage.getItem("discountCode") || "";
      const discountValue = parseFloat(localStorage.getItem("discountValue")) || 0.0;

      // Prepare data for API request
      const products = productsArray.map((item) => ({
        id: String(item.barcode),
        name: item.name,
        price: parseFloat(item.price),
        quantity: Number(item.quantity)
      }));

      const payload = {
        paymentMethod: selectedMethod,
        cartObjects: products,
        discountCode,
        discountValue
      };

      // Send API request with Promise chain
      fetch(`http://localhost:8080/print`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      })
      .then(response => response.json())
      .then(result => {
        if (result.status === "success") {
          console.log("Printing successful");
          alert("Printing successful");
          // Clean up after successful print
          localStorage.removeItem("discountCode");
          localStorage.removeItem("discountValue");
        } else {
          throw new Error(result.error || "Printing failed");
        }
      })
      .catch(error => {
        console.error("Error at fetchPostPrint:", error);
        alert("Error: " + (error.message || "Network error or CORS problem"));
      });

      setTimeout(() => {                        // Somehow the response is not available immediately - asyn/await doesn't work, don't ask me why
        console.log("Awaiting response...");    // without a wait, the request times out - don't ask me why
      }, 1000);                                 // This is a workaround, not a fix - I hate it

    } catch (error) {
      console.error("Error:", error);
      alert(error instanceof SyntaxError 
        ? "Received malformed JSON from the server."
        : `Error: ${error.message || "An unknown error occurred."}`
      );
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
    }
  });
});
