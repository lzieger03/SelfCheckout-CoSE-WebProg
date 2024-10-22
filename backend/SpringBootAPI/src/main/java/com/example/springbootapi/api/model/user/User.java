package com.example.springbootapi.api.model.user;

/**
 * Base class representing a User.
 */
public class User {
    private final String id;
    private String username;
    private String password;
    private final String role;

    public User(String id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getId(){
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setPassword(String hashedPassword) {
        this.password = hashedPassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
