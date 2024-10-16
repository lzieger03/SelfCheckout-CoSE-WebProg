# ScanMate 1.0.0-X "Aurora"

## What's This About?

Welcome to the ScanMate 1.0.0-X "Aurora" project! This is all about making your supermarket checkout experience as smooth as possible. Imagine walking up to a kiosk, scanning your items, choosing how you want to pay, and getting a receipt—all without waiting in line. That's what we're building here.

## How It Works

- **Scan Your Stuff**: Use a barcode scanner to add items to your cart. Each item pops up with its name and price.
- **Pay Your Way**: Whether you're a cash person or a card swiper, we've got you covered. The system guides you through the payment process.
- **Get Your Receipt**: Once you pay, a receipt prints out with all the details—what you bought, how much it cost, and how you paid.

## Who's It For?

We've thought about everyone:

- **Kids**: Big buttons and simple instructions make it easy for the little ones.
- **Seniors**: Large text and high-contrast themes help those who need a bit more visibility.
- **Busy Bees**: Quick and efficient for those who just want to get in and out.

## What's Under the Hood?

- **Frontend**: Built with HTML, CSS, and JavaScript. It's the face of the system where all the action happens.
- **Backend**: Powered by Java and Spring Boot. This is where the magic happens—handling all the data and logic.
- **Database**: MySQL keeps track of all the product info, like barcodes, names, and prices.

## Project Structure

Here's a quick peek at how everything is organized:

1. **Root Directory**: 
   - Contains the main configuration files like `pom.xml` for Maven and `.gitignore` to keep things tidy.

2. **Frontend (client)**:
   - **`src/`**: All the code that makes the user interface tick.
   - **`components/`**: Reusable bits of UI magic.
   - **`assets/`**: Images, stylesheets, and other goodies.
   - **`index.html`**: The main page where it all comes together.

3. **Backend (server)**:
   - **`src/`**: The brains of the operation, handling all the logic and data processing.
   - **`controllers/`**: Manages incoming requests and sends back responses.
   - **`models/`**: Defines the data structures we work with.
   - **`services/`**: Where the business logic lives.

4. **Shared**:
   - **`shared/`**: Any code that's used by both the frontend and backend, like utility functions.

5. **DevOps/CI-CD**:
   - **`.github/workflows/`**: Automates testing and deployment with GitHub Actions.

## The Big Picture

This project is a full-stack application, meaning we're covering everything from the user interface to the database. It's a great way to dive into modern web development and see how all the pieces fit together.

## Why Do It?

We're aiming to cut down on wait times and make shopping a breeze. Plus, it's a fun way to learn about building a complete application from scratch.

## The Team

We're a dynamic duo of student developers, Marven Drechsel and Lars Zieger, working together on both the frontend and backend aspects of the project. This project is part of our coursework for a lecture at DHBW Karlsruhe, where we're learning to integrate various technologies into a cohesive application. It's all about collaboration and learning as we tackle every part of the system together.

## Wrap-Up

In a nutshell, ScanMate 1.0.0-X "Aurora" is about creating a checkout experience that's fast, friendly, and accessible to everyone. Whether you're a developer looking to learn or a shopper wanting a better checkout, this project has something for you.
