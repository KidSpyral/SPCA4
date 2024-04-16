package com.example.spca4.Actvity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.spca4.Adapter.shopAdapter;
import com.example.spca4.Model.Items;
import com.example.spca4.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddNewItem.OnItemSavedListener {

    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    String User;
    private com.example.spca4.Adapter.shopAdapter shopAdapter;
    private List<Items> shopList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser().getUid();

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

        recyclerView = findViewById(R.id.RCV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        shopList = new ArrayList<>();
        shopAdapter = new shopAdapter(shopList, this);
        recyclerView.setAdapter(shopAdapter);
        fetchShopStock();
    }

    private void fetchShopStock() {
        DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("Stock");
        shopRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Items shopItem = snapshot.getValue(Items.class);
                    if (shopItem != null) {
                        // Log the shopItem object to check its values
                        Log.d("FirebaseData", "ShopItem: " + shopItem.toString());
                        shopList.add(shopItem);
                    }
                }
                Log.d("MainActivity", "ShopList size: " + shopList.size());
                shopAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error fetching stock", Toast.LENGTH_SHORT).show();
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
        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void onItemSaved(String description) {

    }
}