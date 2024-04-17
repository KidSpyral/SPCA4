package com.example.spca4.Actvity;

import android.widget.TextView;

import com.example.spca4.Model.ReadWriteUserDetails;

public class ProfileView implements ProfileObserver {
    private TextView numbertextview;
    private TextView nametextview;
    private TextView emailtextview;
    private TextView userTypetextview;

    // Constructor to initialize UI components
    public ProfileView(TextView numbertextview, TextView nametextview, TextView emailtextview, TextView userTypetextview) {
        this.numbertextview = numbertextview;
        this.nametextview = nametextview;
        this.emailtextview = emailtextview;
        this.userTypetextview = userTypetextview;
    }

    @Override
    public void updateProfile(ReadWriteUserDetails userDetails) {
        if (userDetails != null) {
            String name = userDetails.getName();
            String number = userDetails.getPhone();
            String email = userDetails.getEmail();
            String userType = userDetails.getUserType();

            nametextview.setText(name);
            numbertextview.setText(number);
            emailtextview.setText(email);
            userTypetextview.setText(userType);

        }
    }
}
