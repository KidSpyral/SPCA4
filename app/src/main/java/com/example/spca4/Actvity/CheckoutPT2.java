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
import com.example.spca4.Model.CardDetails;
import com.example.spca4.Model.CardDetailsBuilder;
import com.example.spca4.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CheckoutPT2 extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    Button Continue;
    FirebaseAuth mAuth;
    String User;
    private EditText nameOnCard;
    private EditText cardNumber;
    private EditText day;
    private EditText month;
    private EditText cvv;
    private Context context;
    private DatabaseReference userCardDetailsReference; // Reference to the user's basket


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_pt2);

        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser().getUid();

        nameOnCard = findViewById(R.id.CardName);
        cardNumber = findViewById(R.id.CardNumber);
        day = findViewById(R.id.ExpDate);
        month = findViewById(R.id.ExpMonth);
        cvv = findViewById(R.id.CVV);


        Continue = findViewById(R.id.ConfirmDetails);

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setCardDetailsandPurchase();
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
                } else if (itemId == R.id.navigation_profile) {
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

    private void setCardDetailsandPurchase() {

        String textnameoncard = nameOnCard.getText().toString();
        String textcardnumber = cardNumber.getText().toString();
        String textday = day.getText().toString();
        String textmonth = month.getText().toString();
        String textcvv = cvv.getText().toString();

        if(TextUtils.isEmpty(textnameoncard)) {
            Toast.makeText(CheckoutPT2.this, "Enter Card Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(textcardnumber)) {
            Toast.makeText(CheckoutPT2.this, "Enter Card Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(textday)) {
            Toast.makeText(CheckoutPT2.this, "Enter Day", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(textmonth)) {
            Toast.makeText(CheckoutPT2.this, "Enter Month", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(textcvv)) {
            Toast.makeText(CheckoutPT2.this, "Enter CVV", Toast.LENGTH_SHORT).show();
            return;
        }
        if(textcardnumber.length() != 16) {
            Toast.makeText(CheckoutPT2.this, "Please Re-Enter Day, Day should be 2 Digits", Toast.LENGTH_SHORT).show();
            return;
        }
        if(textday.length() != 2) {
            Toast.makeText(CheckoutPT2.this, "Please Re-Enter Day, Day should be 2 Digits", Toast.LENGTH_SHORT).show();
            return;
        }
        if(textmonth.length() != 2) {
            Toast.makeText(CheckoutPT2.this, "Please Re-Enter Month, Month should be 2 Digits", Toast.LENGTH_SHORT).show();
            return;
        }
        if(textcvv.length() != 3) {
            Toast.makeText(CheckoutPT2.this, "Please Re-Enter your CVV, CVV should be 3 Digits", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userCardDetailsReference = FirebaseDatabase.getInstance().getReference().child("Registered Users").child(userId).child("Card Details");

            // Create a new AddressDetails object with the entered details
            CardDetails cardDetails = new CardDetailsBuilder()
                    .nameOnCard(textnameoncard)
                    .cardNumber(textcardnumber)
                    .day(textday)
                    .month(textmonth)
                    .cvv(textcvv)
                    .build();

            // Generate a unique key for the address
            String cardDetailsId = userCardDetailsReference.push().getKey();

            if (cardDetailsId != null) {
                // Add the address details to the user's address list
                userCardDetailsReference.child(cardDetailsId).setValue(cardDetails)
                        .addOnSuccessListener(aVoid -> {
                            // Show a toast indicating that the address is saved
                            Toast.makeText(this, "Card Details Saved", Toast.LENGTH_SHORT).show();
                            // If successfully added, navigate to the next activity
                            Intent intent = new Intent(getApplicationContext(), CheckoutPT2.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(context, "Failed to add card: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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
            Intent intent = new Intent(getApplicationContext(), Checkout.class);
            startActivity(intent);
            finish();        }
        return true;
    }
}