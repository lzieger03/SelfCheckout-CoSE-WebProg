package com.example.springbootapi.repository;

import com.example.springbootapi.api.model.admin.Admin;
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
     * @param login The admin's login username.
     * @return An Admin object if found, otherwise null.
     */
    public Admin findByLogin(String login) {
        String query = "SELECT * FROM admins WHERE login = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Admin(
                            resultSet.getString("id"),
                            resultSet.getString("login"),
                            resultSet.getString("password") // Stored as hashed password
                    );
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching admin by login {}: {}", login, e.getMessage());
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
        String query = "INSERT INTO admins (id, login, password) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, admin.getId());
            statement.setString(2, admin.getLogin());
            statement.setString(3, admin.getPassword()); // Hashed password
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            logger.error("Error adding admin {}: {}", admin.getLogin(), e.getMessage());
        }
        return false;
    }
}
