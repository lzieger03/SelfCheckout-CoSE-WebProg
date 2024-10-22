package com.example.springbootapi.repository;

import com.example.springbootapi.api.model.user.Admin;
import com.example.springbootapi.api.model.user.Customer;
import com.example.springbootapi.api.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for User-related database operations.
 */
@Repository
public class UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private static final String DB_URL = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/user_database.db";

    private final CustomerRepository customerRepository = new CustomerRepository();
    private final AdminRepository adminRepository = new AdminRepository();

    /**
     * Retrieves a User by their username.
     *
     * @param username The user's username.
     * @return A User object if found, otherwise null.
     */
    public User findByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String role = resultSet.getString("role");
                    if ("ADMIN".equalsIgnoreCase(role)) {
                        // Admin specific retrieval handled in AdminRepository
                        return adminRepository.findAdminByUsername(username);
                    } else if ("CUSTOMER".equalsIgnoreCase(role)) {
                        // Retrieve customer-specific info
                        return customerRepository.findCustomerById(resultSet.getString("id"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching user by username {}: {}", username, e.getMessage());
        }
        return null;
    }

    /**
     * Adds a new User to the database.
     *
     * @param user The User object to add.
     * @return true if the user was added successfully, false otherwise.
     */
    public boolean addUser(User user) {
        String query = "INSERT INTO users (id, username, password, role) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getId());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            logger.error("Error adding user {}: {}", user.getUsername(), e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves a User by their ID.
     *
     * @param id The user's ID.
     * @return A User object if found, otherwise null.
     */
    public User findById(String id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String role = resultSet.getString("role");
                    if ("ADMIN".equalsIgnoreCase(role)) {
                        // Admin specific retrieval handled in AdminRepository
                        return adminRepository.findAdminById(resultSet.getString("id"));
                    } else if ("CUSTOMER".equalsIgnoreCase(role)) {
                        // Retrieve customer-specific info
                        return customerRepository.findCustomerById(resultSet.getString("id"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching user by ID {}: {}", id, e.getMessage());
        }
        return null;
    }

    /**
     * Updates an existing User in the database.
     *
     * @param user The User object with updated information.
     * @return true if the user was updated successfully, false otherwise.
     */
    public boolean updateUser(User user) {
        String updateUserQuery = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
        String updateCustomerQuery = "UPDATE customers SET first_name = ?, last_name = ?, email = ? WHERE user_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            connection.setAutoCommit(false); // Start transaction

            try (PreparedStatement userStmt = connection.prepareStatement(updateUserQuery)) {

                // Update users table
                userStmt.setString(1, user.getUsername());
                userStmt.setString(2, user.getPassword());
                userStmt.setString(3, user.getRole());
                userStmt.setString(4, user.getId());
                int userRows = userStmt.executeUpdate();

                boolean customerUpdated = true;
                if ("CUSTOMER".equalsIgnoreCase(user.getRole())) {
                    Customer customer = (Customer) user;
                    try (PreparedStatement customerStmt = connection.prepareStatement(updateCustomerQuery)) {
                        customerStmt.setString(1, customer.getFirstName());
                        customerStmt.setString(2, customer.getLastName());
                        customerStmt.setString(3, customer.getEmail());
                        customerStmt.setString(4, customer.getId());
                        customerUpdated = customerStmt.executeUpdate() > 0;
                    }
                }

                connection.commit(); // Commit transaction
                return userRows > 0 && customerUpdated;
            } catch (SQLException e) {
                connection.rollback(); // Rollback transaction on error
                logger.error("Error updating user {}: {}", user.getUsername(), e.getMessage());
            } finally {
                connection.setAutoCommit(true); // Restore default
            }
        } catch (SQLException e) {
            logger.error("Database connection error: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Deletes a User from the database by their ID.
     *
     * @param id The ID of the user to delete.
     * @return true if the user was deleted successfully, false otherwise.
     */
    public boolean deleteUser(String id) {
        String deleteUserQuery = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(deleteUserQuery)) {

            statement.setString(1, id);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            logger.error("Error deleting user with ID {}: {}", id, e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all User objects.
     */
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String role = resultSet.getString("role");
                if ("ADMIN".equalsIgnoreCase(role)) {
                    Admin admin = adminRepository.findAdminById(resultSet.getString("id"));
                    if (admin != null) {
                        users.add(admin);
                    } else {
                        // Admin retrieval handled in AdminRepository
                        users.add(new User(
                                resultSet.getString("id"),
                                resultSet.getString("username"),
                                resultSet.getString("password"),
                                role
                        ));
                    }
                } else if ("CUSTOMER".equalsIgnoreCase(role)) {
                    Customer customer = customerRepository.findCustomerById(resultSet.getString("id"));
                    if (customer != null) {
                        users.add(customer);
                    } else {
                        // Fallback in case customer-specific info is missing
                        users.add(new User(
                                resultSet.getString("id"),
                                resultSet.getString("username"),
                                resultSet.getString("password"),
                                role
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching all users: {}", e.getMessage());
        }
        return users;
    }
}
