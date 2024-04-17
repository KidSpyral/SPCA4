package com.example.spca4.Actvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private TextView NoAccountSignIn;
    private Button Register;
    private EditText email;
    private EditText password;
    private EditText phone;
    private EditText name;
    private FirebaseAuth mAuth;
    private Spinner spinner;
    private RegistrationFacade registrationFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        registrationFacade = new RegistrationFacade();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        name = findViewById(R.id.name);
        spinner = findViewById(R.id.type);

        String[] types = {"Admin", "User"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        NoAccountSignIn = findViewById(R.id.NoAccountSignIn);
        Register = findViewById(R.id.Register);

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
                String userType = spinner.getSelectedItem().toString();

                String mobileRegex = "08[0-9]{8}";
                Matcher mobilematcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobilematcher = mobilePattern.matcher(textPhoneNumber);

                if (TextUtils.isEmpty(textEmailAddress)) {
                    Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmailAddress).matches()) {
                    Toast.makeText(Register.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(textName)) {
                    Toast.makeText(Register.this, "Enter your Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(textPhoneNumber)) {
                    Toast.makeText(Register.this, "Please Re-Enter your Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!mobilematcher.find()) {
                    Toast.makeText(Register.this, "Phone Number is not valid", Toast.LENGTH_SHORT).show();
                    return;
                } else if (textPhoneNumber.length() != 10) {
                    Toast.makeText(Register.this, "Please Re-Enter your Phone Number, Phone Number should be 10 Digits", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    registrationFacade.registerUser(textEmailAddress, textPassword, textName, textPhoneNumber, userType, new RegistrationFacade.OnRegistrationListener() {
                        @Override
                        public void onRegistrationSuccess() {
                            Toast.makeText(Register.this, "Registration Complete.", Toast.LENGTH_SHORT).show();
                            if (userType.equals("Admin")) {
                                startActivity(new Intent(getApplicationContext(), Admin.class));
                            } else {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                            finish();
                        }

                        @Override
                        public void onRegistrationFailure(Exception exception) {
                            try {
                                throw exception;
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                password.setError("Password too weak must contain a mix of letters, numbers & special characters.");
                                password.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                email.setError("User is already registered. Use another Email.");
                                email.requestFocus();
                            } catch (Exception e) {
                                Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}