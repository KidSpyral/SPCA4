package com.example.spca4.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.UUID;

public class ReadWriteUserDetails implements Serializable {
    private String userId;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String userType;

    // Private constructor to prevent direct instantiation
    public ReadWriteUserDetails(String email, String password, String name, String phone, String userType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.userType = userType;

        // Set userId to the Firebase UID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            this.userId = currentUser.getUid();
        } else {
            // Handle the case where the current user is null
            // For example, you can generate a random ID as a fallback
            this.userId = generateRandomId();
        }
    }

    // Default constructor
    public ReadWriteUserDetails(){

    }

    // Getters and setters for fields
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    // Method to generate a random ID (fallback if Firebase UID is not available)
    private String generateRandomId() {
        // Implement your logic to generate a random ID
        return UUID.randomUUID().toString();
    }
}
