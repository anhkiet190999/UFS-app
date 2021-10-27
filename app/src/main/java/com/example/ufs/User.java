package com.example.ufs;

public class User {
    public String username, password, studentid, phone, email;
    public boolean isVendor;

    public User(){

    }

    public User(String username, String password, String studentid, String phone, String email, boolean isVendor){
        this.username = username;
        this.password = password;
        this.studentid = studentid;
        this.phone = phone;
        this.email = email;
        this.isVendor = isVendor;
    }
}
