package com.fernando.tastypoll.model;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Intent intent;

        if(user == null){
            intent = new Intent(MainActivity.this, Login.class);

        } else {
            ///
            intent = new Intent(MainActivity.this, App.class);

        }

        startActivity(intent);
        finish();
    }
}