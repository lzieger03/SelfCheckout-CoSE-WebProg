package com.example.springbootapi.api.controller;

//import com.example.springbootapi.api.model.receipt.PrintReceiptRequest;
//import com.example.springbootapi.service.ReceiptService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.service.annotation.PostExchange;
//
//
///**
// * REST controller for handling receipt-related operations.
// * Provides endpoints to create and print receipts.
// */
//@RestController
//@CrossOrigin(origins = "http://127.0.0.1:5501") // Change this to the port of the frontend
//public class ReceiptController {
//    private static final Logger logger = LoggerFactory.getLogger(ReceiptController.class);
//
//    @Autowired
//    private ReceiptService receiptService;
//
//    /**
//     * Endpoint to create and print a receipt based on the provided cart and payment method.
//     *
//     * @param request The PrintReceiptRequest containing payment method and cart objects.
//     * @return A ResponseEntity containing a success message or an error message.
//     */
//    @PostExchange("/print")
//    public ResponseEntity<?> printReceipt(@RequestBody PrintReceiptRequest request){
//
//        try {
//            //Receipt receipt = receiptService.createReceipt(request);
//            //receiptService.print(receipt);
//            throw new RuntimeException("Bla");
//            //return true;// ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Printing successful"));
//        } catch (Exception err) {
//            logError("Error during receipt printing", err);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error during receipt printing"));
//        }
//    }
//
//    /**
//     * Logs an error message with the provided exception details.
//     *
//     * @param message The error message to log.
//     * @param e The exception that caused the error.
//     */
//    private void logError(String message, Exception e) {
//        logger.error("{}: {}", message, e.getMessage());
//    }
//
//    public static interface Response {
//        public String getMessage();
//    }
//
//    /**
//     * Inner class to structure error responses.
//     */
//    public static class ErrorResponse implements Response {
//        private String message;
//
//        public ErrorResponse(String message) {
//            this.message = message;
//        }
//
//        // Getter and Setter
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//    }
//
//    /**
//     * Inner class to structure success responses.
//     */
//    public static class SuccessResponse implements Response {
//        private String message;
//
//        public SuccessResponse(String message) {
//            this.message = message;
//        }
//
//        // Getter and Setter
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//    }
//}

