package com.example.spca4.Actvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spca4.Model.ReadWriteUserDetails;
import com.example.spca4.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {
    private List<ProfileObserver> observers = new ArrayList<>();
    private ProfileView profileView;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private String User;
    private TextView numbertextview;
    private TextView nametextview;
    private TextView emailtextview;
    private TextView userTypetextview;
    private DatabaseReference referenceProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser().getUid();

        numbertextview = findViewById(R.id.Phone);
        nametextview = findViewById(R.id.Name);
        emailtextview = findViewById(R.id.Email);
        userTypetextview = findViewById(R.id.Type);

        referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

        if (User == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            referenceProfile.child((User)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ReadWriteUserDetails userprofile = snapshot.getValue(ReadWriteUserDetails.class);
                    if (userprofile != null) {
                        String name = userprofile.getName();
                        String number = userprofile.getPhone();
                        String email = userprofile.getEmail();
                        String userType = userprofile.getUserType();

                        nametextview.setText(name);
                        numbertextview.setText(number);
                        emailtextview.setText(email);
                        userTypetextview.setText(userType);

                        // Notify observers of profile changes
                        notifyObservers(userprofile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Profile.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            });
        }

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

    // Method to register observers
    public void addObserver(ProfileObserver observer) {
        observers.add(observer);
    }

    // Method to remove observers
    public void removeObserver(ProfileObserver observer) {
        observers.remove(observer);
    }

    // Notify all observers when profile data changes
    private void notifyObservers(ReadWriteUserDetails userDetails) {
        for (ProfileObserver observer : observers) {
            observer.updateProfile(userDetails);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister observer to avoid memory leaks
        removeObserver(profileView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.logout) {
            mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        }
        return true;
    }
}
