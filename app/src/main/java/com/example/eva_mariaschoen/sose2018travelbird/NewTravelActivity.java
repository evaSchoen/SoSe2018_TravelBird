package com.example.eva_mariaschoen.sose2018travelbird;


import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewTravelActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText entry;
    EditText travelTitle;
    EditText departure;
    EditText homecoming;
    FloatingActionButton saveTravel;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String firestoreTravelTitle;
    CircleImageView travelPicture;


    DatePickerDialog.OnDateSetListener mDateSetListenerDeparture;
    DatePickerDialog.OnDateSetListener mDateSetListenerHomecoming;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_travel);


        departure = (EditText) findViewById(R.id.departure);
        homecoming = (EditText) findViewById(R.id.homecoming);
        saveTravel = (FloatingActionButton) findViewById(R.id.saveTravelButton);
        travelTitle = (EditText) findViewById(R.id.travel_title);
        entry = (EditText) findViewById(R.id.entry);
        entry.setScroller(new Scroller(getApplicationContext()));

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        travelPicture = (CircleImageView) findViewById(R.id.travel_picture);
        toolbar = (Toolbar) findViewById(R.id.toolbarNewTravel);
        setSupportActionBar(toolbar);


        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        departure.setOnClickListener(new View.OnClickListener() {

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


        homecoming.setOnClickListener(new View.OnClickListener() {

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

        saveTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String traveltitle = travelTitle.getText().toString().trim();

                FirebaseUser user = firebaseAuth.getCurrentUser();
                Map<String, String> travelMap = new HashMap<>();
                travelMap.put("title", traveltitle);
                travelMap.put("uid", user.getUid());

                firestore.collection("travels").add(travelMap);

                DocumentReference docRef = firestore.collection("travels").document(user.getUid());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String firestoreTravel = document.getString("title") ;




                                // immer die erste Reise?! Wie bekomm ich die Titel von der zuletzt erstellten Reise
                                //ordered by timestamp?!
                                Log.d("travel title + ", firestoreTravel);
                                Toast.makeText(getApplicationContext(), firestoreTravel, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


            }
        });



        //edit the title picture of the travel by clicking the picture
        travelPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
                Toast.makeText(NewTravelActivity.this, "image clicked", Toast.LENGTH_SHORT).show();

            }
        });
    }
        //set the the travel title picture with the picture user took from gallery
        protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
            super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

                    if (resultCode == RESULT_OK) {
                        Uri selectedImage = imageReturnedIntent.getData();
                        travelPicture.setImageURI(selectedImage);
                    }

            }


    //arrow for going back to previous activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
            // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


}




