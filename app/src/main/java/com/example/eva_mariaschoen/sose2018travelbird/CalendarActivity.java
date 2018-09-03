package com.example.eva_mariaschoen.sose2018travelbird;


        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

public class CalendarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_calendar, frameLayout);
        setTitle("Calendar");


        setContentView(R.layout.activity_calendar);

    }
}
