package com.example.thedigitalnotelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    //fields
    EditText emailEditText, passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView registerBtnTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //connect fields to their xml layout counterparts
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        registerBtnTextView = findViewById(R.id.register_text_view_btn);

        loginBtn.setOnClickListener(v -> login());
        registerBtnTextView.setOnClickListener((v )->
                startActivity(new Intent(LoginActivity.this, RegisActivity.class)));
    }

    void login(){

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean isValid = validateInput(email, password);
        if(!isValid){
            return;
        }
        loginFirebaseAccount(email, password);
    }

    //show
    void loginFirebaseAccount(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            changeProgress(false);
            if(task.isSuccessful()){
                //successful login
                if(firebaseAuth.getCurrentUser().isEmailVerified()){
                    Utility.showToast(LoginActivity.this, "Successful login.");
                    startActivity(new Intent(LoginActivity.this, OptionsActivity.class));
                }else{
                    Utility.showToast(LoginActivity.this,
                            "You have not verified your email yet.");
                }
            }else{
                //unsuccessful login
                Utility.showToast(LoginActivity.this, "Incorrect!");
            }
        });
    }


    void changeProgress(boolean progress){
        if(progress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateInput(String email, String password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Invalid Email");
            return false;
        }
        //Length < 5 includes empty field
        if(password.length()<5) {
            passwordEditText.setError("Password must be greater than 4 characters");
            return false;
        }
        return true;
    }

}