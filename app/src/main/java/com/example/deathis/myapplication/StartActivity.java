package com.example.deathis.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(StartActivity.this,
                    MapsActivity.class));
            finish();
        } else if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(StartActivity.this,
                    EmailPassActivity.class));
            finish();
        } else {

        }
    }
}
