package com.example.eva_mariaschoen.sose2018travelbird;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new MainActivity.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {

        private Button buttonRegister;
        private EditText editTextEmail;
        private EditText editTextPassword;
        private EditText editTextUserName;
        private ProgressDialog progressDialog;
        CircleImageView profilePicture;


        private Integer currTab;

        private FirebaseAuth firebaseAuth;
        private FirebaseFirestore firestore;


        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            currTab = getArguments().getInt(ARG_SECTION_NUMBER);


            firebaseAuth = FirebaseAuth.getInstance();
            firestore = FirebaseFirestore.getInstance();

            progressDialog = new ProgressDialog(getActivity());

            buttonRegister = (Button) rootView.findViewById(R.id.buttonRegister);
            editTextEmail = (EditText) rootView.findViewById(R.id.editTextEmail);
            editTextPassword = (EditText) rootView.findViewById(R.id.editTextPassword);
            editTextUserName = (EditText) rootView.findViewById(R.id.editTextUserName);
            profilePicture = (CircleImageView) rootView.findViewById(R.id.profile_picture);


            if (currTab == 1) {
                buttonRegister.setText("Sign Up");
            } else {
                buttonRegister.setText("Log In");
            }

            buttonRegister.setOnClickListener(this);
            return rootView;
        }


        public void onClick(View view) {

            if (view == buttonRegister) {
                if (currTab == 1) {
                    registerUser();
                    sendNotification();
                } else {
                    loginUser();
                }

            }
        }

        //sending a notification when signing up to app was successful
        private void sendNotification() {
            NotificationCompat.Builder notification;

            notification = new NotificationCompat.Builder(getContext());
            notification.setAutoCancel(true);
            notification.setSmallIcon(R.drawable.ic_launcher_background);
            notification.setContentTitle("Welcome to TravelBird!");
            notification.setContentText("You signed up successfully.");

            Intent notificationIntent = new Intent(getContext(), ProfileActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pendingIntent);
            NotificationManager nm = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
            nm.notify(23, notification.build());

        }

        private void loginUser() {

            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            progressDialog.setMessage("LogIn User");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Loged in successfully!", Toast.LENGTH_SHORT).show();
                                Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);


                                startActivity(profileIntent);
                            } else {
                                Toast.makeText(getActivity(), "Could not log in User!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }

        //signing up to app by uploading users inputs to firebase
        private void registerUser() {

            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String username = editTextUserName.getText().toString().trim();


            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getActivity(), "enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(getActivity(), "enter a password with at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(getActivity(), "enter an username", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.setMessage("Registering User");
            progressDialog.show();


            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                Map<String, String> userMap = new HashMap<>();
                                userMap.put("name", username);
                                userMap.put("uid", user.getUid());


                                firestore.collection("users").document(user.getUid()).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void mVoid) {


                                    }
                                });


                                Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
                                startActivity(profileIntent);
                            } else {
                                Toast.makeText(getActivity(), "Sorry, something went wrong. Please try again!", Toast.LENGTH_LONG).show();
                            }


                        }
                    });


        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return MainActivity.PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

}
