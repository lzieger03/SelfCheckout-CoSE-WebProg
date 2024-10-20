package com.example.springbootapi.service;

import com.example.springbootapi.api.model.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for handling product-related operations.
 * Provides methods to retrieve product information from the database.
 */
@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

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
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getDouble(3)
                    );
                }
            }
        } catch (SQLException err) {
            logEvents(this.getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), err);
        }
        return null;
    }

    private List<Product> getProductAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String url = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/item_database.db";
        String query = "SELECT * FROM products";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(new Product(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getDouble(3)
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
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return An Optional containing the Product if found, or empty if not found.
     */
    public Optional<Product> getProduct(String id) {
        return Optional.ofNullable(sqlGetProductByID(id));
    }
    public Optional<List<Product>> getAllProducts() {
        try {
            return Optional.ofNullable(getProductAll());
        } catch (SQLException e) {
            logEvents(this.getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), e);
        }
        return Optional.empty();
    }

    /**
     * Logs error events with class name, method name, and exception details.
     *
     * @param className The name of the class where the error occurred.
     * @param methodName The name of the method where the error occurred.
     * @param e The exception that was thrown.
     */
    private void logEvents(String className, String methodName, Exception e) {
        logger.error("Error occurred in Class {} in Method {}: {}", className, methodName, e.getMessage());
    }
}
