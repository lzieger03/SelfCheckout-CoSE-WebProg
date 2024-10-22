package com.example.springbootapi.api.controller;

import com.example.springbootapi.repository.AdminRepository;
import com.example.springbootapi.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling admin login operations.
 */
@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501") // Adjust the origin as per your frontend
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(AdminRepository.class);

    @Autowired
    private AdminService adminService;

    /**
     * Handles POST requests for admin login.
     *
     * @param loginRequest An object containing login and password.
     * @return A ResponseEntity indicating the result of the authentication.
     */
    @PostMapping("/adminlogin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Login and password must be provided."));
        }

        boolean isAuthenticated = adminService.authenticate(username, password);
        if (isAuthenticated) {
            // In a real application, you might generate a session or a JWT token here
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid login or password."));
        }
    }

    /**
     * Inner class to map login requests.
     */
    public static class LoginRequest {
        private String username;
        private String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        // Getters and Setters

        public String getUsername() {
            return username;
        }

        public void setUsername(String login) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * Inner class to structure error responses.
     */
    public static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        // Getter

        public String getError() {
            return error;
        }

        // Setter

        public void setError(String error) {
            this.error = error;
        }
    }
}
