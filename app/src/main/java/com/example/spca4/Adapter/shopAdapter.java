package com.example.spca4.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spca4.Model.Items;
import com.example.spca4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class shopAdapter extends RecyclerView.Adapter<shopAdapter.TaskViewHolder> {

    private List<Items> shopList;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Context context;

    public shopAdapter(List<Items> shopList, Context context){
        this.shopList = shopList;
        this.context = context;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView shopTitle;
        TextView shopPrice;
        TextView shopQuantity;
        TextView shopManufacturer;
        TextView shopCategory;
        ImageView shopImage;

        // ViewHolder components (e.g., TextViews for tag, description, etc.)

        public TaskViewHolder(View itemView) {
            super(itemView);
            // Initialize ViewHolder components
            shopTitle = itemView.findViewById(R.id.shopTitle);
            shopPrice = itemView.findViewById(R.id.shopPrice);
            shopQuantity = itemView.findViewById(R.id.shopQuantity);
            shopManufacturer = itemView.findViewById(R.id.shopManufacturer);
            shopCategory = itemView.findViewById(R.id.shopCategory);
            shopImage = itemView.findViewById(R.id.shopImage);
        }
    }



    @NonNull
    @Override
    public shopAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_layout, parent, false);
        mAuth = FirebaseAuth.getInstance();
        return new shopAdapter.TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull shopAdapter.TaskViewHolder holder, int position) {
        Items shopItem = shopList.get(position);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null && shopItem != null) {
            holder.shopTitle.setText("Title: " + shopItem.getTitle());
            holder.shopPrice.setText("Price: €" + String.valueOf((int) shopItem.getPrice()));
            holder.shopQuantity.setText("Quantity: " + String.valueOf((int) shopItem.getQuantity()));
            holder.shopManufacturer.setText("Manufacturer: " + shopItem.getManufacturer());
            holder.shopCategory.setText("Category: " + shopItem.getCategory());
            String imageUrl = shopItem.getImageUrl();
            Picasso.get().load(imageUrl).into(holder.shopImage);
        }
    }

    public Context getContext(){
        return context;
    }

    public int getItemCount() {
        if (shopList == null) {
            Log.d("shopAdapter", "shopList is null");
            return 0;
        }
        return shopList.size();
    }
}
