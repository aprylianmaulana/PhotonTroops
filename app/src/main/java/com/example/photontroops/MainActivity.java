package com.example.photontroops;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button mLogoutBtn;
    boolean twice = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogoutBtn = findViewById(R.id.buttonLogOut);

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
