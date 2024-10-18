package com.example.springbootapi.service;

import com.example.springbootapi.api.model.discount.Discount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Optional;

/**
 * Service class responsible for handling discount-related operations.
 * Provides methods to retrieve discount information from the database.
 */
@Service
public class DiscountService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountService.class);

    /**
     * Retrieves a Discount object from the database by its code.
     *
     * @param id The code of the discount to retrieve.
     * @return An Optional containing the Discount if found, or empty if not found.
     */
    private Discount sqlGetDiscountByCode(String id) {
        String url = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/discount_database.db";
        String query = "SELECT * FROM discounts WHERE code = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Discount(
                            resultSet.getString(1),
                            resultSet.getDouble(2)
                    );
                }
            }
        } catch (SQLException err) {
            logEvents(this.getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), err);
        }
        return null;
    }

    public Optional<Discount> getDiscountByCode(String code) {
        return Optional.ofNullable(sqlGetDiscountByCode(code));
    }

    /**
     * Logs error events with class name, method name, and exception details.
     *
     * @param className The name of the class where the error occurred.
     * @param methodName The name of the method where the error occurred.
     * @param e The exception that was thrown.
     */
    public void logEvents(String className, String methodName, Exception e) {
        logger.error("Error occurred in Class {} in Method {}: {}",
                className,
                methodName,
                e.getMessage()
        );
    }

}
