document.addEventListener('DOMContentLoaded', () => {
    const paymentButton = document.getElementById('payment-button');
    const popup = document.getElementById('payment-popup');
    const closeButton = document.getElementById('close-popup');
    const proceedButton = document.getElementById('proceed-button');
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

    // You can add functionality for the proceed button here
    proceedButton.addEventListener('click', () => {
        if (!proceedButton.disabled) {
            alert('Proceeding with payment...');
            // Add payment processing logic here
        }
    });
});
