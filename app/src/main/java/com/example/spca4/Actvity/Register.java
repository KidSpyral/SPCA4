package com.example.spca4.Actvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spca4.Model.ReadWriteUserDetails;
import com.example.spca4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    TextView NoAccountSignIn;
    Button Register;
    EditText email;
    EditText password;
    EditText phone;
    EditText name;
    FirebaseAuth mAuth;
    Spinner spinner;
    private final static String TAG= "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Spinner typeSpinner = findViewById(R.id.type);
        String[] types = {"Admin", "User"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        typeSpinner.setAdapter(adapter);

        NoAccountSignIn = findViewById(R.id.NoAccountSignIn);
        Register = findViewById(R.id.Register);

        mAuth = FirebaseAuth.getInstance();

        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        spinner = findViewById(R.id.type);

        NoAccountSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textEmailAddress = email.getText().toString();
                String textPassword = password.getText().toString();
                String textName = name.getText().toString();
                String textPhoneNumber = phone.getText().toString();
                String userType = spinner.getSelectedItem().toString(); // Get selected userType from spinner

                String mobileRegex = "08[0-9]{8}";
                Matcher mobilematcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobilematcher = mobilePattern.matcher(textPhoneNumber);

                if(TextUtils.isEmpty(textEmailAddress)) {
                    Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;

                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(textEmailAddress).matches()) {
                    Toast.makeText(Register.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(textName)) {
                    Toast.makeText(Register.this, "Enter your Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(textPhoneNumber)) {
                    Toast.makeText(Register.this, "Please Re-Enter your Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!mobilematcher.find()) {
                    Toast.makeText(Register.this, "Phone Number is not valid", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(textPhoneNumber.length() != 10) {
                    Toast.makeText(Register.this, "Please Re-Enter your Phone Number, Phone Number should be 10 Digits", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                    mAuth.createUserWithEmailAndPassword(textEmailAddress, textPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textEmailAddress, textPassword, textName, textPhoneNumber, userType);
                                        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

                                        referenceProfile.child(currentUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Register.this, "Registration Complete.", Toast.LENGTH_SHORT).show();
                                                    // Check userType and navigate accordingly
                                                    if (userType.equals("Admin")) {
                                                        Intent intent = new Intent(getApplicationContext(), Admin.class);
                                                        startActivity(intent);
                                                    } else {
                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                        startActivity(intent);
                                                    }
                                                    finish();
                                                }
                                            }
                                        });
                                    } else {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthWeakPasswordException e) {
                                            password.setError("Password too weak must contain a mix of letters, numbers & special characters.");
                                            password.requestFocus();
                                        } catch (FirebaseAuthInvalidCredentialsException e) {
                                            email.setError("Your email is invalid or already in use. Re-Enter Email.");
                                            email.requestFocus();
                                        }catch (FirebaseAuthUserCollisionException e) {
                                            email.setError("User is already registered. Use another Email.");
                                            email.requestFocus();
                                        } catch (Exception e) {
                                            Log.e(TAG, e.getMessage());
                                            Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
            }
        });
    }
}