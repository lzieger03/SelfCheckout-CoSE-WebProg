package com.example.springbootapi.service;

import com.example.springbootapi.api.model.discount.Discount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
     * @return The Discount if found, otherwise null.
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

    /**
     * Retrieves all Discount objects from the database.
     *
     * @return A list of all Discounts.
     */
    private List<Discount> sqlGetAllDiscounts() {
        String url = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/discount_database.db";
        String query = "SELECT * FROM discounts";
        List<Discount> discounts = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                discounts.add(new Discount(
                        resultSet.getString(1),
                        resultSet.getDouble(2)
                ));
            }
        } catch (SQLException err) {
            logEvents(this.getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), err);
        }
        return discounts;
    }

    /**
     * Creates a new Discount in the database.
     *
     * @param code  The discount code.
     * @param discount_value The discount value.
     * @return True if the discount was created successfully, else false.
     */
    private boolean sqlCreateDiscount(String code, double discount_value) {
        String url = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/discount_database.db";
        String query = "INSERT INTO discounts (code, discount_value) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, code);
            statement.setDouble(2, discount_value);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException err) {
            logEvents(this.getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), err);
            return false;
        }
    }
    
    /**
     * Updates a discount in the database by its code.
     *
     * @param code The code of the discount to update.
     * @param discount_value The new discount value.
     * @return True if the discount was updated successfully, else false.
     */
    private boolean sqlUpdateDiscount(String code, double discount_value) {
        String url = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/discount_database.db";
        String query = "UPDATE discounts SET discount_value = ? WHERE code = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, discount_value);
            statement.setString(2, code);
            System.out.println(statement);
            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException err) {
            logEvents(this.getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), err);
        }
        return false;
    }

    /**
     * Deletes a discount from the database by its code.
     *
     * @param code The code of the discount to delete.
     * @return True if the discount was deleted successfully, else false.
     */ 
    private boolean sqlDeleteDiscount(String code) {
        String url = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/discount_database.db";
        String query = "DELETE FROM discounts WHERE code = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, code);
            return statement.executeUpdate() > 0;
        } catch (SQLException err) {
            logEvents(this.getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), err);
        }
        return false;
    }

    /**
     * Retrieves a Discount by its code.
     *
     * @param code The discount code.
     * @return An Optional containing the Discount if found, else empty.
     */
    public Optional<Discount> getDiscountByCode(String code) {
        return Optional.ofNullable(sqlGetDiscountByCode(code));
    }

    /**
     * Retrieves all Discounts.
     *
     * @return A list of all Discounts.
     */
    public List<Discount> getAllDiscounts() {
        return sqlGetAllDiscounts();
    }

    /**
     * Deletes a discount from the database by its code.
     *
     * @param code The code of the discount to delete.
     * @return True if the discount was deleted successfully, else false.
     */
    public boolean deleteDiscount(String code) {
        return sqlDeleteDiscount(code);
    }

    /**
     * Creates a new Discount in the database.
     *
     * @param code The discount code.
     * @param discount_value The discount value.
     * @return True if the discount was created successfully, else false.
     */
    public boolean createDiscount(String code, double discount_value) {
        return sqlCreateDiscount(code, discount_value);
    }

    /**
     * Updates a discount in the database by its code.
     *
     * @param code The code of the discount to update.
     * @param discount_value The new discount value.
     * @return True if the discount was updated successfully, else false.
     */
    public boolean updateDiscount(String code, double discount_value) {
        return sqlUpdateDiscount(code, discount_value);
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

}
