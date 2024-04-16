package com.example.spca4.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spca4.Actvity.Admin;
import com.example.spca4.Model.ReadWriteUserDetails;
import com.example.spca4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.TaskViewHolder> {

    private List<ReadWriteUserDetails> userDetailsList;
    private Admin admin;
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

        // ViewHolder components (e.g., TextViews for tag, description, etc.)

        public TaskViewHolder(View itemView) {
            super(itemView);
            // Initialize ViewHolder components
            Email = itemView.findViewById(R.id.Email);
            Name = itemView.findViewById(R.id.Name);
            Phone = itemView.findViewById(R.id.Phone);
            Type = itemView.findViewById(R.id.Type);
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
