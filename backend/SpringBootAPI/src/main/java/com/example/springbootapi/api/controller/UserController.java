package com.example.springbootapi.api.controller;

import com.example.springbootapi.api.model.user.User;
import com.example.springbootapi.service.AdminService;
import com.example.springbootapi.service.CustomerService;
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
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AdminService adminService;

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
     * Adds a new Customer.
     *
     * @param customer The Customer object to add.
     * @return A ResponseEntity indicating the result of the operation.
     */
    @PostMapping("/addcustomer")
    public ResponseEntity<?> addCustomer(@RequestBody CustomerRequest customer) {
        boolean isAdded = customerService.addCustomer(
                customer.getUid(),
                customer.getUsername(),
                customer.getPassword(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail()
        );
        if (isAdded) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Customer added successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Failed to add customer."));
        }
    }

    /**
     * Adds a new Admin.
     *
     * @param admin The Admin object to add.
     * @return A ResponseEntity indicating the result of the operation.
     */
    @PostMapping("/addadmin")
    public ResponseEntity<?> addAdmin(@RequestBody AdminRequest admin) {
        boolean isAdded = adminService.addAdmin(
                admin.getUid(),
                admin.getUsername(),
                admin.getPassword()
        );
        if (isAdded) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Admin added successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Failed to add admin."));
        }
    }

    /**
     * Updates an existing user.
     *
     * @param user The User object with updated information.
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

    /**
     * Request class for adding a Customer.
     */
    public static class CustomerRequest {
        private String uid;
        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private String email;

        // Getters and Setters

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    /**
     * Request class for adding an Admin.
     */
    public static class AdminRequest {
        private String uid;
        private String username;
        private String password;

        // Getters and Setters

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
