package com.example.ufs;

public class Vendor {
    public String name, email, password, licence, phone;
    public boolean isVendor;


    public Vendor(){

    }

    public Vendor(String name, String email, String password, String licence, String phone){
        this.name = name;
        this.email = email;
        this.password = password;
        this.licence = licence;
        this.phone = phone;
        this.isVendor = true;
    }
}
