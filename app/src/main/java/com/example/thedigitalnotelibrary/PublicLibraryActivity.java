package com.example.thedigitalnotelibrary;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.Query;

public class PublicLibraryActivity extends AppCompatActivity {

    //fields
    RecyclerView publicRecyclerView;
    EditText searchBar;
    PublicNoteItemAdapter noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_library);

        publicRecyclerView = findViewById(R.id.public_library_recycler_view);
        searchBar = findViewById(R.id.public_library_search_bar);

        setupRecyclerView("");

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
                }
                if(s.toString().length() > 70) {
                    Utility.showToast(PublicLibraryActivity.this, "There are no titles " +
                            "that long!");
                }
            }
        });
    }

    //show
    void setupRecyclerView(String searchQuery){
        Query query;
        if(searchQuery.isEmpty()){
            query  = Utility.getCollectionReferenceForPublicNotes().orderBy("timestamp",
                    Query.Direction.DESCENDING);
        } else {
            query = Utility.getCollectionReferenceForPublicNotes().
                    whereArrayContains(FieldPath.of("keywords"), searchQuery.toLowerCase());
        }
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        // Grid Layout chosen
        publicRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        noteAdapter = new PublicNoteItemAdapter(options,this);
        noteAdapter.startListening();
        publicRecyclerView.setAdapter(noteAdapter);
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
