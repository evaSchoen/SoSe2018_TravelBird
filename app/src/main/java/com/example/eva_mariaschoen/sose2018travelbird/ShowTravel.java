package com.example.eva_mariaschoen.sose2018travelbird;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Collections;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowTravel extends AppCompatActivity {

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    TextView show_title;
    TextView show_location;
    TextView show_departure;
    TextView show_homecoming;
    TextView show_entry;
    CircleImageView show_picture;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_travel);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        show_title = (TextView) findViewById(R.id.showTitleView);
        show_location = (TextView) findViewById(R.id.travel_location_view);
        show_departure = (TextView) findViewById(R.id.travel_departure_view);
        show_homecoming = (TextView) findViewById(R.id.travel_homecoming_view);
        show_entry = (TextView) findViewById(R.id.travel_entry_view);
        show_picture = (CircleImageView) findViewById(R.id.travel_picture_show);

        toolbar = (Toolbar) findViewById(R.id.toolbarShowTravel);

        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent i = getIntent();
        String result = i.getExtras().getString("ITEM");
        show_title.setText(result);

        //using user id and title of trip for getting the right information about the trip and showing it
        firestore.collection("travels")
                .whereEqualTo("uid", user.getUid())
                .whereEqualTo("title", result)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    Map<String, Object> documentMap = document.getData();
                    String documentID = document.getId();
                    String documentString = documentMap.toString();

                    Object locationObject = documentMap.get("location");
                    Object departureObject = documentMap.get("departure");
                    Object homecomingObject = documentMap.get("homecoming");
                    Object entryObject = documentMap.get("entry");

                    show_location.setText(locationObject.toString());
                    show_departure.setText(departureObject.toString());
                    show_homecoming.setText(homecomingObject.toString());
                    show_entry.setText(entryObject.toString());

                    getTravelImage(documentID);

                    Log.d("departureLog", "" + documentString);

                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
            // close this activity and return to preview activity
        }

        return super.onOptionsItemSelected(item);
    }

    private void getTravelImage(String documentId) {

        StorageReference imageReference = storageReference.child("images/" + documentId);
        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(show_picture);
            }
        });
    }


}
