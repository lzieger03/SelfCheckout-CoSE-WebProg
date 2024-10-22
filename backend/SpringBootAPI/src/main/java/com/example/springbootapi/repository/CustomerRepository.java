package com.example.springbootapi.repository;

import com.example.springbootapi.api.model.user.Admin;
import com.example.springbootapi.api.model.user.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;

/**
 * Repository class for Customer-related database operations.
 */
@Repository
public class CustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(CustomerRepository.class);
    private static final String DB_URL = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/user_database.db";


    /**
     * Adds a new Customer to the database.
     *
     * @param customer The Customer object to add.
     * @return true if the customer was added successfully, false otherwise.
     */
    public boolean addCustomer(Customer customer) {
        String insertUserQuery = "INSERT INTO users (id, username, password, role) VALUES (?, ?, ?, ?)";
        String insertCustomerQuery = "INSERT INTO customers (user_id, first_name, last_name, email) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            connection.setAutoCommit(false); // Start transaction

            try (PreparedStatement userStmt = connection.prepareStatement(insertUserQuery);
                 PreparedStatement customerStmt = connection.prepareStatement(insertCustomerQuery)) {

                // Insert into users table
                userStmt.setString(1, customer.getId());
                userStmt.setString(2, customer.getUsername());
                userStmt.setString(3, customer.getPassword());
                userStmt.setString(4, customer.getRole());
                userStmt.executeUpdate();

                // Insert into customers table
                customerStmt.setString(1, customer.getId());
                customerStmt.setString(2, customer.getFirstName());
                customerStmt.setString(3, customer.getLastName());
                customerStmt.setString(4, customer.getEmail());
                customerStmt.executeUpdate();

                connection.commit(); // Commit transaction
                return true;
            } catch (SQLException e) {
                connection.rollback(); // Rollback transaction on error
                logger.error("Error adding customer {}: {}", customer.getUsername(), e.getMessage());
            } finally {
                connection.setAutoCommit(true); // Restore default
            }
        } catch (SQLException e) {
            logger.error("Database connection error: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves a Customer by their ID.
     *
     * @param id The customer's ID.
     * @return A Customer object if found, otherwise null.
     */
    public Customer findCustomerById(String id) {
        String query = "SELECT * FROM users u INNER JOIN customers c ON u.id = c.user_id WHERE c.user_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Customer(
                            resultSet.getString("user_id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            "CUSTOMER",
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching customer by ID {}: {}", id, e.getMessage());
        }
        return null;
    }

    /**
     * Updates an existing Customer in the database.
     *
     * @param customer The Customer object with updated information.
     * @return true if the customer was updated successfully, false otherwise.
     */
    public boolean updateCustomer(Customer customer) {
        String query = "UPDATE customers SET first_name = ?, last_name = ?, email = ? WHERE user_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getId());
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            logger.error("Error updating customer {}: {}", customer.getUsername(), e.getMessage());
        }
        return false;
    }

    /**
     * Deletes a Customer from the database by their user ID.
     *
     * @param userId The user ID of the customer to delete.
     * @return true if the customer was deleted successfully, false otherwise.
     */
    public boolean deleteCustomer(String userId) {
        String query = "DELETE FROM customers WHERE user_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, userId);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            logger.error("Error deleting customer with user ID {}: {}", userId, e.getMessage());
        }
        return false;
    }
}
