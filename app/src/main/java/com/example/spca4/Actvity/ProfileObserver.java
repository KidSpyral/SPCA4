package com.example.spca4.Actvity;

import com.example.spca4.Model.ReadWriteUserDetails;

public interface ProfileObserver {
    void updateProfile(ReadWriteUserDetails userDetails);
}