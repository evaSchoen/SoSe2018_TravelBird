package com.example.eva_mariaschoen.sose2018travelbird;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class NewEntryActivity extends AppCompatActivity {

    EditText travelentry;
    FloatingActionButton save;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();


        travelentry = (EditText) findViewById(R.id.entry);
        save = (FloatingActionButton) findViewById(R.id.saveEntryButton);



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewEntryActivity.this, NewTravelActivity.class);
                String entryString = travelentry.getText().toString();

                i.putExtra("ENTRY_STRING", entryString);
                startActivity(i);
            }
        });
    }
}
