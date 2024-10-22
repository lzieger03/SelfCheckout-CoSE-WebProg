package com.example.springbootapi.repository;

import com.example.springbootapi.api.model.user.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;

/**
 * Repository class for Admin-related database operations.
 */
@Repository
public class AdminRepository {

    private static final Logger logger = LoggerFactory.getLogger(AdminRepository.class);
    private static final String DB_URL = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/user_database.db";

    /**
     * Retrieves an Admin by their login.
     *
     * @param username The admin's login username.
     * @return An Admin object if found, otherwise null.
     */
    public Admin findAdminByUsername(String username) {
        String query = "SELECT u.id, u.username, u.password, a.user_id FROM users u INNER JOIN admins a ON u.id = a.user_id WHERE u.username = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Admin(
                            resultSet.getString("user_id"),
                            resultSet.getString("username"),
                            resultSet.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching admin by username {}: {}", username, e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves an Admin by their login.
     *
     * @param userId The admin's user id.
     * @return An Admin object if found, otherwise null.
     */
    public Admin findAdminById(String userId) {
        String query = "SELECT u.id, u.username, u.password, a.user_id FROM users u INNER JOIN admins a ON u.id = a.user_id WHERE a.user_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Admin(
                            resultSet.getString("user_id"),
                            resultSet.getString("username"),
                            resultSet.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching admin by ID {}: {}", userId, e.getMessage());
        }
        return null;
    }

    /**
     * Adds a new Admin to the database.
     *
     * @param admin The Admin object to add.
     * @return true if the admin was added successfully, false otherwise.
     */
    public boolean addAdmin(Admin admin) {
        String insertUserQuery = "INSERT INTO users (id, username, password, role) VALUES (?, ?, ?, ?)";
        String insertAdminQuery = "INSERT INTO admins (user_id) VALUES (?)";

        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            connection.setAutoCommit(false); // Start transaction

            try (PreparedStatement userStmt = connection.prepareStatement(insertUserQuery);
                 PreparedStatement adminStmt = connection.prepareStatement(insertAdminQuery)) {

                // Insert into users table
                userStmt.setString(1, admin.getId());
                userStmt.setString(2, admin.getUsername());
                userStmt.setString(3, admin.getPassword());
                userStmt.setString(4, admin.getRole());
                userStmt.executeUpdate();

                // Insert into admins table
                adminStmt.setString(1, admin.getId());
                adminStmt.executeUpdate();

                connection.commit(); // Commit transaction
                return true;
            } catch (SQLException e) {
                connection.rollback(); // Rollback transaction on error
                logger.error("Error adding admin {}: {}", admin.getUsername(), e.getMessage());
            } finally {
                connection.setAutoCommit(true); // Restore default
            }
        } catch (SQLException e) {
            logger.error("Database connection error: {}", e.getMessage());
        }
        return false;
    }
}
