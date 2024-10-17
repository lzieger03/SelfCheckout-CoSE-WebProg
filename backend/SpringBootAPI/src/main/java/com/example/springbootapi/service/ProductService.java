package com.example.springbootapi.service;

import com.example.springbootapi.api.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

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

    public Optional<Product> getProduct(String id) {
        return Optional.ofNullable(sqlGetProductByID(id));
    }

    private void logEvents(String className, String methodName, Exception e) {
        logger.error("Error occurred in Class {} in Method {}: {}", className, methodName, e.getMessage());
    }
}
