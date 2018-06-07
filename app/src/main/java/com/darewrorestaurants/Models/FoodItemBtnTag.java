package com.darewrorestaurants.Models;

/**
 * Created by Jaffar on 9/27/2017.
 */
public class FoodItemBtnTag {
    int  parent;
    int  child;

    public FoodItemBtnTag(int parent, int child) {
        this.parent = parent;
        this.child = child;
    }

    public FoodItemBtnTag() {
    }
    public int getParent() {
        return parent;
    }
    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getChild() {
        return child;
    }
    public void setChild(int child) {
        this.child = child;
    }
}
