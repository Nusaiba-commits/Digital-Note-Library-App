package com.example.thedigitalnotelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginActivity extends AppCompatActivity {
    //fields

    EditText email, username, password, dob, passcode;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);

        email = findViewById(R.id.admin_email_edit_text);
        username = findViewById(R.id.admin_username_edit_text);
        password = findViewById(R.id.admin_password_edit_text);
        passcode = findViewById(R.id.admin_passcode_edit_text);
        dob = findViewById(R.id.admin_dob_edit_text);
        submitBtn = findViewById(R.id.admin_login_btn);

        submitBtn.setOnClickListener(v -> submitDetails());
    }

    void submitDetails() {
        //user input is collected
        String admin_email = email.getText().toString();
        String admin_username = username.getText().toString();
        String admin_password = password.getText().toString();
        String admin_passcode = passcode.getText().toString();
        String admin_dob = dob.getText().toString();

        //user input is validated
        boolean isValid = validate(admin_email, admin_username, admin_password, admin_passcode, admin_dob);

        //outcome based on validation
        if (isValid) {
            adminLogin(admin_email, admin_password);
        } else {
            Utility.showToast(AdminLoginActivity.this,
                    "One or more of your answers are incorrect");
        }
    }
    //show
    void adminLogin(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Utility.showToast(AdminLoginActivity.this, "Login Successful");
                        startActivity(new Intent(AdminLoginActivity.this,
                                AdminDashboardActivity.class));
                    } else {
                        Utility.showToast(AdminLoginActivity.this, "Problem signing in.");
                    }
                });
    }
    //show
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
