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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.spca4.Adapter.StockAdapter;
import com.example.spca4.Model.Items;
import com.example.spca4.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
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

public class Admin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AddNewItem.OnItemSavedListener, StockObserver {

    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FloatingActionButton fab;
    FirebaseAuth mAuth;
    String User;
    private StockAdapter ItemsAdapter;
    private List<Items> stockList;
    private RecyclerView recyclerView, recyclerView2;
    Spinner adminshopFilter;

    private List<StockObserver> observers = new ArrayList<>();

    // Method to register observers
    public void registerObserver(StockObserver observer) {
        observers.add(observer);
    }

    // Method to unregister observers
    public void unregisterObserver(StockObserver observer) {
        observers.remove(observer);
    }

    // Method to notify observers
    private void notifyObservers(List<Items> stockList) {
        for (StockObserver observer : observers) {
            observer.onStockUpdate(stockList);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.adminshopRCV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        fab = findViewById(R.id.fab);

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewItem dialog = AddNewItem.newInstance();
                dialog.setOnItemSavedListener(Admin.this);
                dialog.show(getSupportFragmentManager(), AddNewItem.TAG);
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

        stockList = new ArrayList<>();
        ItemsAdapter = new StockAdapter(stockList,this);
        recyclerView.setAdapter(ItemsAdapter);
        fetchStock("");
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
        Spinner titleSpinner = findViewById(R.id.adminshopFilter);
        titleSpinner.setAdapter(adapter);

        titleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedTitle = titleList.get(position);
                if (selectedTitle.isEmpty()) {
                    // Handle the case where no mood is selected
                    fetchStock("");
                } else {
                    // Handle the selected mood
                    fetchStock(selectedTitle);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Fetch all entries when nothing is selected
                fetchStock("");
            }
        });
    }

    private void fetchStock(String selectedTitle) {
        DatabaseReference stockRef = FirebaseDatabase.getInstance().getReference("Stock");
        stockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stockList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Items stockItem = snapshot.getValue(Items.class);
                    if (stockItem != null && (selectedTitle.isEmpty() || selectedTitle.equals(stockItem.getTitle()))) {
                        stockList.add(stockItem);
                    }
                }
                Log.d("MainActivity", "ShopList size: " + stockList.size());
                ItemsAdapter.notifyDataSetChanged();
                notifyObservers(stockList); // Notify observers when stock data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Admin.this, "Error fetching stock", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onStockUpdate(List<Items> stockList) {
        // Update RecyclerView or any other UI component with the new stock data
        ItemsAdapter.setStockList(stockList);
        ItemsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

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