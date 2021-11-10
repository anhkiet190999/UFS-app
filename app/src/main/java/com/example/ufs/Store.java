package com.example.ufs;

import java.util.ArrayList;

public class Store {
    public String name, phone;

    public class food{
        String name;
        double price;
    }
    public class menu{
        ArrayList<food> foods;
    }

    public Store(){

    }

    public Store(String name, String phone){
        this.name = name;
        this.phone = phone;
    }
}
