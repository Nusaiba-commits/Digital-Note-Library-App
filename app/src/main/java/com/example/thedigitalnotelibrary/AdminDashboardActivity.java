package com.example.thedigitalnotelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    //field
    Button usersBtn, deletePublicNoteBtn, logOutBtn;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        usersBtn = findViewById(R.id.admin_dashboard_users_btn);
        deletePublicNoteBtn = findViewById(R.id.admin_dashboard_notes_btn);
        logOutBtn = findViewById(R.id.admin_dashboard_logout_btn);


        usersBtn.setOnClickListener( v -> startActivity(
                new Intent(AdminDashboardActivity.this, AdminUsersActivity.class)));
        logOutBtn.setOnClickListener( v -> startActivity(
                new Intent(AdminDashboardActivity.this, LogoutActivity.class)));
        Intent intent = new Intent(AdminDashboardActivity.this, PublicLibraryActivity.class);
        intent.putExtra("role", "admin");
        deletePublicNoteBtn.setOnClickListener( v -> startActivity(intent));
    }
}
