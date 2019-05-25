package com.example.codegladiators;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddBooking {

    public static void insert(String name,String address,String lat,String lng,String clothes){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        HashMap<String,String> data=new HashMap<>();
//        data.put("name",name);
//        data.put("address",address);
//        data.put("date","28th feb 2020");
//        data.put("amount","500");
//         database.getReference("Bookings").push().setValue(data);
        HashMap<String,String>map=new HashMap<>();
        map.put("name",name);
        map.put("address",address);
        map.put("latitude",lat);
        map.put("longitude",lng);
        map.put("count",clothes);
        database.getReference().child("Bookings").push().setValue(map);

    }
}
