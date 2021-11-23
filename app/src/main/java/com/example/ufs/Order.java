package com.example.ufs;

public class Order {

    public String foodName;
    public int quantity;
    public float total_price;

    public Order(){

    }

    public Order(String foodName, int quantity, float total_price){
        this.foodName = foodName;
        this.quantity = quantity;
        this.total_price = total_price;
    }

}
