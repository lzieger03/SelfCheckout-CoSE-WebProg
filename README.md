# ScanMate 1.0.0-X "Aurora"

## What's This About?

Welcome to the ScanMate 1.0.0-X "Aurora" project! This is all about making your supermarket checkout experience as smooth as possible. Imagine walking up to a kiosk, scanning your items, choosing how you want to pay, and getting a receipt—all without waiting in line. That's what we're building here.

## How It Works

- **Scan Your Stuff**: Use a barcode scanner to add items to your cart. Each item pops up with its name and price.
- **Pay Your Way**: Whether you're a cash person or a card swiper, we've got you covered. The system guides you through the payment process.
- **Get Your Receipt**: Once you pay, a receipt prints out with all the details—what you bought, how much it cost, and how you paid.


## What's Under the Hood?

- **Frontend**: Built with HTML, CSS, and JavaScript. It's the face of the system where all the action happens.
- **Backend**: Powered by Java and Spring Boot. This is where the magic happens—handling all the data and logic.
- **Database**: A SQLite Database keeps track of all the product info, like barcodes, names, and prices.


## Project Structure

Here's how the project is organized:

1. **Frontend**:
   - **HTML**: The main interface is defined in `index.html`, which includes various sections for product display, barcode input, and payment options.
   - **CSS**: Styling is managed in `styles.css`, providing layout and design for the application.
   - **JavaScript**: Scripts like `barcode-script.js` handle barcode scanning and interaction logic.

2. **Backend**:
   - **Java**: The backend is built using Spring Boot, with configurations like `WebConfig.java` to manage CORS settings.
   - **Database**: The application uses a SQLite database, `item_database.db`, to store product information.

3. **Configuration**:
   - **Maven**: The project uses Maven for dependency management, as specified in `pom.xml`.
   - **Git**: The `.gitignore` file ensures that unnecessary files are not tracked in version control.


## The Big Picture

This project is a full-stack application, meaning we're covering everything from the user interface to the database. It's a great way to dive into modern web development and see how all the pieces fit together.

## Why Do It?

We're aiming to cut down on wait times and make shopping a breeze. Plus, it's a fun way to learn about building a complete application from scratch.

## The Team

We're a dynamic duo of student developers, Marven Drechsel and Lars Zieger, working together on both the frontend and backend aspects of the project. This project is part of our coursework for a lecture at DHBW Karlsruhe, where we're learning to integrate various technologies into a cohesive application. It's all about collaboration and learning as we tackle every part of the system together.

## Wrap-Up

In a nutshell, ScanMate 1.0.0-X "Aurora" is about creating a checkout experience that's fast, friendly, and accessible to everyone. Whether you're a developer looking to learn or a shopper wanting a better checkout, this project has something for you.

## Disclaimer

### General 
This project is a learning exercise and is not intended for production use. It is a proof of concept and may not be fully functional or secure for real-world applications.

### Security
The backend is not secured and does not use HTTPS. It is intended for local use only.

### Database
The database is not backed up and does not use a remote database. It is intended for local use only.

### Discounts
The discount system is not secured and does not use a remote discount database. It is intended for local use only.

### Barcodes
The barcode system is not secured and does not use a remote barcode database. It is intended for local use only.

### Payment
The payment system is not secured and does not use a remote payment processor. It is intended for local use only.

### Receipts
The receipt system is not secured and does not use a remote receipt printer. It is intended for local use only.

### Accessibility
The accessibility features are not fully implemented and do not provide full accessibility support. This is a known limitation, but will not be implemented in this version.

