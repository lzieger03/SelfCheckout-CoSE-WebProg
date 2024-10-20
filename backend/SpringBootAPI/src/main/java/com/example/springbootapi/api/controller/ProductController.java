package com.example.springbootapi.api.controller;

import com.example.springbootapi.api.model.product.Product;
import com.example.springbootapi.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for handling product-related operations.
 * Provides endpoints to retrieve and manage products.
 */
@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501") // Change this to the port of the frontend
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    /**
     * Constructs a new ProductController with the specified ProductService.
     *
     * @param productService The ProductService to be used for product operations.
     */
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieves a single product by its ID.
     *
     * @param id The product ID.
     * @return The product if found, otherwise a 404 status.
     */
    @GetMapping("/product") // Entrypoint of API https://api.url/product
    public ResponseEntity<Product> getProduct(@RequestParam String id) { // @RequestParam is the API Parameter e.g.: .../product?id=123
        try {
            Optional<Product> productOpt = productService.getProduct(id);
            if (productOpt.isPresent()) {
                return ResponseEntity.ok(productOpt.get());
            } else {
                return ResponseEntity.status(404).build();
            }
        } catch (Exception err) {
            logEvents(
                    this.getClass().getName(),
                    new Throwable().getStackTrace()[0].getMethodName(),
                    err
            );
            return ResponseEntity.status(500).body(new Product("1", err.getMessage(), 0.0, null));
        }
    }

    /**
     * Retrieves all products.
     *
     * @return A list of all products.
     */
    @GetMapping("/allproducts")
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            Optional<List<Product>> productsOpt = productService.getAllProducts();
            if (productsOpt.isPresent()) {
                return ResponseEntity.ok(productsOpt.get());
            } else {
                return ResponseEntity.status(404).build();
            }
        } catch (Exception err) {
            logEvents(
                    this.getClass().getName(),
                    new Throwable().getStackTrace()[0].getMethodName(),
                    err
            );
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Adds a new product with an image.
     *
     * @param productData The product data in JSON format.
     * @param image       The image file for the product.
     * @return The added product with a 201 status, or an error response.
     */
    @PostMapping("/addproduct")
    public ResponseEntity<?> addProduct(
            @RequestPart("product") Product productData,
            @RequestPart("image") MultipartFile image) {
        try {
            byte[] imageBytes = image.getBytes();
            productData.setItemImage(imageBytes);
            productService.addProduct(productData);
            return ResponseEntity.status(HttpStatus.CREATED).body(productData);
        } catch (IOException e) {
            logger.error("Error reading image file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Invalid image file."));
        } catch (Exception e) {
            logEvents(this.getClass().getName(),
                    new Throwable().getStackTrace()[0].getMethodName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to add product."));
        }
    }

    /**
     * Updates an existing product with a new image.
     *
     * @param productData The updated product data in JSON format.
     * @param image       The new image file for the product.
     * @return The updated product with a 200 status, or an error response.
     */
    @PutMapping("/updateproduct")
    public ResponseEntity<?> updateProduct(
            @RequestPart("product") Product productData,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            if (image != null && !image.isEmpty()) {
                byte[] imageBytes = image.getBytes();
                productData.setItemImage(imageBytes);
            } else {
                // No new image provided; existing image will remain
                productData.setItemImage(null);
            }
            productService.updateProduct(productData);
            return ResponseEntity.ok(productData);
        } catch (IOException e) {
            logger.error("Error reading image file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Invalid image file."));
        } catch (Exception e) {
            logEvents(this.getClass().getName(),
                    new Throwable().getStackTrace()[0].getMethodName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to update product."));
        }
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     * @return A confirmation message with a 200 status, or an error response.
     */
    @DeleteMapping("/deleteproduct")
    public ResponseEntity<?> deleteProduct(@RequestParam String id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse("Product deleted successfully."));
        } catch (Exception err) {
            logEvents(this.getClass().getName(),
                    new Throwable().getStackTrace()[0].getMethodName(), err);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to delete product."));
        }
    }

    /**
     * Retrieves the image of a product by its ID.
     *
     * @param id The ID of the product.
     * @return The image as a byte array with the appropriate content type.
     */
    @GetMapping("/product/image")
    public ResponseEntity<byte[]> getProductImage(@RequestParam String id) {
        Optional<Product> productOpt = productService.getProduct(id);
        if (productOpt.isPresent()) {
            byte[] imageBytes = productOpt.get().getItemImage();
            if (imageBytes != null && imageBytes.length > 0) {
                return ResponseEntity.ok()
                        .header("Content-Type", "image/png") // Adjust if using different image types
                        .body(imageBytes);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Logs error events with class name, method name, and exception details.
     *
     * @param className  The name of the class where the error occurred.
     * @param methodName The name of the method where the error occurred.
     * @param e          The exception that was thrown.
     */
    public void logEvents(String className, String methodName, Exception e) {
        logger.error("Error occurred in Class {} in Method {}: {}",
                className,
                methodName,
                e.getMessage()
        );
    }

    /**
     * Inner class to structure error responses.
     */
    public static class ErrorResponse {
        private String error;

        public ErrorResponse() {
        }

        public ErrorResponse(String error) {
            this.error = error;
        }

        // Getter and Setter

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }

    /**
     * Inner class to structure success responses.
     */
    public static class SuccessResponse {
        private String message;

        public SuccessResponse() {
        }

        public SuccessResponse(String message) {
            this.message = message;
        }

        // Getter and Setter

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
