package com.example.spca4.Model;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class ItemId {


    @Exclude
    public String itemId;

    public ItemId() {
        // Default constructor required for Firebase
    }
    public String getItemId() {
        return itemId;
    }
    public void setItemIdId(String itemId) {
        this.itemId = itemId;
    }

    public <T extends ItemId> T withId(@NonNull final String id) {
        this.itemId = id;
        return (T) this;
    }
}
