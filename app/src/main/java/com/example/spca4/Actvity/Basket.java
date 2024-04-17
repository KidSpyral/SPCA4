package com.example.spca4.Actvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spca4.Adapter.BasketAdapter;
import com.example.spca4.Adapter.shopAdapter;
import com.example.spca4.Model.Items;
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

public class Basket extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Button Checkout;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    String User;
    private com.example.spca4.Adapter.BasketAdapter BasketAdapter;
    private List<com.example.spca4.Model.Basket> basketList;
    private RecyclerView recyclerView;
    TextView totalPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser().getUid();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Checkout = findViewById(R.id.Checkout);
        totalPriceTextView = findViewById(R.id.TQText);

        Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Checkout.class);
                startActivity(intent);
                finish();

            }
        });

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

        recyclerView = findViewById(R.id.Basket);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        basketList = new ArrayList<>();
        BasketAdapter = new BasketAdapter(basketList, this);
        recyclerView.setAdapter(BasketAdapter);
        fetchBasket();
    }

    private void fetchBasket() {
        DatabaseReference basketRef = FirebaseDatabase.getInstance().getReference().child("Registered Users").child(User).child("Basket");
        basketRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                basketList.clear();
                double totalPrice = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    com.example.spca4.Model.Basket basketItem = snapshot.getValue(com.example.spca4.Model.Basket.class);
                    if (basketItem != null) {
                        basketList.add(basketItem);
                        totalPrice += basketItem.getPrice() * basketItem.getQuantity();
                    }
                }
                totalPriceTextView.setText("Total Price: €" + totalPrice); // Update the total price TextView
                BasketAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Basket.this, "Error fetching stock", Toast.LENGTH_SHORT).show();
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
}