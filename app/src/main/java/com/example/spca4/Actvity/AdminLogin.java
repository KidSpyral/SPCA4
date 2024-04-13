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

public class AdminLogin extends AppCompatActivity {

    TextView NoAccountSignUp;
    Button LogIn;
    EditText email;
    EditText password;
    EditText code;

    FirebaseAuth AdminmAuth;
    Toolbar toolbar;
    private final static String TAG= "Login";

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentAdmin = AdminmAuth.getCurrentUser();
        if(currentAdmin != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AdminmAuth = FirebaseAuth.getInstance();

        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        code = findViewById(R.id.code);

        NoAccountSignUp = findViewById(R.id.NoAccountSignUp);
        NoAccountSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminRegister.class);
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
                String textCode = code.getText().toString();

                if(TextUtils.isEmpty(textEmailAddress)) {
                    Toast.makeText(AdminLogin.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(AdminLogin.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(textCode)) {
                    Toast.makeText(AdminLogin.this, "Enter Admin Code", Toast.LENGTH_SHORT).show();
                    return;
                }

                AdminmAuth.signInWithEmailAndPassword(textEmailAddress, textPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser Admin = AdminmAuth.getCurrentUser();
                                    if (Admin != null) {
                                        // User exists, retrieve user data to get the admin code
                                        DatabaseReference AdminRef = FirebaseDatabase.getInstance().getReference().child("Registered Admins").child(Admin.getUid());
                                        AdminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                // Check if the user data exists
                                                if (dataSnapshot.exists()) {
                                                    // Retrieve the admin code from user data
                                                    String adminCode = dataSnapshot.child("code").getValue(String.class);
                                                    if (adminCode != null && adminCode.equals(textCode)) {
                                                        // Admin code matches, proceed with login
                                                        Toast.makeText(AdminLogin.this, "LogIn Successful.",
                                                                Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplicationContext(), Admin.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        // Admin code does not match
                                                        Toast.makeText(AdminLogin.this, "Incorrect Admin Code.",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    // User data does not exist
                                                    Toast.makeText(AdminLogin.this, "Admin data not found.",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                // Handle database error
                                                Log.e(TAG, "Database error: " + databaseError.getMessage());
                                                Toast.makeText(AdminLogin.this, "Database error. Please try again.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        // User does not exist or is no longer valid
                                        password.setError("Admin does not exist or is no longer valid. Register again");
                                        password.requestFocus();
                                    }
                                } else {
                                    // Handle sign-in failures
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthInvalidUserException e) {
                                        password.setError("Admin does not exist or is no longer valid. Register again");
                                        password.requestFocus();
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        email.setError("Invalid details. Check and Re-Enter.");
                                        email.requestFocus();
                                    } catch (Exception e) {
                                        Log.e(TAG, e.getMessage());
                                        Toast.makeText(AdminLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.backButton){
            Intent intent = new Intent(getApplicationContext(), Landing.class);
            startActivity(intent);
            finish();        }
        return true;
    }
}