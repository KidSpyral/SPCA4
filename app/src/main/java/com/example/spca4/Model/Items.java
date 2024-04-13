package com.example.spca4.Model;

public class Items extends ItemId
{
    private String Title;
    private String Manufacturer;
    private double Price;
    private int Quantity;
    private String Category;

    public Items(){

    }

    public Items(String title, String manufacturer, double price, int quantity, String category) {
        Title = title;
        Manufacturer = manufacturer;
        Price = price;
        Quantity = quantity;
        Category = category;
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
    @Override
    public String toString() {
        return "Item{id='" + itemId + "', manufacturer='" + Manufacturer + "', itle='" + Title + "', category='" + Category + "', price='" + Price + "', quantity='" + Quantity + "'}";
    }
}
