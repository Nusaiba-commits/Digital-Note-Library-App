package com.example.thedigitalnotelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AdminDashboardUserSideActivity extends AppCompatActivity {

    MaterialButton deleteBtn, changePasswordBtn;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_options);

        deleteBtn = findViewById(R.id.admin_users_delete_btn);
        changePasswordBtn = findViewById(R.id.admin_users_change_password_btn);

        deleteBtn.setOnClickListener( v -> deleteUser());
        changePasswordBtn.setOnClickListener( v -> showPopup());
    }

    void deleteUser(){
        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("users").document(uID).
                collection("my notes").get().addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                QuerySnapshot snapshot = task.getResult();
                                if (!snapshot.isEmpty()) {
                                    for (QueryDocumentSnapshot doc : snapshot) {
                                        if (doc.exists()) {
                                            //delete the user's "my notes" collection
                                            doc.getReference().delete();
                                            //then delete the user's document in "users" collection
                                            FirebaseFirestore.getInstance().
                                                    collection("users").document(uID).delete();
                                        }
                                    }
                                }
                            } else {
                                Utility.showToast(AdminDashboardUserSideActivity.this,
                                        Objects.requireNonNull(task.getException()).getLocalizedMessage());
                            }
                        });



        //delete the user from the Authentication table
        FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(
                Task -> {
                    if(Task.isSuccessful()) {
                        //account has been deleted
                        Utility.showToast(AdminDashboardUserSideActivity.this,
                                "Deleted Successfully." );
                        startActivity(new Intent(AdminDashboardUserSideActivity.this,
                                LoginActivity.class));
                    } else {
                        //account has failed to be deleted
                        Utility.showToast(AdminDashboardUserSideActivity.this,
                                Objects.requireNonNull(Task.getException()).getLocalizedMessage());
                    }
                });
    }




    //show
    void showPopup() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_password, null);
        passwordEditText = popupView.findViewById(R.id.popup_change_password);

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setView(popupView);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String newPassword = passwordEditText.getText().toString();
            Boolean isValid = validateNewPassword(newPassword);
            if(isValid){
                // The new password is taken and the Authentication table and FireStore records are updated with it
                FirebaseAuth.getInstance().getCurrentUser().updatePassword(newPassword).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentReference docID = FirebaseFirestore.getInstance().
                                        collection("users").document(FirebaseAuth.getInstance().
                                                getCurrentUser().getUid());
                                docID.get().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("password", newPassword);
                                        docID.update(updates)
                                                .addOnSuccessListener(aVoid -> Utility.
                                                        showToast(AdminDashboardUserSideActivity.this,
                                                                "Password updated successfully!"))
                                                .addOnFailureListener(e -> Utility.showToast(
                                                        AdminDashboardUserSideActivity.this,
                                                        "Password update failed."));
                                    } else {
                                        Utility.showToast(AdminDashboardUserSideActivity.this,
                                                "Failed to get user document");
                                    }
                                });
                            } else Utility.showToast(AdminDashboardUserSideActivity.this,
                                    "User update failed.");
                        });
            }

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    Boolean validateNewPassword(String newPassword){
        // length < 5 includes length = 0 which means it is blank
        if(newPassword.length()<5) {
            Utility.showToast(AdminDashboardUserSideActivity.this, "Must be greater than 4 characters.");
            return false;
        }
        return true;
    }

}

