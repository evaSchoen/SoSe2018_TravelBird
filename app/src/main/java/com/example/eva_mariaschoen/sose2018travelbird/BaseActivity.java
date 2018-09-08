package com.example.eva_mariaschoen.sose2018travelbird;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class BaseActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    FrameLayout frameLayout;

    NavigationView navigationView;
    Toolbar toolbar;
    TextView eMailTextView;
    TextView userNameTextView;
    ImageView userPictureImageView;

    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        String email = user.getEmail();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        frameLayout = findViewById(R.id.content_frame);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        View headerDrawer = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.nav_home:
                                Intent home = new Intent(getApplicationContext(), ProfileActivity.class);
                                startActivity(home);
                                break;
                            case R.id.nav_travels:
                                Intent travels = new Intent(getApplicationContext(), TravelsActivity.class);
                                startActivity(travels);
                                break;
                            case R.id.nav_calendar:
                                Intent calendar = new Intent(getApplicationContext(), CalendarActivity.class);
                                startActivity(calendar);
                                break;
                            case R.id.nav_logout:

                                FirebaseAuth.getInstance().signOut(); //End user session
                                startActivity(new Intent(getApplicationContext(), MainActivity.class)); //Go back to home page
                                finish();

                        }
                        return true;
                    }


                });

        eMailTextView = (TextView) headerDrawer.findViewById(R.id.emailHeader);
        eMailTextView.setText(email);


        userNameTextView = (TextView) headerDrawer.findViewById(R.id.userNameHeader);
        userPictureImageView = (CircleImageView) headerDrawer.findViewById(R.id.userPictureHeader);


       StorageReference profilePictureReference = storageReference.child("images/" + user.getUid().toString());

        profilePictureReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //profilePicture.setImageURI(uri);
                Log.d("uri log", "" + uri);
                Glide.with(getApplicationContext()).load(uri).into(userPictureImageView);

            }
        });



        DocumentReference docRef = firestore.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        userNameTextView.setText(document.getString("name"));
                    }
                }
            }
        });

    }


}