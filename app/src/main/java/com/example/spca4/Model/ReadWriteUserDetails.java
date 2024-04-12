package com.example.spca4.Model;

public class ReadWriteUserDetails {
    public String email;
    public String password;

    public String name;
    public String phone;
    public String code;

    public ReadWriteUserDetails(){

    }

   public ReadWriteUserDetails(String textEmailAddress, String textPassword, String textName, String textPhoneNumber, String textCode){
       this.email = textEmailAddress;
       this.password = textPassword;
       this.name = textName;
       this.phone = textPhoneNumber;
       this.code = textCode;
   }
}
