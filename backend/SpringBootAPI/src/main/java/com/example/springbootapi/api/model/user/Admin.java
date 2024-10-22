package com.example.springbootapi.api.model.user;

/**
 * Represents an Admin user with login credentials.
 */
public class Admin extends User {

    public Admin(String id, String username, String password) {
        super(id, username, password, "ADMIN");
    }

    // Admin-specific methods can be added here
}
