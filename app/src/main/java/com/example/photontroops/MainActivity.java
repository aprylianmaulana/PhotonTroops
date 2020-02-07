package com.example.photontroops;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Document;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    Button mLogoutBtn;
    TextView userName;
    TextView userEmail;
    TextView userPhone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    boolean twice = false;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogoutBtn = findViewById(R.id.buttonLogOut);
        userName   = findViewById(R.id.userName_text);
        userEmail  = findViewById(R.id.userName_email);
        userPhone  = findViewById(R.id.userName_phone);
        fAuth      = FirebaseAuth.getInstance();
        fStore     = FirebaseFirestore.getInstance();
        userID     = fAuth.getCurrentUser().getUid();

        /**
         * Retrieve data from firebase firestore
         * Create snapshot listener to fetch and showing the database
         */
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                //update the data
                userName.setText(documentSnapshot.getString("name"));
                userEmail.setText(documentSnapshot.getString("email"));
                userPhone.setText(documentSnapshot.getString("phone"));
            }
        });

        //Created Logout button
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginPage.class));
                finish();
            }
        });
    }
    /**Membuat onClickListener untuk double button close app
     * Method apabila tombol back diklik 2 kali dalam kurun waktu 3 detik maka
     * akan menutup aplikasi.
     */
    @Override
    public void onBackPressed(){
        if(twice){
            super.onBackPressed();
            finishAffinity();
            return;
        }
        this.twice = true;
        Toast.makeText(this,"Press once again to exit",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
            }
        },2000);
    }
}
