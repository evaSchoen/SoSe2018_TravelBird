package com.example.eva_mariaschoen.sose2018travelbird;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BaseActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    FrameLayout frameLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView eMailTextView;
    TextView userNameTextView;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;



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

        FirebaseUser user = firebaseAuth.getCurrentUser();

        String email = user.getEmail();



        mDrawerLayout = findViewById(R.id.drawer_layout);
        frameLayout = findViewById(R.id.content_frame);


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
                            case R.id.nav_settings:
                                Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
                                startActivity(settings);
                                break;
                            case R.id.nav_logout:

                                FirebaseAuth.getInstance().signOut(); //End user session
                                startActivity(new Intent(getApplicationContext(), MainActivity.class)); //Go back to home page
                                finish();

                        }
                        return true;
                    }


                });

        eMailTextView=(TextView)headerDrawer.findViewById(R.id.emailHeader);
        eMailTextView.setText(email);


        userNameTextView = (TextView)headerDrawer.findViewById(R.id.userNameHeader);

        DocumentReference docRef = firestore.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        userNameTextView.setText(document.getString("name"));
                    }
                }
            }
        });





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}