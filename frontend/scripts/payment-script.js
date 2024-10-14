document.addEventListener('DOMContentLoaded', () => {
    const paymentButton = document.getElementById('payment-button');
    const popup = document.getElementById('payment-popup');
    const closeButton = document.getElementById('payment-close-popup');
    const proceedButton = document.getElementById('payment-popup-proceed-button');
    const paymentMethods = document.querySelectorAll('input[name="payment"]');

    // Show the pop-up when the payment button is clicked
    paymentButton.addEventListener('click', () => {
        popup.style.display = 'flex';
    });

    // Close the pop-up when the close button (X) is clicked
    closeButton.addEventListener('click', () => {
        popup.style.display = 'none';
    });

    // Enable the proceed button when a payment method is selected
    paymentMethods.forEach(method => {
        method.addEventListener('change', () => {
            proceedButton.disabled = false;
            proceedButton.classList.add('active');
        });
    });

    // Add functionality for the proceed button
    proceedButton.addEventListener('click', () => {
        if (!proceedButton.disabled) {
            const selectedMethod = document.querySelector('input[name="payment"]:checked').value;
            alert('Proceeding with payment...');

            fetchPostPrint();

            popup.style.display = 'none';
        }
    });

    async function fetchPostPrint() {
        try {
          const response = await fetch(
            `http://localhost:8080/print?products=${localStorage.barcodes}`, 
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            }
          );
    
          if (!response.ok) {
            throw new Error("Error with the server response");
          }
        } catch (error) {
          console.error("Error at API-Call:", error);
        }
      }
});
