package com.example.spca4.Actvity;

// RegistrationFacade.java
import androidx.annotation.NonNull;

import com.example.spca4.Model.ReadWriteUserDetails;
import com.example.spca4.Model.UserDetailsBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationFacade {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public RegistrationFacade() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Registered Users");
    }

    public void registerUser(String email, String password, String name, String phoneNumber, String userType, final OnRegistrationListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            ReadWriteUserDetails writeUserDetails = new UserDetailsBuilder()
                                    .email(email)
                                    .password(password)
                                    .name(name)
                                    .phone(phoneNumber)
                                    .userType(userType)
                                    .build();


                            DatabaseReference userRef = mDatabase.child(currentUser.getUid());
                            userRef.setValue(writeUserDetails)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                listener.onRegistrationSuccess();
                                            } else {
                                                listener.onRegistrationFailure(task.getException());
                                            }
                                        }
                                    });
                        } else {
                            listener.onRegistrationFailure(task.getException());
                        }
                    }
                });
    }

    public interface OnRegistrationListener {
        void onRegistrationSuccess();
        void onRegistrationFailure(Exception exception);
    }
}
