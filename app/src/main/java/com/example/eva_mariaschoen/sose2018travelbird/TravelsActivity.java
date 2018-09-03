package com.example.eva_mariaschoen.sose2018travelbird;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class TravelsActivity extends BaseActivity {

FloatingActionButton addTravelTravels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_travels, frameLayout);
        setTitle("Travels");
        addTravelTravels = (FloatingActionButton) view.findViewById(R.id.addTravelTravels);

        System.out.println(addTravelTravels);

        addTravelTravels.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent createTravel = new Intent(getApplicationContext(), NewTravelActivity.class);
                startActivity(createTravel);
            }
        });

    }
}
