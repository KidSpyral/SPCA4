package com.example.spca4.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Basket extends BasketId
{
    private String Title;
    private String Manufacturer;
    private double Price;
    private int Quantity;
    private String Category;
    private String ImageUrl;

    public Basket(){

    }

    public Basket(String title, String manufacturer, double price, int quantity, String category, String imageUrl) {
        Title = title;
        Manufacturer = manufacturer;
        Price = price;
        Quantity = quantity;
        Category = category;
        ImageUrl = imageUrl;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Items{" +
                "title='" + Title + '\'' +
                ", manufacturer='" + Manufacturer + '\'' +
                ", price=" + Price +
                ", quantity=" + Quantity +
                ", category='" + Category + '\'' +
                ", imageUrl='" + ImageUrl + '\'' +
                ", basketId='" + basketId + '\'' +
                '}';
    }
}
