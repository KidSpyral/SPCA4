package com.example.spca4.Model;

import java.util.UUID;

public class ReadWriteUserDetails {
    private String email;
    private String password;
    private String name;
    private String phone;
    private String userType; // New field to store the user type (Admin or User)

    // Constructor
    public ReadWriteUserDetails(String email, String password, String name, String phone, String userType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.userType = userType;
    }

    // Default constructor
    public ReadWriteUserDetails(){

    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserType() {
        return userType;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
