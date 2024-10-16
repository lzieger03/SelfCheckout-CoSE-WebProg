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
  function isCartEmpty() {
    const storedProducts = localStorage.getItem("barcodes");
    const productsArray = JSON.parse(storedProducts) || [];
    return productsArray.length === 0; // Returns true if cart is empty
  }

  // --- Show popups on payment button click ---
  paymentButton.addEventListener("click", () => {
    if (isCartEmpty()) {
      cartEmptyPopup.style.display = "flex"; // Show empty cart popup
    } else {
      paymentPopup.style.display = "flex"; // Show payment popup
    }
  });

  // --- Close CartEmpty-popup ---
  cartEmptyPopup.addEventListener("click", () => {
    cartEmptyPopup.style.display = "none"; // Hide empty cart popup
  });

  // --- Hide CartEmpty-popup on barcode input ---
  barcodeInput.addEventListener("input", () => {
    cartEmptyPopup.style.display = "none"; // Hide empty cart popup on barcode input
  });

  // --- Close Payment-popup ---
  closeButton.addEventListener("click", () => {
    paymentPopup.style.display = "none"; // Hide payment popup
  });

  // --- Enable proceed button when payment method selected ---
  paymentMethods.forEach((method) => {
    method.addEventListener("change", () => {
      if (!isCartEmpty()) {
        proceedButton.disabled = false; // Enable proceed button if cart is not empty
        proceedButton.classList.add("active"); // Add active styling
      }
    });
  });

  // --- Proceed button functionality ---
  proceedButton.addEventListener("click", () => {
    if (!proceedButton.disabled && !isCartEmpty()) {
      // Ensure button is enabled and cart is not empty
      const selectedMethod = document.querySelector('input[name="payment"]:checked').value; // Get selected payment method

      fetchPostPrint(); // Call function to send data

      paymentPopup.style.display = "none"; // Hide payment popup
    } else {
      alert("Your cart is empty. Please add products before proceeding to payment.");
    }
  });

  // --- Send data for printing ---
  async function fetchPostPrint() {
    try {
      // Get selected payment method
      const selectedMethod = document.querySelector('input[name="payment"]:checked').value;

      // Get products from local storage
      const storedProducts = localStorage.getItem("barcodes");
      const productsArray = JSON.parse(storedProducts) || []; // Parse or use empty array

      console.log("storedProducts: " + storedProducts);
      console.log("productsArray: " + productsArray);

      // Prepare data for API request
      const products = [
        {
          barcode: "", // Assuming no barcode for payment method
          name: selectedMethod, // Add the selected payment method as the name
          price: 0, // No price associated with the payment method
          quantity: 1, // Default quantity for payment method
        },
        ...productsArray.map((item) => ({
          barcode: String(item.barcode),
          name: item.name,
          price: parseFloat(item.price),
          quantity: Number(item.quantity), // Convert quantity to number
        })),
      ];

      console.log(products);

      // Send data to server
      const response = await fetch("http://localhost:8080/print", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(products),
      });

      console.log(response);

      // Handle server response
      if (!response.ok) {
        const errorMessage = `Server responded with status: ${response.status} (${response.statusText})`;
        let errorDetails = await response.text();

        try {
          // Parse error details if JSON
          const errorJson = JSON.parse(errorDetails);
          errorDetails = `Error: ${errorJson.error}\nPath: ${errorJson.path}\nTimestamp: ${errorJson.timestamp}`;
        } catch (jsonError) {
          console.log("Response:", errorDetails); // Log raw text if not JSON
        }

        throw new Error(`${errorMessage}. \nDetails: ${errorDetails}`);
      }

      const data = await response.json();
      console.log("Success:", data);
      location.reload(); // Reload after successful payment
    } catch (error) {
      console.error("Error at API-Call:", error);
    }
  }
});
