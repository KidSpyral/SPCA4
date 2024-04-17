package com.example.spca4.Model;

public class AddressDetailsBuilder {
    private String fullname;
    private String phonenumber;
    private String postcode;
    private String line1;
    private String line2;
    private String city;

    public AddressDetailsBuilder() {}

    public AddressDetailsBuilder fullname(String fullname) {
        this.fullname = fullname;
        return this;
    }

    public AddressDetailsBuilder phonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
        return this;
    }

    public AddressDetailsBuilder postcode(String postcode) {
        this.postcode = postcode;
        return this;
    }

    public AddressDetailsBuilder line1(String line1) {
        this.line1 = line1;
        return this;
    }

    public AddressDetailsBuilder line2(String line2) {
        this.line2 = line2;
        return this;
    }

    public AddressDetailsBuilder city(String city) {
        this.city = city;
        return this;
    }

    public AddressDetails build() {
        return new AddressDetails(fullname, phonenumber, postcode, line1, line2, city);
    }
}
