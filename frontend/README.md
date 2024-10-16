# Frontend - ScanMate 1.0.0-X "Aurora"

## Overview

The frontend of the ScanMate project is designed to provide a user-friendly interface for the self-checkout system. It allows users to scan items, view their cart, and proceed to payment seamlessly.

## Structure

1. **HTML (`index.html`)**:
   - Defines the main structure of the application, including popups for start, item not found, and empty cart notifications.
   - Contains sections for product display, barcode input, and payment options.

   ```html:frontend/index.html
   startLine: 1
   endLine: 173
   ```

2. **CSS (`styles.css`)**:
   - Provides styling for the entire application, ensuring a consistent and visually appealing layout.
   - Includes styles for the header, main layout, and specific sections like the left and right panels.

   ```css:frontend/styles.css
   startLine: 1
   endLine: 596
   ```

3. **JavaScript (`barcode-script-backup.js`)**:
   - Handles the logic for barcode scanning and interaction with the backend API.
   - Manages popups and user input, ensuring a smooth user experience.

   ```javascript:frontend/scripts/barcode-script.js
   startLine: 1
   endLine: 212
   ```

4. **JavaScript (`payment-script.js`)**:
   - Handles the logic for payment processing and receipt generation.
  
   ```javascript:frontend/scripts/payment-script.js
   startLine: 1
   endLine: 89
   ```  

## Key Features

- **Responsive Design**: The layout adapts to different screen sizes, providing a consistent experience across devices.
- **Interactive Elements**: Buttons and inputs are styled for ease of use, with hover effects and transitions.
- **Accessibility**: Features like screen readers and text size adjustments *will be implemented in future versions*.

## How It Works

- Users start by scanning items using the barcode input.
- The system fetches product details from the backend and updates the cart.
- Users can adjust quantities, view totals, and proceed to payment.

## Development

The frontend is built using standard web technologies: HTML, CSS, and JavaScript. It is designed to be lightweight and efficient, ensuring quick load times and a responsive interface.
