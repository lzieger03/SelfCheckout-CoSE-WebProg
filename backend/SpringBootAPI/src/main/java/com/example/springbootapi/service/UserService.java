package com.example.springbootapi.service;

import com.example.springbootapi.api.model.user.User;
import com.example.springbootapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Service class responsible for User management and authentication.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Authenticates a user with the given username and password.
     *
     * @param username The user's username.
     * @param password The user's password in plain text.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        String hashedInputPassword = hashPassword(password);
        return user.getPassword().equals(hashedInputPassword);
    }

    /**
     * Adds a new user to the system.
     *
     * @param uid      The user's id.
     * @param username The user's username.
     * @param email    The user's email.
     * @param password The user's password in plain text.
     * @param role     The role of the user (e.g., "ADMIN", "USER").
     * @return true if the user was added successfully, false otherwise.
     */
    public boolean addUser(String uid, String username, String email, String password, String role) {
        String hashedPassword = hashPassword(password);
        String id = generateUniqueId(uid, role);
        User user = new User(id, username, email, hashedPassword, role);
        return userRepository.addUser(user);
    }

    /**
     * Updates an existing user.
     *
     * @param user The user object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateUser(User user) {
        // Optionally, hash the password only if it's being updated
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String hashedPassword = hashPassword(user.getPassword());
            user.setPassword(hashedPassword);
        }
        return userRepository.updateUser(user);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteUser(String id) {
        return userRepository.deleteUser(id);
    }

    /**
     * Retrieves all users.
     *
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAllUsers();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user.
     * @return The User object if found, null otherwise.
     */
    public User getUserById(String id) {
        return userRepository.findById(id);
    }

    /**
     * Generates a unique ID for a new user.
     *
     * @return A unique ID string.
     */
    private String generateUniqueId(String id, String role) {
        if("admin".equalsIgnoreCase(role)) {
            return "USER_ADMIN_" + id;
        }
        return "USER_" + id;
    }

    /**
     * Hashes a password using SHA-256.
     *
     * @param password The password to hash.
     * @return The hashed password as a hexadecimal string.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            // Convert byte array into hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password: " + e.getMessage());
        }
    }
}
