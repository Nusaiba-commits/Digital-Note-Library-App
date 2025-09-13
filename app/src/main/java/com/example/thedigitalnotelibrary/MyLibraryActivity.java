package com.example.thedigitalnotelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyLibraryActivity extends AppCompatActivity {

    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    EditText searchBar;
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_library);

        addNoteBtn = findViewById(R.id.my_library_add_note_btn);
        recyclerView = findViewById(R.id.my_library_recycler_view);
        searchBar = findViewById(R.id.my_library_search_bar);


        setupRecyclerView("");
        addNoteBtn.setOnClickListener((v)-> startActivity(new Intent(MyLibraryActivity.this,NoteContentActivity.class)) );
        //menuBtn.setOnClickListener((v)->showMenu() );

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                setupRecyclerView("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            //show
            @Override
            public void afterTextChanged(Editable s) {
                //validation
                if(s.toString().length() < 70){
                    setupRecyclerView(s.toString());
                } else {
                    Utility.showToast(MyLibraryActivity.this, "There are no titles " +
                            "that long!");
                }
            }
        });
    }


    void setupRecyclerView(String searchQuery) {
        Query query;
            if (searchQuery.isEmpty()) {
                query = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("my notes").orderBy("title");
            } else {
                query = FirebaseFirestore.getInstance().collection("users").
                        document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        collection("my notes").whereArrayContains(FieldPath.of("keywords"), searchQuery.toLowerCase());
            }
            FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                    .setQuery(query, Note.class).build();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            noteAdapter = new NoteAdapter(options, this);
            noteAdapter.startListening();
            recyclerView.setAdapter(noteAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }

}