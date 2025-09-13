package com.example.thedigitalnotelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OptionsActivity extends AppCompatActivity {

    //fields

    Button logoutBtn, publicLibraryBtn, myLibraryBtn;
    TextView admin;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_screen);

        //connecting the view to the controller
        myLibraryBtn = findViewById(R.id.myLibrary_btn);
        publicLibraryBtn = findViewById(R.id.publicLibrary_btn);
        logoutBtn = findViewById(R.id.logout_btn);
        admin = findViewById(R.id.my_library_options_admin);

        //button handlers
        admin.setOnClickListener(v -> startActivity(new Intent(OptionsActivity.this,
                AdminLoginUserSideActivity.class)));
        myLibraryBtn.setOnClickListener(v -> startActivity(new Intent(OptionsActivity.this,
                MyLibraryActivity.class)));
        logoutBtn.setOnClickListener(v -> startActivity(new Intent(OptionsActivity.this,
                LogoutActivity.class)));
        publicLibraryBtn.setOnClickListener(v -> startActivity(new Intent(OptionsActivity.this,
                PublicLibraryActivity.class)));
    }
}
