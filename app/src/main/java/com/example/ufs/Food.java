package com.example.ufs;

public class Food {
    public String item;
    public String price;

    public Food(){

    }

    public Food(String item, String price){
        this.item = item;
        this.price = price;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
