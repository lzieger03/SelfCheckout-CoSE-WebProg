package com.example.springbootapi.api.model.admin;

/**
 * Represents an Admin user with login credentials.
 */
public class Admin {
    private String id;
    private String login;
    private String password; // Hashed password

    public Admin(String id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
