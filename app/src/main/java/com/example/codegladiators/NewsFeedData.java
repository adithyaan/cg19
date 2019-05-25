package com.example.codegladiators;

import java.util.HashMap;

/**
 * Created by ADITHYA AN on 03-05-2018.
 */

public class NewsFeedData
{
    String name,address,date,amount;

    public NewsFeedData(String name, String address, String date, String amount) {
        this.name = name;
        this.address = address;
        this.date = date;
        this.amount = amount;
    }

    public NewsFeedData(){

    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "DAta:"+getName()+","+getAddress();
    }
}
