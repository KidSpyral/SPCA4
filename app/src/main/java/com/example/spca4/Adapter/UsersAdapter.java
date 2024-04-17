package com.example.spca4.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spca4.Actvity.Admin;
import com.example.spca4.Actvity.Login;
import com.example.spca4.Actvity.PurchaseHistory;
import com.example.spca4.Model.Basket;
import com.example.spca4.Model.ReadWriteUserDetails;
import com.example.spca4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.TaskViewHolder> {

    private List<ReadWriteUserDetails> userDetailsList;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Context context;

    public UsersAdapter(List<ReadWriteUserDetails> userDetailsList, Context context){
        this.userDetailsList = userDetailsList;
        this.context = context;


    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView Email;
        TextView Name;
        TextView Phone;
        TextView Type;
        Button PurchaseHistory;

        // ViewHolder components (e.g., TextViews for tag, description, etc.)

        public TaskViewHolder(View itemView) {
            super(itemView);
            // Initialize ViewHolder components
            Email = itemView.findViewById(R.id.Email);
            Name = itemView.findViewById(R.id.Name);
            Phone = itemView.findViewById(R.id.Phone);
            Type = itemView.findViewById(R.id.Type);
            PurchaseHistory = itemView.findViewById(R.id.PurchaseHistory);
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_layout, parent, false);
        mAuth = FirebaseAuth.getInstance();
        return new TaskViewHolder(itemView);
    }

    public Context getContext(){
        return context;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        ReadWriteUserDetails userDetails = userDetailsList.get(position);
        FirebaseUser currentAdmin = mAuth.getCurrentUser();

        if (currentAdmin != null && userDetails != null) {
            if (userDetails.getUserType().equals("User")) {
                holder.Email.setText(userDetails.getEmail());
                holder.Name.setText(userDetails.getName());
                holder.Phone.setText(userDetails.getPhone());
                holder.Type.setText(userDetails.getUserType());


                // Add To Basket Button Click Listener
                holder.PurchaseHistory.setOnClickListener(view -> {

                    ReadWriteUserDetails userDetails1 = new ReadWriteUserDetails(userDetails.getEmail(), userDetails.getPassword(), userDetails.getName(), userDetails.getPhone(), userDetails.getUserType());

                    // Pass user details to the PurchaseHistory activity
                    Intent intent = new Intent(context, PurchaseHistory.class);
                    intent.putExtra("userDetails", userDetails1);
                    context.startActivity(intent);
                });
            } else {
                // If userType is not "User", skip this item
                return;
            }
        }
    }


    @Override
    public int getItemCount() {
        if (userDetailsList == null) {
            Log.d("UsersAdapter", "userDetailsList is null");
            return 0;
        }
        return userDetailsList.size();
    }
}
