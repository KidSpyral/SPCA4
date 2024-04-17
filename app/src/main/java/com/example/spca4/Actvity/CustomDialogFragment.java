package com.example.spca4.Actvity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.spca4.Model.Reviews;
import com.example.spca4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomDialogFragment extends DialogFragment {

    private EditText Rating, Comment;
    private Button save;
    FirebaseAuth mAuth;
    String User;
    private Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public static CustomDialogFragment newInstance() {
        return new CustomDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_item, container, false);

        Rating = view.findViewById(R.id.Rating);
        Comment = view.findViewById(R.id.Comment);
        save = view.findViewById(R.id.saveReview);

        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser().getUid();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String RT = Rating.getText().toString();
                final String CM = Comment.getText().toString();

                if(TextUtils.isEmpty(RT)) {
                    Toast.makeText(context, "Enter Rating", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(CM)) {
                    Toast.makeText(context, "Enter Comment", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    DatabaseReference stockRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId).child("Reviews");
                    Reviews reviews = new Reviews(RT,CM);
                    // Generate a unique key for the address
                    String reviewsId = stockRef.push().getKey();

                    if (reviewsId != null) {
                        // Add the address details to the user's address list
                        stockRef.child(reviewsId).setValue(reviews)
                                .addOnSuccessListener(aVoid -> {
                                    // Show a toast indicating that the address is saved
                                    Toast.makeText(context, "Review Saved", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                })
                                .addOnFailureListener(e -> Toast.makeText(context, "Failed to add review: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                } else {
                    Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show();

                }
            }
            });

        return view;
    }
}
