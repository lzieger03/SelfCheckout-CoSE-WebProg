document.addEventListener("DOMContentLoaded", () => {
  // --- Get DOM elements ---
  const paymentButton = document.getElementById("payment-button");
  const cartEmptyPopup = document.getElementById("cartEmpty-popup");
  const barcodeInput = document.getElementById("barcode-input");
  const paymentPopup = document.getElementById("payment-popup");
  const closeButton = document.getElementById("payment-close-popup");
  const proceedButton = document.getElementById("payment-popup-proceed-button");
  const paymentMethods = document.querySelectorAll('input[name="payment"]');

  // --- Check if cart is empty ---
  const isCartEmpty = () => {
    const storedProducts = localStorage.getItem("barcodes");
    return !storedProducts || JSON.parse(storedProducts).length === 0;
  };

  // --- Show appropriate popup on payment button click ---
  paymentButton.addEventListener("click", () => {
    cartEmptyPopup.style.display = isCartEmpty() ? "flex" : "none";
    paymentPopup.style.display = isCartEmpty() ? "none" : "flex";
  });

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
        discountValue: discountValue   // Include discount value
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

  // --- Promo Code Functionality ---
  const togglePromoCode = document.getElementById("toggle-promo-code");
  const promoCodeInput = document.getElementById("promo-code-input");
  const promoCodeField = document.getElementById("promo-code");
  const applyPromoCode = document.getElementById("apply-promo-code");

  let discountCode = null;
  let discountValue = 0;

  togglePromoCode.addEventListener("click", () => {
    promoCodeInput.style.display = promoCodeInput.style.display === "none" ? "block" : "none";
    togglePromoCode.textContent = promoCodeInput.style.display === "none" ? "Have a promo code? ^" : "Have a promo code? v";
  });

  applyPromoCode.addEventListener("click", async () => {
    const promoCode = promoCodeField.value.trim();
    if (promoCode) {
      try {
        const response = await fetch(`http://localhost:8080/discount?code=${encodeURIComponent(promoCode)}`);
        if (!response.ok) {
          throw new Error("Invalid promo code.");
        }
        const discount = await response.json();
        console.log(`Promo code applied: ${discount.code} with value ${discount.value}`);
        alert(`Promo code "${discount.code}" applied! Discount Value: ${discount.value}`);
        
        // Store discount information locally (you can also manage this state differently)
        localStorage.setItem("discountCode", discount.code);
        localStorage.setItem("discountValue", discount.value);

        promoCodeField.value = "";
        promoCodeInput.style.display = "none";
        togglePromoCode.textContent = "Have a promo code? ^";
      } catch (error) {
        console.error("Invalid promo code:", error);
        alert("Invalid promo code. Please try again.");
      }
    } else {
      alert("Please enter a valid promo code.");
    }
  });
});
