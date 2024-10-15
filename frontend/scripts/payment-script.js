document.addEventListener("DOMContentLoaded", () => {
  // Get DOM elements
  const paymentButton = document.getElementById("payment-button");
  const popup = document.getElementById("payment-popup");
  const closeButton = document.getElementById("payment-close-popup");
  const proceedButton = document.getElementById("payment-popup-proceed-button");
  const paymentMethods = document.querySelectorAll('input[name="payment"]');

  // Show pop-up on payment button click
  paymentButton.addEventListener("click", () => {
    popup.style.display = "flex"; // Show pop-up
  });

  // Close pop-up on close button click
  closeButton.addEventListener("click", () => {
    popup.style.display = "none"; // Hide pop-up
  });

  // Enable proceed button when a payment method is selected
  paymentMethods.forEach((method) => {
    method.addEventListener("change", () => {
      proceedButton.disabled = false; // Enable proceed button
      proceedButton.classList.add("active"); // Add active styling
    });
  });

  // Proceed button functionality
  proceedButton.addEventListener("click", () => {
    if (!proceedButton.disabled) { // Ensure button is enabled
      const selectedMethod = document.querySelector(
        'input[name="payment"]:checked' // Get selected payment method
      ).value;

      fetchPostPrint(); // Call function to send data

      popup.style.display = "none"; // Hide pop-up
    }
  });

  // Function to send data for printing
  async function fetchPostPrint() {
    try {
      // Get products from local storage
      const storedProducts = localStorage.getItem("barcodes");
      const productsArray = JSON.parse(storedProducts) || []; // Parse or use empty array

      console.log("storedProducts: " + storedProducts);
      console.log("productsArray: " + productsArray);

      // Prepare data for API request
      const products = productsArray.map((item) => ({
        barcode: String(item.barcode),
        name: item.name,
        price: parseFloat(item.price), 
        quantity: Number(item.quantity), // Convert quantity to number
      }));

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

        throw new Error(`${errorMessage}.\nDetails: ${errorDetails}`);
      }

      const data = await response.json();
      console.log("Success:", data);
    } catch (error) {
      console.error("Error at API-Call:", error);
      alert(`There was an issue processing your request:\n${error.message}`);
    }
  }
});