# SelfCheckOut-CoSe-WebProg Project

## 1. Introduction  
The **Self-Checkout System** aims to modernize the checkout experience in a supermarket by offering customers a fast, efficient, and user-friendly alternative to traditional cashier checkouts. Customers will be able to scan items, select a payment method, and receive a printed receipt. The system is designed with different personas in mind, accommodating users of varying ages and technological abilities, ensuring broad usability.

This project is part of a full-stack application development exercise, integrating a simple user interface (UI), a Java-based backend, and a relational database.

---

## 2. Project Overview  
### 2.1 Objective  
The objective is to deliver a self-checkout system that reduces customer wait times and improves the checkout process. The system will allow for both cash and card payments, providing a seamless scanning experience, and enhancing customer satisfaction by addressing the needs of different user types.

### 2.2 User Personas  
The system is designed with the following personas in mind:
- **Cathrine Cashier**: Supervises the checkout process and assists customers.
- **Christian Childish**: Needs a simple, easy-to-follow process for buying items.
- **Adam Average**: Prefers an organized and methodical checkout experience.
- **Bertha Business**: Values speed and efficiency.
- **Olaf Oldie**: Requires larger text and a straightforward process.
- **Cathy Coupon**: Wants an easy way to use multiple coupons.

These personas ensure inclusivity, catering to various technical proficiencies and checkout preferences.

---

## 3. Requirements  
### 3.1 Functional Requirements  
- **Item Scanning**: Users can scan items via a barcode scanner. Each item will be listed with its name, price, and total.
- **Multiple Payment Options**: The system supports cash and card payments. A user-friendly interface guides customers through the payment process.
- **Receipt Printing**: After a successful payment, a receipt will automatically print, detailing all purchased items, the total cost, and the payment method.
- **Admin Features**: Cashiers or system admins can adjust settings to accommodate accessibility needs or fix system bugs.

### 3.2 Non-Functional Requirements  
- **Performance**: The system should handle up to 10 users simultaneously without delays, ensuring speedy checkouts.
- **Security**: Although no real payments are processed, the system must simulate secure payment interactions, adhering to GDPR data privacy guidelines.
- **Usability**: The interface must be intuitive, with support for accessibility features such as larger fonts and simplified navigation for elderly users.
- **Reliability**: All items must be accurately scanned and processed without data loss.

---

## 4. Architecture  
The project follows a simple three-layer architecture:
- **Frontend**: Built with HTML, CSS, and JavaScript, providing the user interface for item scanning, payment selection, and receipt display.
- **Backend**: A Java-based server handles business logic, including item data management, total calculation, and payment processing.
- **Database**: A MySQL database stores product information, including barcode, name, and price.

---

## 5. User Interface (UI) Design  
The UI will be designed to cater to the following:
- **Child-Friendly Design**: For Christian Childish, the interface will use large buttons and simple instructions.
- **Accessible Design**: For Olaf Oldie, the system will include large text, high-contrast themes, and simplified navigation.
- **Fast and Streamlined**: For Bertha Business, the interface will focus on speed, minimizing unnecessary steps.

The UI flow ensures that scanning, payment, and receipt printing are logically separated. Users can easily backtrack or restart transactions without confusion.

---

## 6. Data Model  
- **Product Table**: Stores details of each item, such as barcode, name, and price.
- **Transaction Table**: Logs each completed transaction, including item lists, total amount, and payment method.
- **Receipt Table**: Stores receipt details for audit purposes.

Data protection measures will ensure compliance with GDPR.

---

## 7. Processes  
Key processes include:
1. **Item Scanning Process**: When an item is scanned, the backend retrieves its details from the database, updating the total.
2. **Payment Process**: After scanning all items, the user selects a payment method (cash or card). The backend processes the payment and prints the receipt.

Sequence diagrams will illustrate these interactions.

---

## 8. Constraints  
- **Hardware**: The system must work with existing barcode scanners and receipt printers.
- **Budget**: The solution should be cost-effective.
- **Timeline**: The project must be completed before the academic deadline, limiting the scope to essential features.

---

## 9. Acceptance Criteria  
The project will be accepted if:
- Users can scan items, pay, and print receipts without errors.
- It addresses the needs of all user personas.
- It meets the specified functional and non-functional requirements.

---

## 10. Project Management  
The project will be managed with an agile approach:
1. **Setup**: Initialize the development environment and database.
2. **Frontend Development**: Implement UI and basic scanning functionality.
3. **Backend Integration**: Connect the UI with the backend.
4. **Testing**: Conduct thorough testing with all personas.
5. **Final Demo**: Present a working prototype.

The project team consists of 2-3 members, each responsible for specific parts (UI, backend, database).

---

## Conclusion  
This **Self-Checkout System** aims to create an inclusive, efficient, and modern checkout experience for supermarket customers. With clear personas, functional requirements, and a well-defined architecture, this project offers a valuable opportunity to practice full-stack application development.
