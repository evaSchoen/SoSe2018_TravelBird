package com.example.eva_mariaschoen.sose2018travelbird;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileActivity extends BaseActivity {

    TextView eMailTextView;
    TextView userNameTextView;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FloatingActionButton addTravelProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profile, frameLayout);
        setTitle("Profile");

        userNameTextView=(TextView)findViewById(R.id.userNameProfile);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = firebaseAuth.getCurrentUser().getEmail();
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

        addTravelProfile = (FloatingActionButton)findViewById(R.id.addTravelProfile);
        eMailTextView=(TextView)findViewById(R.id.emailProfile);
        eMailTextView.setText(email);


        addTravelProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, NewTravelActivity.class );
                startActivity(i);
            }
        });

    }
}









