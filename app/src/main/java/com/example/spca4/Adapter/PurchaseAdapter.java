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

import com.example.spca4.Model.Basket;
import com.example.spca4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.TaskViewHolder> {

    private List<Basket> basketList;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Context context;

    public PurchaseAdapter(List<com.example.spca4.Model.Basket> basketList, Context context){
        this.basketList = basketList;
        this.context = context;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView PurchaseTitle;
        TextView PurchasePrice;
        TextView PurchaseQuantity;
        TextView PurchaseManufacturer;
        TextView PurchaseCategory;
        ImageView PurchaseImage;


        // ViewHolder components (e.g., TextViews for tag, description, etc.)

        public TaskViewHolder(View itemView) {
            super(itemView);
            // Initialize ViewHolder components
            PurchaseTitle = itemView.findViewById(R.id.PurchaseTitle);
            PurchasePrice = itemView.findViewById(R.id.PurchasePrice);
            PurchaseQuantity = itemView.findViewById(R.id.PurchaseQuantity);
            PurchaseManufacturer = itemView.findViewById(R.id.PurchaseManufacturer);
            PurchaseCategory = itemView.findViewById(R.id.PurchaseCategory);
            PurchaseImage = itemView.findViewById(R.id.PurchaseImage);

        }
    }

    @NonNull
    @Override
    public PurchaseAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchases_layout, parent, false);
        mAuth = FirebaseAuth.getInstance();
        return new PurchaseAdapter.TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseAdapter.TaskViewHolder holder, int position) {
        Basket basketItem = basketList.get(position);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null && basketItem != null) {
            holder.PurchaseTitle.setText("Title: " + basketItem.getTitle());
            holder.PurchasePrice.setText("Price: €" + String.valueOf((int) basketItem.getPrice()));
            holder.PurchaseQuantity.setText("Quantity: " + String.valueOf((int) basketItem.getQuantity()));
            holder.PurchaseManufacturer.setText("Manufacturer: " + basketItem.getManufacturer());
            holder.PurchaseCategory.setText("Category: " + basketItem.getCategory());
            String imageUrl = basketItem.getImageUrl();
            Picasso.get().load(imageUrl).into(holder.PurchaseImage);
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
