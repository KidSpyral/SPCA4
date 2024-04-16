package com.example.spca4.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public abstract class ItemId implements Parcelable {


    @Exclude
    protected String itemId;

    public ItemId() {
        // Default constructor required for Firebase
    }
    @Exclude
    public String getItemId() {
        return itemId;
    }
    @Exclude
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @NonNull
    @Override
    public String toString() {
        return "ItemId{" +
                "itemId='" + itemId + '\'' +
                '}';
    }

    // Parcelable implementation
    protected ItemId(Parcel in) {
        itemId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemId);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public <T extends ItemId> T withId(@NonNull final String id) {
        this.itemId = id;
        return (T) this;
    }

}
