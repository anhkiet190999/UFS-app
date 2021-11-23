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

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }
}
