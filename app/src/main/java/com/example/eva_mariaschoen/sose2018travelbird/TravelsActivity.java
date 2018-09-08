package com.example.eva_mariaschoen.sose2018travelbird;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
//import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class TravelsActivity extends BaseActivity {

    ListView travelList;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseUser user;

    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.activity_travels, frameLayout);


        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = firebaseAuth.getCurrentUser();


        travelList = (ListView) view.findViewById(R.id.travelList);
        arrayList = new ArrayList<>();

        firestore.collection("travels").whereEqualTo("uid", user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        arrayList.add(document.getData().get("title").toString());
                        Collections.sort(arrayList);

                    }
                    Log.d("document snapshot", "" + arrayList);
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
                Object item = adapterView.getItemAtPosition(i);
                Log.d("logItem", "" + item);
                String name = item.toString();
                //Gson gson = new Gson();
                //String json = gson.toJson(item);
                Intent intent = new Intent(TravelsActivity.this, ShowTravel.class);
                intent.putExtra("ITEM", name);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "klicked", Toast.LENGTH_SHORT).show();

            }
        });

        travelList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getItemAtPosition(i);

                Toast.makeText(getApplicationContext(), "long klicked", Toast.LENGTH_SHORT).show();

                AlertDialog diaBox = AskOption(item);
                diaBox.show();
                return true;

            }


        });
    }

    private AlertDialog AskOption(Object item) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete your Trip")
                .setMessage("Do you really want to delete your trip?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {


                        String name = item.toString();

                        firestore.collection("travels")
                                .whereEqualTo("uid", user.getUid())
                                .whereEqualTo("title", name)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();

                                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                    document.getReference().delete();

                                }
                            }

                        });
                        arrayList.remove(item);
                        adapter.notifyDataSetChanged();


                        //your deleting code
                        dialog.dismiss();
                    }

                })


                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }
}
