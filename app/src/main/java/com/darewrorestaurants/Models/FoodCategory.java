package com.darewrorestaurants.Models;

import java.util.ArrayList;

/**
 * Created by Jaffar on 9/27/2017.
 */
public class FoodCategory {
    int id;
    String category;
    // ArrayList to store food items
    private ArrayList<FoodItem> foodItems;

    public FoodCategory(int id, String category) {
        this.id = id;
        this.category = category;
    }

    public FoodCategory() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    // ArrayList to store food items
    public ArrayList<FoodItem> getFoodItems() {
        return foodItems;
    }
    public void setFoodItems(ArrayList<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }
}
