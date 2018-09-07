package com.example.eva_mariaschoen.sose2018travelbird;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class TravelsActivity extends BaseActivity {

    ListView travelList;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private Uri filePath;
    String[] strings;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.activity_travels, frameLayout);
        setTitle("Travels");

        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        travelList = (ListView) view.findViewById(R.id.travelList);
        arrayList = new ArrayList<String>();


        FirebaseUser user = firebaseAuth.getCurrentUser();

        firestore.collection("travels").whereEqualTo("uid", user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        arrayList.add(document.getData().get("title").toString());

                    }
                    Log.d("document snapshot", "" + arrayList);
                    Collections.sort(arrayList);
                    adapter = new ArrayAdapter<String>(TravelsActivity.this, android.R.layout.simple_list_item_1, arrayList);
                    travelList.setAdapter(adapter);


                }
            }
        });

        StorageReference profilePictureReference = storageReference.child("images/" + firestore.collection("travels").toString());
        profilePictureReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //profilePicture.setImageURI(uri);
                arrayList.add(uri.toString());
                travelList.setAdapter(adapter);
                Log.d("uri log", "" + uri);


            }
        });

        travelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "klicked", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
