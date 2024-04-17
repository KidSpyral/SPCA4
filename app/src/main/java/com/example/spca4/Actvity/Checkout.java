package com.example.spca4.Actvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spca4.Model.AddressDetails;
import com.example.spca4.Model.AddressDetailsBuilder;
import com.example.spca4.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Checkout extends AppCompatActivity {


    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    Button SelectAddress;
    FirebaseAuth mAuth;
    String User;

    private EditText fullname;
    private EditText phonenumber;
    private EditText postcode;
    private EditText line1;
    private EditText line2;
    private EditText city;
    private Context context;
    private DatabaseReference userAddressReference; // Reference to the user's basket

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser().getUid();

        fullname = findViewById(R.id.Fullname);
        phonenumber = findViewById(R.id.Number);
        postcode = findViewById(R.id.PostCode);
        line1 = findViewById(R.id.Line1);
        line2 = findViewById(R.id.Line2);
        city = findViewById(R.id.TownCity);

        SelectAddress = findViewById(R.id.SelectAddress);

        SelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setAddress();

            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } if (itemId == R.id.navigation_profile) {
                    Intent intent = new Intent(getApplicationContext(), Profile.class);
                    startActivity(intent);
                    finish();
                } else if (itemId == R.id.navigation_checkout) {
                    Intent intent = new Intent(getApplicationContext(), Basket.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

    }

    private void setAddress() {

        String textfullname = fullname.getText().toString();
        String textphonenumber = phonenumber.getText().toString();
        String textpostcode = postcode.getText().toString();
        String textline1 = line1.getText().toString();
        String textline2 = line2.getText().toString();
        String textcity = city.getText().toString();

        String mobileRegex = "08[0-9]{8}";
        Matcher mobilematcher;
        Pattern mobilePattern = Pattern.compile(mobileRegex);
        mobilematcher = mobilePattern.matcher(textphonenumber);



        if(TextUtils.isEmpty(textfullname)) {
            Toast.makeText(Checkout.this, "Enter Full Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(textphonenumber)) {
            Toast.makeText(Checkout.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(textpostcode)) {
            Toast.makeText(Checkout.this, "Enter Post Code", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(textline1)) {
            Toast.makeText(Checkout.this, "Enter Address Line 1", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(textline2)) {
            Toast.makeText(Checkout.this, "Enter Address Line 2", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(textcity)) {
            Toast.makeText(Checkout.this, "Enter City/Town", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!mobilematcher.find()) {
            Toast.makeText(Checkout.this, "Phone Number is not valid", Toast.LENGTH_SHORT).show();
            return;
        }
        if(textphonenumber.length() != 10) {
            Toast.makeText(Checkout.this, "Please Re-Enter your Phone Number, Phone Number should be 10 Digits", Toast.LENGTH_SHORT).show();
            return;
        }



        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userAddressReference = FirebaseDatabase.getInstance().getReference().child("Registered Users").child(userId).child("Address Details");

            // Create a new AddressDetails object with the entered details
            AddressDetails addressItem = new AddressDetailsBuilder()
                    .fullname(textfullname)
                    .phonenumber(textphonenumber)
                    .postcode(textpostcode)
                    .line1(textline1)
                    .line2(textline2)
                    .city(textcity)
                    .build();


            // Generate a unique key for the address
            String addressId = userAddressReference.push().getKey();

            if (addressId != null) {
                // Add the address details to the user's address list
                userAddressReference.child(addressId).setValue(addressItem)
                        .addOnSuccessListener(aVoid -> {
                            // Show a toast indicating that the address is saved
                            Toast.makeText(this, "Address Saved", Toast.LENGTH_SHORT).show();
                            // If successfully added, navigate to the next activity
                            Intent intent = new Intent(getApplicationContext(), CheckoutPT2.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(context, "Failed to add address: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        } else {
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show();
        }

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
            Intent intent = new Intent(getApplicationContext(), Basket.class);
            startActivity(intent);
            finish();        }
        return true;
    }
}