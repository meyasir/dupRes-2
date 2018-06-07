package com.darewrorestaurants.Models;

import java.util.ArrayList;

/**
 * Created by Jaffar on 2018-02-14.
 */

public class Order {

    int id;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    String customerName;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

    public long getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(long readyTime) {
        this.readyTime = readyTime;
    }

    long readyTime;

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    String status;

    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }

    String detail;
    public long getPrepareTime() {
        return prepareTime;
    }
    public void setPrepareTime(long prepareTime) {
        this.prepareTime = prepareTime;
    }

    long prepareTime;

    public ArrayList<OrderFoodItem> getOrderFoodItems() {
        return orderFoodItems;
    }
    public void setOrderFoodItems(ArrayList<OrderFoodItem> orderFoodItems) {
        this.orderFoodItems = orderFoodItems;
    }

    ArrayList<OrderFoodItem> orderFoodItems;

    public Order() {
    }
}
