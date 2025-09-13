package com.example.thedigitalnotelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginUserSideActivity extends AppCompatActivity {

    EditText email, username, password, dob, passcode;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.admin_login);

        email = findViewById(R.id.admin_email_edit_text);
        username = findViewById(R.id.admin_username_edit_text);
        password = findViewById(R.id.admin_password_edit_text);
        passcode = findViewById(R.id.admin_passcode_edit_text);
        dob = findViewById(R.id.admin_dob_edit_text);
        submitBtn = findViewById(R.id.admin_login_btn);

        submitBtn.setOnClickListener( v -> submitDetails());
    }

    void submitDetails(){

        String admin_email = email.getText().toString();
        String admin_username = username.getText().toString();
        String admin_password = password.getText().toString();
        String admin_passcode = passcode.getText().toString();
        String admin_dob = dob.getText().toString();

        boolean isValid = validate(admin_email, admin_username, admin_password, admin_passcode, admin_dob);

        if (isValid){
            Utility.showToast(AdminLoginUserSideActivity.this, "Login Successful");
            startActivity(new Intent(AdminLoginUserSideActivity.this, AdminDashboardUserSideActivity.class));
        }
        if (!isValid){
            Utility.showToast(AdminLoginUserSideActivity.this, "One or more of your answers are incorrect");
        }
    }

    Boolean validate(String email, String username, String password, String passcode, String dob){

        if(!email.equals("exzitin@gmail.com")){
            this.email.setError("Incorrect");
        }
        if(!password.equals("0568928517Banana!")){
            this.password.setError("Incorrect");
        }
        if(!username.equals("Nusaiba Amin")){
            this.username.setError("Incorrect");
        }
        if(!passcode.equals("255961")){
            this.passcode.setError("Incorrect");
        }
        if(!dob.equals("01/09/2000")){
            this.dob.setError("Incorrect");
        }
        if(email.isEmpty() || password.isEmpty() || username.isEmpty() || passcode.isEmpty() || dob.isEmpty()){
            return false;
        }
        return (email.equals("exzitin@gmail.com")) && (password.equals("0568928517Banana!")) &&
                (username.equals("Nusaiba Amin")) && (passcode.equals("255961") && (dob.equals("01/09/2000")));
    }

}
