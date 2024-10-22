package com.example.springbootapi.service;

import com.example.springbootapi.api.model.user.Admin;
import com.example.springbootapi.api.model.user.Customer;
import com.example.springbootapi.api.model.user.User;
import com.example.springbootapi.repository.AdminRepository;
import com.example.springbootapi.repository.CustomerRepository;
import com.example.springbootapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

/**
 * Service class responsible for User management and authentication.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AdminRepository adminRepository;

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
     * Adds a new Customer to the system.
     *
     * @param uid        The user's id.
     * @param username   The user's username.
     * @param password   The user's password in plain text.
     * @param role       The role of the user (should be "CUSTOMER").
     * @param firstName  The customer's first name.
     * @param lastName   The customer's last name.
     * @param email      The customer's email.
     * @return true if the customer was added successfully, false otherwise.
     */
    public boolean addCustomer(String uid, String username, String password, String role, String firstName, String lastName, String email) {
        String hashedPassword = hashPassword(password);
        String id = generateUniqueId(uid, "CUSTOMER");
        Customer customer = new Customer(id, username, hashedPassword, role, firstName, lastName, email);
        return customerRepository.addCustomer(customer);
    }

    /**
     * Adds a new Admin to the system.
     *
     * @param uid      The admin's id.
     * @param username The admin's username.
     * @param password The admin's password in plain text.
     * @return true if the admin was added successfully, false otherwise.
     */
    public boolean addAdmin(String uid, String username, String password) {
        String hashedPassword = hashPassword(password);
        String id = generateUniqueId(uid, "ADMIN");
        Admin admin = new Admin(id, username, hashedPassword);
        return adminRepository.addAdmin(admin);
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
     * @param uid   The base ID to use.
     * @param role  The role of the user ("ADMIN" or "CUSTOMER").
     * @return A unique ID string.
     */
    private String generateUniqueId(String uid, String role) {
        if ("ADMIN".equalsIgnoreCase(role)) {
            return "ADMIN_" + uid;
        }
        return "CUSTOMER_" + uid;
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
