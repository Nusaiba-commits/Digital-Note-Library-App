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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class RegisActivity extends AppCompatActivity {

    EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    Button submitBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView, adminLoginBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //connect fields to their xml layout counterparts
        usernameEditText = findViewById(R.id.user_name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        submitBtn = findViewById(R.id.submit_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);
        adminLoginBtnTextView = findViewById(R.id.admin_click_here_btn);

        submitBtn.setOnClickListener(v -> makeAccount());
        loginBtnTextView.setOnClickListener(v -> startActivity(new Intent(RegisActivity.this, LoginActivity.class)));
        adminLoginBtnTextView.setOnClickListener(v -> startActivity(new Intent(RegisActivity.this, AdminLoginActivity.class)));

    }

    void makeAccount(){

        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        boolean isValid = validateRegisForm(username, email, password, confirmPassword);
        if(!isValid){
            return;
        }
        makeFirebaseAccount(email, password);
    }

    //show
    void makeFirebaseAccount(String email, String password){
        changeProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).
                addOnCompleteListener(RegisActivity.this,
                task -> {
                    changeProgress(false);
                    if(task.isSuccessful()) {
                        //account has been created
                       Utility.showToast(RegisActivity.this,
                               "Registration Successful. Verify email before login.");
                        Objects.requireNonNull(firebaseAuth.getCurrentUser()).sendEmailVerification();
                        saveUserToFirebase(usernameEditText.getText().toString(),
                                emailEditText.getText().toString(), passwordEditText.getText().toString());
                        firebaseAuth.signOut();
                        startActivity(new Intent(RegisActivity.this, LoginActivity.class));
                    } else {
                        //account has failed to be created
                        Utility.showToast(RegisActivity.this,
                                Objects.requireNonNull(task.getException()).getLocalizedMessage());
                    }
                });
    }

    void changeProgress(boolean progress){
        if(progress){
            progressBar.setVisibility(View.VISIBLE);
            submitBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            submitBtn.setVisibility(View.VISIBLE);
        }
    }

    public boolean validateRegisForm(String username, String email, String password,
                                     String confirmPassword){

        if((username.isEmpty()) || !(Utility.doesNotContainNumbers(username)) ||
                (Utility.containSpecialChars(username))) {
            this.usernameEditText.setError("Invalid Username");
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            this.emailEditText.setError("Invalid Email");
        }

        if(password.length()<5) {
            passwordEditText.setError("Password must be greater than 4 characters");
        }

        if(!password.equals(confirmPassword)){
            confirmPasswordEditText.setError("Passwords do not match");
        }

        return !username.isEmpty() && !Utility.containSpecialChars(username) && Utility.doesNotContainNumbers(username) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.length()>4 && password.equals(confirmPassword);
    }

    void saveUserToFirebase(String username, String email, String password) {
        //This method sets the User's ID to be the same as the User ID that was generated by the
        //Firebase firestore when creating an account
        DocumentReference userReference;
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        // adds new user to the "users" collection
        userReference = FirebaseFirestore.getInstance().collection("users").
                document(currentUser.getUid());
        //set the newly created user's fields
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("user");
        user.setReference(currentUser.getUid());
        String[] keywords = username.toLowerCase().split(" ");
        ArrayList<String> keywordsArray = new ArrayList<>(Arrays.asList(keywords));
        user.setKeywords(keywordsArray);

        userReference.set(user);
    }

    public EditText getUsernameEditText() {
        return usernameEditText;
    }

    public void setUsernameEditText(EditText usernameEditText) {
        this.usernameEditText = usernameEditText;
    }

    public EditText getEmailEditText() {
        return emailEditText;
    }

    public void setEmailEditText(EditText emailEditText) {
        this.emailEditText = emailEditText;
    }

    public EditText getPasswordEditText() {
        return passwordEditText;
    }

    public void setPasswordEditText(EditText passwordEditText) {
        this.passwordEditText = passwordEditText;
    }

    public EditText getConfirmPasswordEditText() {
        return confirmPasswordEditText;
    }

    public void setConfirmPasswordEditText(EditText confirmPasswordEditText) {
        this.confirmPasswordEditText = confirmPasswordEditText;
    }

    public Button getSubmitBtn() {
        return submitBtn;
    }

    public void setSubmitBtn(Button submitBtn) {
        this.submitBtn = submitBtn;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public TextView getLoginBtnTextView() {
        return loginBtnTextView;
    }

    public void setLoginBtnTextView(TextView loginBtnTextView) {
        this.loginBtnTextView = loginBtnTextView;
    }

    public TextView getAdminLoginBtnTextView() {
        return adminLoginBtnTextView;
    }

    public void setAdminLoginBtnTextView(TextView adminLoginBtnTextView) {
        this.adminLoginBtnTextView = adminLoginBtnTextView;
    }
}
