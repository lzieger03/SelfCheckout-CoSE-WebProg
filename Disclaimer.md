# Disclaimer

## General

**ScanMate 1.0.0-X "Aurora"** is a student-led project developed as part of an academic curriculum at **Duale Hochschule Baden-Württemberg (DHBW) Karlsruhe**. This project serves as a proof of concept and is intended solely for educational evaluation. It is **not designed for production use** and should **not** be deployed in any real-world retail or commercial environment.

## Specific Disclaimers

### Security

- **Backend Security**: The backend infrastructure lacks robust security measures. It does **not** implement HTTPS, authentication, or authorization mechanisms. It is intended for **local-based use only** within a controlled educational setting.
  
- **Data Protection**: Sensitive data handling practices, such as encryption and secure storage, are not implemented. Users should avoid inputting any real or sensitive information.

### Database

- **Data Backup**: The SQLite database (`item_database.db`) used in this project does **not** have an automated backup system. Data persistence is limited to the local environment, and there are **no provisions** for remote backups or replication.
  
- **Remote Access**: The database is configured for local access only. Attempts to access it remotely without proper configuration may lead to data loss or corruption.

### Discounts

- **Discount System**: The discount feature is rudimentary and lacks validation against a secure, remote discount database. It is intended for **local testing purposes** and does **not** support real-world discount verification or application.

### Barcodes

- **Barcode Handling**: The barcode scanning and processing mechanisms are simplified and do **not** integrate with a secure, remote barcode database. Consequently, the system **cannot** verify the authenticity or accuracy of barcodes against a central repository.

### Payment

- **Payment Processing**: The payment module is a simulated feature and does **not** integrate with any real payment gateways or processors. All payment interactions are **mocked** for demonstration purposes and do **not** handle actual financial transactions.
  
- **Data Transmission**: Payment data is processed locally without encryption or secure transmission protocols, posing potential security risks if extended beyond the current scope.

### Receipts

- **Receipt Printing**: The receipt generation and printing functionalities are designed for local simulation. The system does **not** interface with actual receipt printers or external printing services.
  
- **Receipt Data**: Information printed on receipts is derived from local data and lacks integration with secure logging or transaction record-keeping systems.

### Accessibility

- **Accessibility Features**: While basic accessibility considerations have been included, **full accessibility support** is **not** implemented. Features such as screen reader compatibility, adjustable text sizes, and high-contrast themes may be incomplete or non-functional.
  
- **Future Improvements**: Accessibility enhancements are planned for future versions but are **not** present in the current iteration.

## Limitations

- **Performance**: Being an educational project, the application may not handle large datasets or high user traffic efficiently.
  
- **Scalability**: The system architecture is not optimized for scalability and may require significant modifications to support additional features or increased usage.

## Liability

The developers and DHBW Karlsruhe are **not liable** for any damages, losses, or issues arising from the use or misuse of this project. Users engage with this project at their own risk and discretion.

## Acknowledgements

We acknowledge the guidance and resources provided by DHBW Karlsruhe, which have been instrumental in the development of this project.
