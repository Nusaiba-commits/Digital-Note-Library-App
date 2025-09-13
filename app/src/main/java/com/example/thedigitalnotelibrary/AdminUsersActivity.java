package com.example.thedigitalnotelibrary;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

//This class lets the admin search all the registered users.

public class AdminUsersActivity extends AppCompatActivity {

    //fields
    RecyclerView recyclerView;
    AdminUsersItemAdapter adapter;
    EditText searchBar;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.admin_users);
        recyclerView = findViewById(R.id.admin_users_recycler_view);
        searchBar = findViewById(R.id.admin_side_users_search_bar);

        //initially
        setUpRecyclerView("");
        //when admin searches something
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                setUpRecyclerView("");
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                //divides query into words to enable multiple item search

                    //validation of search
                    if(s.toString().length() < 70){
                        setUpRecyclerView(s.toString());
                    }
                    if(s.toString().length() > 70) {
                        Utility.showToast(AdminUsersActivity.this, "There are no titles " +
                                "that long!");
                    }
                    if(Utility.containSpecialChars(s.toString())) {
                        Utility.showToast(AdminUsersActivity.this, "Invalid name!");
                    }
            }
        });
    }
    //show
    void setUpRecyclerView(String searchQuery){
        //querying the database
        Query query;
        if(searchQuery.isEmpty()){
            query = FirebaseFirestore.getInstance().collection("users")
                    .orderBy("username", Query.Direction.DESCENDING);

        } else {
            query = FirebaseFirestore.getInstance().collection("users").whereArrayContains(FieldPath.of("keywords"), searchQuery.toLowerCase());
        }
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query,User.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminUsersItemAdapter(options,this);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

}
