package com.example.photontroops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginPage extends AppCompatActivity {
    EditText mEmail;
    EditText mPassword;
    Button mBLoginBtn;
    TextView mSignupBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    boolean twice = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mEmail      = findViewById(R.id.editTextEmailLogin);
        mPassword   = findViewById(R.id.editTextPwdLogin);
        mBLoginBtn  = findViewById(R.id.buttonLogIn);
        mSignupBtn  = findViewById(R.id.tVLogin2);
        progressBar = findViewById(R.id.pBLogin);
        fAuth       = FirebaseAuth.getInstance();

        //Create login button onClickListener
        mBLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email    = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                //Validate email & password should be not null
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }
                //Minimum length of password is 8 characters
                if(password.length()<8){
                    mPassword.setError("At least password should be 8 characters.");
                }
                progressBar.setVisibility(View.VISIBLE);
                //Authentication the user data (email & pass) with the db
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginPage.this, "Login Succeed!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(LoginPage.this, "Error! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterPage.class));
                finish();
            }
        });
    }
    //Membuat onClickListener untuk double button close app
    @Override
    /**
     * Method apabila tombol back diklik 2 kali dalam kurun waktu 3 detik maka
     * akan menutup aplikasi.
     */
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
        },3000);
    }
}
