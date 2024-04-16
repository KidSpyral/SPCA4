package com.example.spca4.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Basket extends ItemId implements Parcelable
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

    // Parcelable implementation
    protected Basket(Parcel in) {
        super(in);
        Title = in.readString();
        Manufacturer = in.readString();
        Price = in.readDouble();
        Quantity = in.readInt();
        Category = in.readString();
        ImageUrl = in.readString();
    }

    public static final Creator<Basket> CREATOR = new Creator<Basket>() {
        @Override
        public Basket createFromParcel(Parcel in) {
            return new Basket(in);
        }

        @Override
        public Basket[] newArray(int size) {
            return new Basket[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(Title);
        dest.writeString(Manufacturer);
        dest.writeDouble(Price);
        dest.writeInt(Quantity);
        dest.writeString(Category);
        dest.writeString(ImageUrl);
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
                ", itemId='" + itemId + '\'' +
                '}';
    }
}
