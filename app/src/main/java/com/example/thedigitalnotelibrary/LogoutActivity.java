package com.example.thedigitalnotelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout_screen);
        FirebaseAuth.getInstance().signOut();
        new Handler().postDelayed(() -> startActivity(new Intent(LogoutActivity.this,
                MainActivity.class)), 1000);
    }
}
