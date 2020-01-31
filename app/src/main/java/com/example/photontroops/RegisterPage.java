package com.example.photontroops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.*;

public class RegisterPage extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText mName;
    EditText mEmail;
    EditText mPassword;
    EditText mPhone;
    Button mSignUpBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    boolean twice = false;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mName       = findViewById(R.id.editTextNamelRegister);
        mEmail      = findViewById(R.id.editTextEmailRegister);
        mPassword   = findViewById(R.id.editTextPwdRegister);
        mSignUpBtn  = findViewById(R.id.buttonSignUp);
        mLoginBtn   = findViewById(R.id.tVRegist2);
        mPhone      = findViewById(R.id.editTextPhone);
        fAuth       = FirebaseAuth.getInstance();
        fStore      = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.pBSignUp);


        //Check validation is user is already register
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        //Create onClick function on SignUp button
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name     = mName.getText().toString();
                final String phone    = mPhone.getText().toString();
                final String email    = mEmail.getText().toString().trim();
                String password       = mPassword.getText().toString().trim();

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
                //Register user in firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterPage.this, "Your account is created.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            final Map<String,Object> user = new HashMap<>();
                            user.put("name",name);
                            user.put("email",email);
                            user.put("phone",phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"onSuccess: user profile is created for "+userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"onFailure: "+e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else{
                            Toast.makeText(RegisterPage.this,"Error! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginPage.class));
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
