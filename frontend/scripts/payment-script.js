document.addEventListener("DOMContentLoaded", () => {
  // --- Get DOM elements ---
  const paymentButton = document.getElementById("payment-button");
  const cartEmptyPopup = document.getElementById("cartEmpty-popup");
  const barcodeInput = document.getElementById("barcode-input");
  const paymentPopup = document.getElementById("payment-popup");
  const closeButton = document.getElementById("payment-close-popup");
  const proceedButton = document.getElementById("payment-popup-proceed-button");
  const paymentMethods = document.querySelectorAll('input[name="payment"]');
  const paymentPopupSubtotal = document.getElementById("payment-popup-subtotal");
  const paymentPopupTax = document.getElementById("payment-popup-tax");
  const paymentPopupTotal = document.getElementById("payment-popup-total");
  const couponButton = document.getElementById("coupon-btn");


  // --- Check if cart is empty ---
  const isCartEmpty = () => {
    const storedProducts = localStorage.getItem("barcodes");
    return !storedProducts || JSON.parse(storedProducts).length === 0;
  };

  // --- Show appropriate popup on payment button click ---
  paymentButton.addEventListener("click", () => {
    cartEmptyPopup.style.display = isCartEmpty() ? "flex" : "none";
    paymentPopup.style.display = isCartEmpty() ? "none" : "flex";
    couponButton.style.display = isCartEmpty() ? "none" : "flex";
    updatePaymentPopup();
  });

  // --- Update payment popup ---
  function updatePaymentPopup() {
    const subtotal = localStorage.getItem("subtotal") || 0.0;
    const tax = localStorage.getItem("tax") || 0.0;
    const total = localStorage.getItem("total") || 0.0;

    paymentPopupSubtotal.textContent = `Subtotal: ${subtotal}$`;
    paymentPopupTax.textContent = `Tax: ${tax}$`;
    paymentPopupTotal.textContent = `Total: ${total}$`;
  }

  // --- Close popups ---
  cartEmptyPopup.addEventListener("click", () => (cartEmptyPopup.style.display = "none"));
  closeButton.addEventListener("click", () => (paymentPopup.style.display = "none"));
  barcodeInput.addEventListener("input", () => (cartEmptyPopup.style.display = "none"));

  // --- Enable proceed button when payment method selected ---
  paymentMethods.forEach((method) => {
    method.addEventListener("change", () => {
      if (!isCartEmpty()) {
        proceedButton.disabled = false;
        proceedButton.classList.add("active");
      }
    });
  });

  // --- Proceed button functionality ---
  proceedButton.addEventListener("click", () => {
    if (!proceedButton.disabled && !isCartEmpty()) {
      fetchPostPrint();
      paymentPopup.style.display = "none";
    } else {
      cartEmptyPopup.style.display = "flex";
    }
  });

  // --- Send data for printing ---
  async function fetchPostPrint() {
    try {
      const selectedMethod = document.querySelector('input[name="payment"]:checked').value;
      const storedProducts = localStorage.getItem("barcodes");
      const productsArray = JSON.parse(storedProducts) || [];

      // Retrieve discount information from local storage
      const discountCode = localStorage.getItem("discountCode") || "";
      const discountValue = parseFloat(localStorage.getItem("discountValue")) || 0.0;

      // Prepare data for API request
      const products = productsArray.map((item) => ({
        id: String(item.barcode),
        name: item.name,
        price: parseFloat(item.price),
        quantity: Number(item.quantity),
      }));

      const payload = {
        paymentMethod: selectedMethod,
        cartObjects: products,
        discountCode: discountCode,    // Include discount code
        discountValue: discountValue   // Include discount value (percentage)
      };

      const response = await fetch("http://localhost:8080/print", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const errorDetails = await response.text();
        try {
          const errorJson = JSON.parse(errorDetails);
          throw new Error(`Server responded with status: ${response.status} (${response.statusText}).\nError: ${errorJson.error}\nPath: ${errorJson.path}\nTimestamp: ${errorJson.timestamp}`);
        } catch {
          throw new Error(`Server responded with status: ${response.status} (${response.statusText}).\nDetails: ${errorDetails}`);
        }
      }

      console.log("Success:", await response.json());
      // Clear discount information after successful print
      localStorage.removeItem("discountCode");
      localStorage.removeItem("discountValue");
      location.reload();
    } catch (error) {
      console.error("Error at API-Call:", error);
      alert(`Error: ${error.message}`);
    }
  }
});
