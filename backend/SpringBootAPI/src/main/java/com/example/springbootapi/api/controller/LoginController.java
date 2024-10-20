package com.example.springbootapi.api.controller;

import com.example.springbootapi.service.AdminService;
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

    @Autowired
    private AdminService adminService;

    /**
     * Handles POST requests for admin login.
     *
     * @param loginRequest An object containing login and password.
     * @return A ResponseEntity indicating the result of the authentication.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String login = loginRequest.getLogin();
        String password = loginRequest.getPassword();

        if (login == null || password == null || login.trim().isEmpty() || password.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Login and password must be provided."));
        }

        boolean isAuthenticated = adminService.authenticate(login, password);
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
        private String login;
        private String password;

        // Getters and Setters

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
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
