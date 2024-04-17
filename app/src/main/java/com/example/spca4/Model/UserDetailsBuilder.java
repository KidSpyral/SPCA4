package com.example.spca4.Model;

import java.util.UUID;

public class UserDetailsBuilder {
    private String userId;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String userType;

    public UserDetailsBuilder() {}

    public UserDetailsBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserDetailsBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserDetailsBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserDetailsBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }

    public UserDetailsBuilder userType(String userType) {
        this.userType = userType;
        return this;
    }

    public UserDetailsBuilder userId(String userId) {
        this.userId = userId;
        return this;
    }

    public ReadWriteUserDetails build() {
        return new ReadWriteUserDetails(email, password, name, phone, userType);
    }


}