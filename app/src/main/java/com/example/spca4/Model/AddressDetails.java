package com.example.spca4.Model;

public class AddressDetails {

    private String fullname;
    private String phonenumber;
    private String postcode;
    private String line1;
    private String line2;
    private String city;

    // Constructor
    public AddressDetails(String fullname, String phonenumber, String postcode, String line1, String line2, String city) {
        this.fullname = fullname;
        this.phonenumber = phonenumber;
        this.postcode = postcode;
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
    }

    // Getters and Setters
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
