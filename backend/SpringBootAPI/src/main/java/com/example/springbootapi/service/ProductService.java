package com.example.springbootapi.service;

import com.example.springbootapi.api.model.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for handling product-related operations.
 * Provides methods to retrieve and manage products from the database.
 */
@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    // Public Methods

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return An Optional containing the Product if found, or empty if not found.
     */
    public Optional<Product> getProduct(String id) {
        return Optional.ofNullable(sqlGetProductByID(id));
    }

    /**
     * Retrieves all products.
     *
     * @return An Optional containing a List of Products, or empty if an error occurs.
     */
    public Optional<List<Product>> getAllProducts() {
        try {
            return Optional.ofNullable(getProductAll());
        } catch (SQLException e) {
            logEvents(this.getClass().getName(),
                    new Throwable().getStackTrace()[0].getMethodName(), e);
        }
        return Optional.empty();
    }

    /**
     * Adds a new product to the database.
     *
     * @param product The Product object to add.
     */
    public void addProduct(Product product) {
        sqlAddProduct(product);
    }

    /**
     * Updates an existing product in the database.
     *
     * @param product The Product object to update.
     */
    public void updateProduct(Product product) {
        // If no new image is provided, retrieve the existing image
        if (product.getItemImage() == null) {
            Product existingProduct = getProduct(product.getId()).get();
            product.setItemImage(existingProduct.getItemImage());
        }
        sqlUpdateProduct(product);
    }

    /**
     * Deletes a product from the database by its ID.
     *
     * @param id The ID of the product to delete.
     */
    public void deleteProduct(String id) {
        sqlDeleteProduct(id);
    }


    // Private Methods

    /**
     * Retrieves a Product object from the database by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return A Product object if found, otherwise null.
     */
    private Product sqlGetProductByID(String id) {
        String url = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/item_database.db";
        String query = "SELECT * FROM products WHERE ItemNo = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Product(
                            resultSet.getString("ItemNo"),
                            resultSet.getString("ItemName"),
                            resultSet.getDouble("ItemPrice"),
                            resultSet.getBytes("ItemImage") // Retrieve image as byte array
                    );
                }
            }
        } catch (SQLException err) {
            logEvents(this.getClass().getName(),
                    new Throwable().getStackTrace()[0].getMethodName(), err);
        }
        return null;
    }

    /**
     * Retrieves all products from the database.
     *
     * @return A List of Product objects.
     * @throws SQLException If an error occurs while retrieving the products.
     */
    private List<Product> getProductAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String url = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/item_database.db";
        String query = "SELECT * FROM products ORDER BY ItemName ASC";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(new Product(
                            resultSet.getString("ItemNo"),
                            resultSet.getString("ItemName"),
                            resultSet.getDouble("ItemPrice"),
                            resultSet.getBytes("ItemImage")
                    ));
                }
                return products;
            }
        } catch (Exception err) {
            logEvents(this.getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), err);
        }
        return null;
    }

    /**
     * Inserts a new product into the database.
     *
     * @param product The Product to insert.
     */
    private void sqlAddProduct(Product product) {
        String url = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/item_database.db";
        String query = "INSERT OR IGNORE INTO products (ItemNo, ItemName, ItemPrice, ItemImage) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getId());
            statement.setString(2, product.getName());
            statement.setDouble(3, product.getPrice());
            statement.setBytes(4, product.getItemImage());
            statement.executeUpdate();
        } catch (SQLException err) {
            logEvents(this.getClass().getName(),
                    new Throwable().getStackTrace()[0].getMethodName(), err);
        }
    }

    /**
     * Updates an existing product in the database.
     *
     * @param product The Product to update.
     */
    private void sqlUpdateProduct(Product product) {
        String url = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/item_database.db";
        String query = "UPDATE products SET ItemName = ?, ItemPrice = ?, ItemImage = ? WHERE ItemNo = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setBytes(3, product.getItemImage());
            statement.setString(4, product.getId());
            statement.executeUpdate();
        } catch (SQLException err) {
            logEvents(this.getClass().getName(),
                    new Throwable().getStackTrace()[0].getMethodName(), err);
        }
    }

    /**
     * Deletes a product from the database.
     *
     * @param id The ID of the product to delete.
     */
    private void sqlDeleteProduct(String id) {
        String url = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/item_database.db";
        String query = "DELETE FROM products WHERE ItemNo = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException err) {
            logEvents(this.getClass().getName(),
                    new Throwable().getStackTrace()[0].getMethodName(), err);
        }
    }

    /**
     * Logs error events with class name, method name, and exception details.
     *
     * @param className  The name of the class where the error occurred.
     * @param methodName The name of the method where the error occurred.
     * @param e          The exception that was thrown.
     */
    private void logEvents(String className, String methodName, Exception e) {
        logger.error("Error occurred in Class {} in Method {}: {}",
                className,
                methodName,
                e.getMessage());
    }

//    public static void main(String[] args) {
//        // put in every product image in the database
//        ProductService productService = new ProductService();
//        Optional<List<Product>> products = productService.getAllProducts();
//
//        // the database row is empty, so we need to put in every product image in the database
//        // so that the frontend can display the images, the images are in the resources/images folder
//        // the images are named after the product id in png format
//
//        for (Product product : products.get()) {
//            String imagePath = "./src/main/resources/itemPictures/" + product.getId() + ".png";
//            File imageFile = new File(imagePath);
//            byte[] imageBytes = null;
//            try {
//                imageBytes = Files.readAllBytes(imageFile.toPath());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            product.setItemImage(imageBytes);
//            productService.updateProduct(product);
//        }
//    }
}
