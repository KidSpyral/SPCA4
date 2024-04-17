package com.example.spca4.Actvity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements AddNewItem.OnItemSavedListener, com.example.spca4.Adapter.shopAdapter.OnItemClickListener {

    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    String User;
    private com.example.spca4.Adapter.shopAdapter shopAdapter;
    private List<Items> shopList;
    private RecyclerView recyclerView;
    public static MainActivity newInstance(){
        return new MainActivity();
    }


    public void onItemClick(int position) {
        DialogFactory factory = new ReviewDialogFactory(); // Create a factory instance
        CustomDialogFragment dialog = factory.createDialog(); // Use the factory to create a dialog
        dialog.show(getSupportFragmentManager(), "CustomDialog");
    }

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
        shopAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(shopAdapter);
        fetchShopStock("");
        fetchUniqueTitles();
    }

    private void fetchUniqueTitles() {
        DatabaseReference stockRef = FirebaseDatabase.getInstance().getReference("Stock");
        stockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> uniqueTitles = new HashSet<>();

                uniqueTitles.add("");

                for (DataSnapshot entrySnapshot : snapshot.getChildren()) {
                    Items entry = entrySnapshot.getValue(Items.class);
                    if (entry != null) {
                        uniqueTitles.add(entry.getTitle());
                    }
                }
                // Now, 'uniqueMoods' set contains unique mood entries
                // Populate the spinner with these entries
                populateSpinner(uniqueTitles);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void populateSpinner(Set<String> uniqueTitles) {
        // Convert set to list for spinner adapter
        List<String> titleList = new ArrayList<>(uniqueTitles);

        // Create an ArrayAdapter and set it to the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, titleList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner titleSpinner = findViewById(R.id.shopFilter);
        titleSpinner.setAdapter(adapter);

        titleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedTitle = titleList.get(position);
                if (selectedTitle.isEmpty()) {
                    // Handle the case where no mood is selected
                    fetchShopStock("");
                } else {
                    // Handle the selected mood
                    fetchShopStock(selectedTitle);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Fetch all entries when nothing is selected
                fetchShopStock("");
            }
        });
    }

    private void fetchShopStock(String selectedTitle) {
        DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("Stock");
        shopRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Items shopItem = snapshot.getValue(Items.class);
                    if (shopItem != null && (selectedTitle.isEmpty() || selectedTitle.equals(shopItem.getTitle()))) {
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
    private AddNewItem.OnItemSavedListener savedListener;
    public void setOnItemSavedListener(AddNewItem.OnItemSavedListener listener) {
        this.savedListener = listener;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}