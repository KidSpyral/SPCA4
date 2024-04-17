package com.example.spca4.Actvity;

import static android.content.ContentValues.TAG;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.spca4.Adapter.BasketAdapter;
import com.example.spca4.Adapter.PurchaseAdapter;
import com.example.spca4.Model.Basket;
import com.example.spca4.Model.Purchases;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PurchaseHistory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    String User;
    private com.example.spca4.Adapter.PurchaseAdapter PurchaseAdapter;
    private List<Purchases> purchasesList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

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

        recyclerView = findViewById(R.id.purchasesRCV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        purchasesList = new ArrayList<>();
        PurchaseAdapter = new PurchaseAdapter(purchasesList, this);
        recyclerView.setAdapter(PurchaseAdapter);

        // Receive user details from previous activity
        Intent intent = getIntent();
            ReadWriteUserDetails userDetails1 = (ReadWriteUserDetails) intent.getSerializableExtra("userDetails");
                String userId = userDetails1.getUserId(); // Get the user ID from userDetails
                // Now you have the correct userId, proceed with fetching the basket
                fetchBasket(userId);




    }

    private void fetchBasket(String userId) {
        DatabaseReference purchasesRef = FirebaseDatabase.getInstance().getReference()
                .child("Registered Users").child(userId).child("Purchases");
        purchasesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                purchasesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Purchases purchaseItem = snapshot.getValue(Purchases.class);
                    if (purchaseItem != null) {
                        purchasesList.add(purchaseItem);
                    }
                }
                PurchaseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PurchaseHistory.this, "Error fetching purchases", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(getApplicationContext(), Customers.class);
            startActivity(intent);
            finish();        }
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