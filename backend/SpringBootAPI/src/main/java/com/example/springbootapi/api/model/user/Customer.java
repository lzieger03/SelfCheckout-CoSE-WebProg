package com.example.springbootapi.api.model.user;

/**
 * Represents a Customer with personal details.
 */
public class Customer extends User {
    private String firstName;
    private String lastName;
    private String email;

    public Customer(String id, String username, String password, String role, String firstName, String lastName, String email) {
        super(id, username, password, role);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    
    public String getEmail() {
        return email;
    }

    // Setters if needed
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
