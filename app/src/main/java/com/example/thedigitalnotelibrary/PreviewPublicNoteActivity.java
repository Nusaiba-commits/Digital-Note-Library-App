package com.example.thedigitalnotelibrary;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class PreviewPublicNoteActivity extends AppCompatActivity {

    //fields
    EditText titleEditText,contentEditText;
    ImageButton downloadBtn, deleteBtn;
    String title, content;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_public_note);

        titleEditText = findViewById(R.id.public_note_title_txt);
        contentEditText = findViewById(R.id.public_note_content_txt);
        downloadBtn = findViewById(R.id.download_note_btn);
        deleteBtn = findViewById(R.id.delete_public_note_btn);

        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals("c0NXywfGgaXEUlqvzoTprsiodvC2")){
            deleteBtn.setVisibility(View.VISIBLE);
            downloadBtn.setVisibility(View.INVISIBLE);
        } else {
            deleteBtn.setVisibility(View.INVISIBLE);
            downloadBtn.setVisibility(View.VISIBLE);
        }

        //receive data for an already saved note
        title = getIntent().getStringExtra("title");
        content= getIntent().getStringExtra("content");

        //populate the note
        titleEditText.setText(title);
        contentEditText.setText(content);

        //redirection for buttons
        downloadBtn.setOnClickListener( (v)-> downloadNote() );
        deleteBtn.setOnClickListener(v -> deleteNote() );
    }

    //makes a copy of the note and save it in the current user's "my notes" collection
    void downloadNote() {
        //receive data for an already saved note
        title = titleEditText.getText().toString();
        content= contentEditText.getText().toString();
        DocumentReference docID = Utility.getCollectionReferenceForPublicNotes()
                .document(getIntent().getStringExtra("docId"));
        docID.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot result = task.getResult();
                Map<String, Object> map = result.getData();
                Object dwnlds = map.get("downloads");
                int intDwnlds = Integer.parseInt(dwnlds.toString());
                intDwnlds = intDwnlds + 1;
                Map<String, Object> updates = new HashMap<>();
                updates.put("downloads", intDwnlds);
                docID.update(updates)
                        .addOnSuccessListener(aVoid -> Utility.showToast(PreviewPublicNoteActivity.this,
                                "Document updated successfully."))
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Utility.showToast(PreviewPublicNoteActivity.this,
                                        "Error updating document");
                            }
                        });
            } else Utility.showToast(PreviewPublicNoteActivity.this, "Unsuccessful update");
        });
        //it will be saved as a different document with a different Document Reference
        //in the user's personal collection
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setTimestamp(Timestamp.now());
        saveNoteToMyLibrary(note);
    }



    void saveNoteToMyLibrary(Note note){
        DocumentReference documentReference;
        //create new note
        documentReference = Utility.getCollectionReferenceForNotes().document();

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is added
                    Utility.showToast(PreviewPublicNoteActivity.this,
                            "Note added to my library successfully");
                    finish();
                }else{
                    Utility.showToast(PreviewPublicNoteActivity.this,
                            "Failed while adding note");
                }
            }
        });
    }

    void deleteNote(){
        //deletes the note from the Firestore database
        DocumentReference docID = Utility.getCollectionReferenceForPublicNotes()
                .document(getIntent().getStringExtra("docId"));
        docID.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(PreviewPublicNoteActivity.this,
                            "Note deleted successfully");
                    finish();
                }else{
                    Utility.showToast(PreviewPublicNoteActivity.this,
                            "Failed while deleting note");
                }
            }
        });
    }


}

//        timestamp = getIntent().getStringExtra("timestamp");
//        docId = getIntent().getStringExtra("docId");