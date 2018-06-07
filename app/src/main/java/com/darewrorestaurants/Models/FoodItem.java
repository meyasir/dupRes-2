package com.darewrorestaurants.Models;

/**
 * Created by Jaffar on 8/28/2017.
 */
public class FoodItem {
    int id;
    String name;
    String desc;
    String price;
    String weight;
    String quantity;

    public FoodItem() {
    }

    public FoodItem(String name, String desc, String price, String weight) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getWeight() {
        return weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }
}
