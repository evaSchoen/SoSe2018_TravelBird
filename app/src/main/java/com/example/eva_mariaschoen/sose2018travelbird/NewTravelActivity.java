package com.example.eva_mariaschoen.sose2018travelbird;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewTravelActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 71;
    Toolbar toolbar;
    Button buttonEntry;
    EditText travelTitle;
    EditText departure;
    EditText homecoming;
    ImageButton buttonLocation;
    private TextView viewLocation;
    FloatingActionButton saveTravel;
    CircleImageView travelPicture;
    private Uri filePath;

    String entryString;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    DatePickerDialog.OnDateSetListener mDateSetListenerDeparture;
    DatePickerDialog.OnDateSetListener mDateSetListenerHomecoming;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

        }

        setContentView(R.layout.activity_new_travel);

        departure = (EditText) findViewById(R.id.departure);
        homecoming = (EditText) findViewById(R.id.homecoming);
        saveTravel = (FloatingActionButton) findViewById(R.id.saveTravelButton);
        travelTitle = (EditText) findViewById(R.id.travel_title);
        buttonEntry = (Button) findViewById(R.id.buttonEntry);
        buttonLocation = (ImageButton) findViewById(R.id.button_location);
        viewLocation = (TextView) findViewById(R.id.travel_location);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();


        travelPicture = (CircleImageView) findViewById(R.id.travel_picture);
        toolbar = (Toolbar) findViewById(R.id.toolbarNewTravel);
        setSupportActionBar(toolbar);

        //by hitting the "location" button users location with city and country is detected and set to the textview
        buttonLocation.setOnClickListener(new View.OnClickListener() {
            //asking for permissions
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
                } else {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    try {
                        String city = getAddress(location.getLatitude(), location.getLongitude());
                        viewLocation.setText(city);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        buttonEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewTravelActivity.this,
                        NewEntryActivity.class);
                startActivityForResult(i, 123);

            }
        });

        //opening a date picker for choosing date of arrival
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

        //same as departure
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

        //saving whole trip
        saveTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = "";
                String departureString = "";
                String homecomingString = "";
                String location = "";


                departureString = departure.getText().toString();
                homecomingString = homecoming.getText().toString();
                location = viewLocation.getText().toString();

                String traveltitle = travelTitle.getText().toString().trim();

                //creating a new document in firebase collection "travels"
                //user id must be saved here too, using it in ShowTravelActivity for showing the trips of current user
                Map<String, String> travelMap = new HashMap<>();
                travelMap.put("title", traveltitle);
                travelMap.put("uid", user.getUid());
                travelMap.put("entry", entryString);
                travelMap.put("departure", departureString);
                travelMap.put("homecoming", homecomingString);
                travelMap.put("location", location);

                firestore.collection("travels").add(travelMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String documentIdString = documentReference.getId();
                        uploadImage(documentIdString);


                    }
                });
                String name = firestore.collection("travels").document().getId();
                Log.d("travelName", "" + name);

                Intent i = new Intent(NewTravelActivity.this,
                        TravelsActivity.class);
                startActivity(i);

            }
        });


        //edit the title picture of the travel by clicking the picture
        travelPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });


    }

    //set the the travel title picture with the picture user took from gallery
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                travelPicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 123 && resultCode == RESULT_OK) {
            entryString = data.getStringExtra("ENTRY_STRING");

        }
    }

    private void uploadImage(String documentIdString) {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            FirebaseUser user = firebaseAuth.getCurrentUser();

            StorageReference ref = storageReference.child("images/" + documentIdString);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {
                        String fullLocation = getAddress(location.getLatitude(), location.getLongitude());
                        viewLocation.setText(fullLocation);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    public String getAddress(double lats, double lons) {
        Geocoder geocoder;
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lats, lons, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            String city = addresses.get(0).getLocality();
            String country = addresses.get(0).getCountryName();
            String location = city + ", " + country;
            return location;
        } else {
            return "failed";
        }
    }


}




