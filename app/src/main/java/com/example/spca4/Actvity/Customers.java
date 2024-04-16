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

public class Customers extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    String User;
    private com.example.spca4.Adapter.UsersAdapter UsersAdapter;
    private List<ReadWriteUserDetails> userDetailsList;
    private RecyclerView recyclerView, recyclerView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.usersRCV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

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
                } if (itemId == R.id.navigation_profile) {
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


        userDetailsList = new ArrayList<>();
        UsersAdapter = new UsersAdapter(userDetailsList, this);
        recyclerView.setAdapter(UsersAdapter);
        fetchUsers();

    }

    private void fetchUsers() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Registered Users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userDetailsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                    if (userDetails != null && userDetails.getUserType().equals("User")) {
                        userDetailsList.add(userDetails);
                    }
                }
                // Notify the adapter that the data set has changed
                UsersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Customers.this, "Error fetching user details", Toast.LENGTH_SHORT).show();
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