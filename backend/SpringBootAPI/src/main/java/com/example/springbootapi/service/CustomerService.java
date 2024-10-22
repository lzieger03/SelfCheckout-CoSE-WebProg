package com.example.springbootapi.service;

import com.example.springbootapi.api.model.user.Admin;
import com.example.springbootapi.api.model.user.Customer;
import com.example.springbootapi.repository.AdminRepository;
import com.example.springbootapi.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Service class responsible for Customer authentication and management.
 */
@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(AdminRepository.class);

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Authenticates a customer with the given login id and password.
     *
     * @param uid    The customer's login id.
     * @param password The customer's password in plain text.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean authenticate(String uid, String password) {
        Customer customer = customerRepository.findCustomerById(uid);
        if (customer == null) {
            return false;
        }
        String hashedInputPassword = hashPassword(password);
        return customer.getPassword().equals(hashedInputPassword);
    }

    /**
     * Hashes a password using SHA-256.
     *
     * @param password The plain text password.
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

    /**
     * Adds a new customer to the system.
     *
     * @param uid    The customer's login username.
     * @param username The customer's username.
     * @param password The customer's password in plain text.
     *
     * @param firstName The customer's first name.
     * @param lastName The customer's last name.
     * @param email The customer's email.
     * @return true if the customer was added successfully, false otherwise.
     */
    public boolean addCustomer(String uid, String  username, String password, String firstName, String lastName, String email) {
        String hashedPassword = hashPassword(password);
        Customer customer = new Customer(generateUniqueId(uid), username, hashedPassword, "CUSTOMER", firstName, lastName, email);
        return customerRepository.addCustomer(customer);
    }

    /**
     * Generates a unique ID for a new customer.
     * This is a simple implementation; consider using UUIDs or a more robust method.
     *
     * @return A unique ID string.
     */
    private String generateUniqueId(String uid) {
        // Simple unique ID generation (for demonstration purposes)
        return "CUSTOMER_" + uid;
    }
}
