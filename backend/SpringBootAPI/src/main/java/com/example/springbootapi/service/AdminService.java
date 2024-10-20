package com.example.springbootapi.service;

import com.example.springbootapi.api.model.admin.Admin;
import com.example.springbootapi.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Service class responsible for Admin authentication.
 */
@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository = new AdminRepository();

    /**
     * Authenticates an admin with the given login and password.
     *
     * @param login    The admin's login username.
     * @param password The admin's password.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean authenticate(String login, String password) {
        Admin admin = adminRepository.findByLogin(login);
        if (admin == null) {
            return false;
        }
        String hashedInputPassword = hashPassword(password);
        return admin.getPassword().equals(hashedInputPassword);
    }

    /**
     * Hashes a password using SHA-256.
     *
     * @param password The plain text password.
     * @return The hashed password as a hexadecimal string.
     */
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            // Convert byte array into signum representation
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
     * Adds a new admin to the system.
     *
     * @param login    The admin's login username.
     * @param password The admin's password in plain text.
     * @return true if the admin was added successfully, false otherwise.
     */
    public boolean addAdmin(String login, String password) {
        String hashedPassword = hashPassword(password);
        Admin admin = new Admin(generateUniqueId(), login, hashedPassword);
        return adminRepository.addAdmin(admin);
    }

    /**
     * Generates a unique ID for a new admin.
     * This is a simple implementation; consider using UUIDs or a more robust method.
     *
     * @return A unique ID string.
     */
    private String generateUniqueId() {
        // Simple unique ID generation (for demonstration purposes)
        return "ADMIN_" + (int)(System.currentTimeMillis() % 100000);
    }

//    public static void main(String[] args) {
//        AdminService adminService = new AdminService();
//        adminService.addAdmin("marven", "marven123");
//    }
}
