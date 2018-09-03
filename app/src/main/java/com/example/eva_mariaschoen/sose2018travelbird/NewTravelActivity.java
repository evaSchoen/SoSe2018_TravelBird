package com.example.eva_mariaschoen.sose2018travelbird;



import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.Calendar;

public class NewTravelActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView entry;
    EditText departure;
    EditText homecoming;

    DatePickerDialog.OnDateSetListener mDateSetListenerDeparture;
    DatePickerDialog.OnDateSetListener mDateSetListenerHomecoming;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_travel);



        departure = (EditText)findViewById(R.id.departure);
        homecoming = (EditText)findViewById(R.id.homecoming);
        entry = (TextView)findViewById(R.id.entry);
        entry.setScroller(new Scroller(getApplicationContext()));



        toolbar = (Toolbar) findViewById(R.id.toolbarNewTravel);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        departure.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(NewTravelActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerDeparture,
                        year, month, day);
                dialog.show();

            }
        });

        mDateSetListenerDeparture = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "/" + month + "/" + dayOfMonth;
                departure.setText(date);
            }
        };



        homecoming.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(NewTravelActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerHomecoming,
                        year, month, day);
                dialog.show();

            }
        });

        mDateSetListenerHomecoming = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "/" + month + "/" + dayOfMonth;
                homecoming.setText(date);
            }
        };

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();

            //zu Travels zurück???


            // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    }




