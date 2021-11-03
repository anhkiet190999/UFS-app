package com.example.ufs;

public class User {
    public String username, email, password, studentid, phone;
    public boolean isVendor;


    public User(){

    }

    public User(String username, String email, String password, String studentid, String phone){
        this.username = username;
        this.password = password;
        this.studentid = studentid;
        this.phone = phone;
        this.email = email;
        this.isVendor = false;
    }
}
