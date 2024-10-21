package com.example.springbootapi.api.controller;

import com.example.springbootapi.api.model.user.User;
import com.example.springbootapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling user-related operations.
 */
@RestController
@CrossOrigin(origins = "http://127.0.0.1:5501") // Adjust the origin as per your frontend
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Retrieves all users.
     *
     * @return A ResponseEntity containing the list of all users.
     */
    @GetMapping("/allusers")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user.
     * @return A ResponseEntity containing the user data if found, or a NOT_FOUND status.
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUserById(@RequestParam("id") String id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not found."));
        }
    }

    /**
     * Adds a new user.
     *
     * @param user The user data to add.
     * @return A ResponseEntity indicating the result of the operation.
     */
    @PostMapping("/adduser")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        boolean isAdded = userService.addUser(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getRole());
        if (isAdded) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("User added successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Failed to add user."));
        }
    }

    /**
     * Updates an existing user.
     *
     * @param user The user data to update.
     * @return A ResponseEntity indicating the result of the operation.
     */
    @PutMapping("/updateuser")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        boolean isUpdated = userService.updateUser(user);
        if (isUpdated) {
            return ResponseEntity.ok(new SuccessResponse("User updated successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Failed to update user."));
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @return A ResponseEntity indicating the result of the operation.
     */
    @DeleteMapping("/deleteuser")
    public ResponseEntity<?> deleteUser(@RequestParam("id") String id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.ok(new SuccessResponse("User deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Failed to delete user."));
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

        // Getter and Setter

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }

    /**
     * Inner class to structure success responses.
     */
    public static class SuccessResponse {
        private String message;

        public SuccessResponse(String message) {
            this.message = message;
        }

        // Getter and Setter

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
