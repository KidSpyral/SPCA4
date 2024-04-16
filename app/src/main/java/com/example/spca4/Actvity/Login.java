package com.example.spca4.Actvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spca4.Model.ReadWriteUserDetails;
import com.example.spca4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    TextView NoAccountSignUp;
    Button LogIn;
    EditText email;
    EditText password;
    FirebaseAuth mAuth;
    Toolbar toolbar;
    private final static String TAG= "Login";

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(currentUser.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        ReadWriteUserDetails userDetails = dataSnapshot.getValue(ReadWriteUserDetails.class);
                        String userType = userDetails.getUserType();
                        // Check userType and navigate accordingly
                        if (userType.equals("Admin")) {
                            // User is an admin
                            Intent intent = new Intent(getApplicationContext(), Admin.class);
                            startActivity(intent);
                            finish();
                        } else if (userType.equals("User")) {
                            // User is a regular user
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Unknown user type, handle accordingly
                            // For example, redirect to a generic landing page
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                    Log.e(TAG, "Database error: " + databaseError.getMessage());
                    Toast.makeText(Login.this, "Database error. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        password = findViewById(R.id.password);
        email = findViewById(R.id.email);

        NoAccountSignUp = findViewById(R.id.NoAccountSignUp);
        NoAccountSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });

        LogIn = findViewById(R.id.LogIn);

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textEmailAddress = email.getText().toString();
                String textPassword = password.getText().toString();

                if(TextUtils.isEmpty(textEmailAddress)) {
                    Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(textEmailAddress, textPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // Retrieve user details from database to get userType
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(user.getUid());
                                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                ReadWriteUserDetails userDetails = dataSnapshot.getValue(ReadWriteUserDetails.class);
                                                String userType = userDetails.getUserType();
                                                // Check userType and navigate accordingly
                                                if (userType.equals("Admin")) {
                                                    Toast.makeText(Login.this, "LogIn Successful.",
                                                            Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), Admin.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(Login.this, "LogIn Successful.",
                                                            Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(intent);
                                                }
                                                finish();
                                            } else {
                                                // Handle the case where user data doesn't exist
                                                // This should ideally not happen if user registration is handled properly
                                                Toast.makeText(Login.this, "User data not found.", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Handle errors here
                                            Log.e(TAG, "Error fetching user data: " + databaseError.getMessage());
                                            Toast.makeText(Login.this, "Error fetching user data.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    // Handle login failures
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthInvalidUserException e) {
                                        email.setError("User does not exist or is no longer valid. Register again");
                                        email.requestFocus();
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        password.setError("Invalid details. Check and Re-Enter.");
                                        password.requestFocus();
                                    } catch (Exception e) {
                                        Log.e(TAG, e.getMessage());
                                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

            }
        });
    }


}