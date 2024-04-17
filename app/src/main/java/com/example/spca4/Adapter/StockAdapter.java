package com.example.spca4.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spca4.Actvity.AddNewItem;
import com.example.spca4.Model.Items;
import com.example.spca4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.TaskViewHolder> implements View.OnClickListener {

    private List<Items> stockList;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Context context;

    public StockAdapter(List<Items> stockList, Context context){
        this.stockList = stockList;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        Items stockItem = stockList.get(position);
        showUpdateDialog(stockItem);
    }

    private void showUpdateDialog(Items stockItem) {
        AddNewItem dialog = AddNewItem.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable("stockItem", (Parcelable) stockItem);
        bundle.putBoolean("isNewItem", false); // This is an existing item being updated
        bundle.putString("imageUri", stockItem.getImageUrl()); // Pass the existing image URI
        dialog.setArguments(bundle);
        dialog.show(((AppCompatActivity)context).getSupportFragmentManager(), AddNewItem.TAG);
    }


    public void setStockList(List<Items> stockList) {
        this.stockList = stockList;
        notifyDataSetChanged(); // Notify adapter that the dataset has changed
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView Title;
        TextView Price;
        TextView Quantity;
        TextView Manufacturer;
        TextView Category;
        ImageView Image;
        Button Update;

        // ViewHolder components (e.g., TextViews for tag, description, etc.)

        public TaskViewHolder(View itemView) {
            super(itemView);
            // Initialize ViewHolder components
            Title = itemView.findViewById(R.id.Title);
            Price = itemView.findViewById(R.id.Price);
            Quantity = itemView.findViewById(R.id.Quantity);
            Manufacturer = itemView.findViewById(R.id.Manufacturer);
            Category = itemView.findViewById(R.id.Category);
            Image = itemView.findViewById(R.id.Image);
            Update = itemView.findViewById(R.id.UpdateStock);
        }


    }



    @NonNull
    @Override
    public StockAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_layout, parent, false);
        mAuth = FirebaseAuth.getInstance();
        return new StockAdapter.TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Items stockItem = stockList.get(position);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null && stockItem != null) {
                holder.Title.setText("Title: " + stockItem.getTitle());
                holder.Price.setText("Price: €" + String.valueOf((int) stockItem.getPrice()));
                holder.Quantity.setText("Quantity: " + String.valueOf((int) stockItem.getQuantity()));
                holder.Manufacturer.setText("Manufacturer: " + stockItem.getManufacturer());
                holder.Category.setText("Category: " + stockItem.getCategory());
                String imageUrl = stockItem.getImageUrl();
                Picasso.get().load(imageUrl).into(holder.Image);
                holder.Update.setOnClickListener(this);
                holder.Update.setTag(position);
        }
    }
    public Context getContext(){
        return context;
    }

    public int getItemCount() {
        if (stockList == null) {
            Log.d("ItemsAdapter", "stockList is null");
            return 0;
        }
        return stockList.size();
    }
}
