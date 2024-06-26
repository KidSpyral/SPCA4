package com.example.spca4.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spca4.Actvity.Admin;
import com.example.spca4.Model.Basket;
import com.example.spca4.Model.Items;
import com.example.spca4.Model.ReadWriteUserDetails;
import com.example.spca4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.TaskViewHolder> {

    private List<com.example.spca4.Model.Basket> basketList;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Context context;

    public BasketAdapter(List<com.example.spca4.Model.Basket> basketList, Context context){
        this.basketList = basketList;
        this.context = context;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView BasketTitle;
        TextView BasketPrice;
        TextView BasketQuantity;
        TextView BasketManufacturer;
        TextView BasketCategory;
        ImageView BasketImage;
        Button RemoveFromBasket;

        // ViewHolder components (e.g., TextViews for tag, description, etc.)

        public TaskViewHolder(View itemView) {
            super(itemView);
            // Initialize ViewHolder components
            BasketTitle = itemView.findViewById(R.id.BasketTitle);
            BasketPrice = itemView.findViewById(R.id.BasketPrice);
            BasketQuantity = itemView.findViewById(R.id.BasketQuantity);
            BasketManufacturer = itemView.findViewById(R.id.BasketManufacturer);
            BasketCategory = itemView.findViewById(R.id.BasketCategory);
            BasketImage = itemView.findViewById(R.id.BasketImage);
            RemoveFromBasket = itemView.findViewById(R.id.RemoveFromBasket);
        }
    }

    @NonNull
    @Override
    public BasketAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_layout, parent, false);
        mAuth = FirebaseAuth.getInstance();
        return new BasketAdapter.TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketAdapter.TaskViewHolder holder, int position) {
        Basket basketItem = basketList.get(position);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null && basketItem != null) {
            holder.BasketTitle.setText("Title: " + basketItem.getTitle());
            holder.BasketPrice.setText("Price: €" + String.valueOf((int) basketItem.getPrice()));
            holder.BasketQuantity.setText("Quantity: " + String.valueOf((int) basketItem.getQuantity()));
            holder.BasketManufacturer.setText("Manufacturer: " + basketItem.getManufacturer());
            holder.BasketCategory.setText("Category: " + basketItem.getCategory());
            String imageUrl = basketItem.getImageUrl();
            Picasso.get().load(imageUrl).into(holder.BasketImage);
        }
    }

    public Context getContext(){
        return context;
    }

    public int getItemCount() {
        if (basketList == null) {
            Log.d("BasketAdapter", "basketList is null");
            return 0;
        }
        return basketList.size();
    }
}
