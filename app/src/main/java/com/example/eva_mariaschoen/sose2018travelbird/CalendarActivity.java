package com.example.eva_mariaschoen.sose2018travelbird;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toolbar;

public class CalendarActivity extends BaseActivity {

    CalendarView calendarView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_calendar, frameLayout);
        setTitle("Calendar");


    }
}
