package com.example.spca4.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public abstract class BasketId {


    @Exclude
    protected String basketId;

    public BasketId() {
        // Default constructor required for Firebase
    }
    @Exclude
    public String getBasketId() {
        return basketId;
    }
    @Exclude
    public void setBasketId(String basketId) {
        this.basketId = basketId;
    }

    @NonNull
    @Override
    public String toString() {
        return "BasketId{" +
                "basketId='" + basketId + '\'' +
                '}';
    }


    public <T extends BasketId> T withId(@NonNull final String id) {
        this.basketId = id;
        return (T) this;
    }

}
