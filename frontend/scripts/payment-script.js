document.addEventListener("DOMContentLoaded", () => {
  const paymentButton = document.getElementById("payment-button");
  const popup = document.getElementById("payment-popup");
  const closeButton = document.getElementById("payment-close-popup");
  const proceedButton = document.getElementById("payment-popup-proceed-button");
  const paymentMethods = document.querySelectorAll('input[name="payment"]');

  // Show the pop-up when the payment button is clicked
  paymentButton.addEventListener("click", () => {
    popup.style.display = "flex";
  });

  // Close the pop-up when the close button (X) is clicked
  closeButton.addEventListener("click", () => {
    popup.style.display = "none";
  });

  // Enable the proceed button when a payment method is selected
  paymentMethods.forEach((method) => {
    method.addEventListener("change", () => {
      proceedButton.disabled = false;
      proceedButton.classList.add("active");
    });
  });

  // Add functionality for the proceed button
  proceedButton.addEventListener("click", () => {
    if (!proceedButton.disabled) {
      const selectedMethod = document.querySelector(
        'input[name="payment"]:checked'
      ).value;

      fetchPostPrint();

      popup.style.display = "none";
    }
  });

  async function fetchPostPrint() {
    try {
      const storedProducts = localStorage.getItem("barcodes");
      const productsArray = JSON.parse(storedProducts) || [];

      console.log("storedProducts: " + storedProducts);
      console.log("productsArray: " + productsArray);

      const products = productsArray.map((item) => ({
        barcode: item.barcode,
        quantity: Number(item.quantity), // Convert quantity to a number
      }));

      console.log(products);

      const response = await fetch("http://localhost:8080/print", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(products), // Send the array directly
      });

      console.log(response);

      if (!response.ok) {
        const errorMessage = `Server responded with status: ${response.status} (${response.statusText})`;
        let errorDetails = await response.text();

        try {
          const errorJson = JSON.parse(errorDetails);
          errorDetails = `Error: ${errorJson.error}\nPath: ${errorJson.path}\nTimestamp: ${errorJson.timestamp}`;
        } catch (jsonError) {
          // If response is not JSON, log the raw text
          console.log("Response:", errorDetails);
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
