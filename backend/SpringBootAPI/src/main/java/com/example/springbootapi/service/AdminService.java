package com.example.springbootapi.service;

import com.example.springbootapi.api.model.user.Admin;
import com.example.springbootapi.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Service class responsible for Admin authentication.
 */
@Service
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminRepository.class);

    @Autowired
    private AdminRepository adminRepository;

    /**
     * Authenticates an admin with the given login and password.
     *
     * @param username    The admin's login username.
     * @param password The admin's password in plain text.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean authenticate(String username, String password) {
        Admin admin = adminRepository.findAdminByUsername(username);
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
     * Adds a new admin to the system.
     *
     * @param uid
     * @param username    The admin's login username.
     * @param password The admin's password in plain text.
     * @return true if the admin was added successfully, false otherwise.
     */
    public boolean addAdmin(String uid, String username, String password) {
        String hashedPassword = hashPassword(password);
        Admin admin = new Admin(generateUniqueId(uid), username, hashedPassword);
        return adminRepository.addAdmin(admin);
    }

    /**
     * Generates a unique ID for a new admin.
     * This is a simple implementation; consider using UUIDs or a more robust method.
     *
     * @return A unique ID string.
     */
    private String generateUniqueId(String uid) {
        // Simple unique ID generation (for demonstration purposes)
        return "ADMIN_" + uid;
    }

//    public static void main(String[] args) {
//        AdminService adminService = new AdminService();
//        adminService.addAdmin("marven", "marven123");
//    }
}
