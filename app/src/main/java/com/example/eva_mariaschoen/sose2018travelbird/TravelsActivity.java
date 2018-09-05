package com.example.eva_mariaschoen.sose2018travelbird;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class TravelsActivity extends BaseActivity {

    FloatingActionButton addTravelTravels;
    ListView travelList;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_travels, frameLayout);
        setTitle("Travels");
        addTravelTravels = (FloatingActionButton) view.findViewById(R.id.addTravelTravels);
        travelList = (ListView)view.findViewById(R.id.travelList);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(TravelsActivity.this, android.R.layout.simple_list_item_1, arrayList);

        travelList.setAdapter(adapter);


        addTravelTravels.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent createTravel = new Intent(getApplicationContext(), NewTravelActivity.class);
                startActivity(createTravel);
            }
        });

    }
}
