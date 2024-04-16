package com.example.spca4.Actvity;

public class UserTypeChecker {

    public static boolean isAdmin(String uid) {
        // Check if the UID starts with the admin prefix
        return uid.startsWith("admin_");
    }

    public static boolean isUser(String uid) {
        // Check if the UID starts with the admin prefix
        return uid.startsWith("user_");
    }

}
