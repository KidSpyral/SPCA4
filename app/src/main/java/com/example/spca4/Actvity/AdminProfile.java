package com.example.spca4.Actvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spca4.Adapter.UsersAdapter;
import com.example.spca4.Model.ReadWriteUserDetails;
import com.example.spca4.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    String User;
    TextView numbertextview;
    TextView nametextview;
    TextView emailtextview;
    TextView userTypetextview;
    DatabaseReference referenceProfile;
    private com.example.spca4.Adapter.UsersAdapter UsersAdapter;
    private List<ReadWriteUserDetails> userDetailsList;
    private RecyclerView recyclerView, recyclerView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser().getUid();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);

        numbertextview = findViewById(R.id.Phone);
        nametextview = findViewById(R.id.Name);
        emailtextview = findViewById(R.id.Email);
        userTypetextview = findViewById(R.id.Type);

        referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

        if (User == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else
            referenceProfile.child((User)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ReadWriteUserDetails userprofile  = snapshot.getValue(ReadWriteUserDetails.class);
                    if (userprofile != null){
                        String name = userprofile.getName();
                        String number = userprofile.getPhone();
                        String email = userprofile.getEmail();
                        String userType = userprofile.getUserType();

                        nametextview.setText(name);
                        numbertextview.setText(number);
                        emailtextview.setText(email);
                        userTypetextview.setText(userType);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(AdminProfile.this, "Error!", Toast.LENGTH_SHORT).show();

                }
            });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    Intent intent = new Intent(getApplicationContext(), Admin.class);
                    startActivity(intent);
                    finish();
                }
                if (itemId == R.id.navigation_profile) {
                    Intent intent = new Intent(getApplicationContext(), AdminProfile.class);
                    startActivity(intent);
                    finish();
                } else if (itemId == R.id.navigation_checkout) {
                    Intent intent = new Intent(getApplicationContext(), AdminBasket.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
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
        if (itemId == R.id.logout){
            mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item2) {
        int itemId = item2.getItemId();
        if (itemId == R.id.Home){
            Intent intent = new Intent(getApplicationContext(), Admin.class);
            startActivity(intent);
            finish();        }
        if (itemId == R.id.Inventory){
            Intent intent = new Intent(getApplicationContext(), Admin.class);
            startActivity(intent);
            finish();        }
        else if (itemId == R.id.Customers) {
            Intent intent = new Intent(getApplicationContext(), Customers.class);
            startActivity(intent);
            finish();         }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}