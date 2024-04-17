package com.example.spca4.Model;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public abstract class PurchasesId {

    @Exclude
    protected String purchasesId;

    public PurchasesId() {
        // Default constructor required for Firebase
    }

    @Exclude
    public String getPurchasesId() {
        return purchasesId;
    }
    @Exclude
    public void setPurchasesId(String purchasesId) {
        this.purchasesId = purchasesId;
    }

    @NonNull
    @Override
    public String toString() {
        return "PurchasesIdPurchasesId{" +
                "purchasesId='" + purchasesId + '\'' +
                '}';
    }


    public <T extends PurchasesId> T withId(@NonNull final String id) {
        this.purchasesId = id;
        return (T) this;
    }
}
