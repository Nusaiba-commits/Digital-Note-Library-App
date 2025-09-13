package com.example.thedigitalnotelibrary;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


public class UploadFormActivity extends AppCompatActivity {

    EditText subjectEditText, yearEditText;
    MaterialButton uploadBtn;
    String authorName, noteContent, noteTitle, noteYear, noteSubject;
    TextView authorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_form);

        subjectEditText = findViewById(R.id.subject_edit_text);
        yearEditText = findViewById(R.id.year_edit_text);
        authorText = findViewById(R.id.author_text_view);
        uploadBtn = findViewById(R.id.upload_form_btn);

        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("users").
                document(uID).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot result = task.getResult();
                        Map<String, Object> map = result.getData();
                        Object author = map.get("username");
                        authorName = author.toString();
                        authorText.setText("Author: " + authorName);
                    } else Utility.showToast(UploadFormActivity.this,
                            "Unsuccessful in getting user's name");
                });

        uploadBtn.setOnClickListener(v->contentVerification());

    }
    //show
    Boolean contentValidation(String year, String subject) {
        //validating all input
        if (subject.isEmpty() || (Utility.containSpecialChars(subject))){
           this.subjectEditText.setError("Invalid Subject Name");
            Utility.showToast(UploadFormActivity.this, "Invalid Subject Name");
            return false;
        }

        if (year.isEmpty() || (Utility.doesNotContainNumbers(year)) || year.length() > 2) {
            this.yearEditText.setError("Invalid Year Digit");
            return false;
        }
        return true;
    }

    void contentVerification() {
        //get the note's title, content, and timestamp, bind it to the preview note view
        noteTitle = getIntent().getStringExtra("title");
        noteContent = getIntent().getStringExtra("content");
        noteYear = yearEditText.getText().toString();
        noteSubject = subjectEditText.getText().toString().toLowerCase();
        Query query = Utility.getCollectionReferenceForPublicNotes().whereEqualTo("content", noteContent);

                //checking for same content in public notes
                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (!document.getData().isEmpty()){ // if there is content that is same
                                Utility.showToast(UploadFormActivity.this,
                                        "Hey! Note already exists in Public Library!");
                                finish(); // go back to the previous activity
                            }
                        }
                        if (task.getResult().isEmpty()){
                            uploadToPublicLibrary();
                        }
                    } else {
                        Utility.showToast(UploadFormActivity.this,
                                "Error getting documents: ");

                    }
                });
    }

    void uploadToPublicLibrary() {
            if (contentValidation(noteYear, noteSubject)) {
                Note note = new Note();
                note.setTitle(noteTitle);
                note.setContent(noteContent);
                note.setTimestamp(Timestamp.now());
                note.setYear(Integer.parseInt(noteYear));
                note.setAuthor(authorName);
                note.setSubject(noteSubject);
                //add all the fields to the keywords array
                String[] keywords = noteSubject.toLowerCase().split(" ");
                ArrayList<String> keywordsArray = new ArrayList<>(Arrays.asList(keywords));
                note.setKeywords(keywordsArray);

                String[] titleArray = noteTitle.toLowerCase().split(" ");
                for(String s: titleArray){note.getKeywords().add(s);}

                String[] authorArray = authorName.toLowerCase().split(" ");
                for(String s: authorArray){note.getKeywords().add(s);}
                note.getKeywords().add(noteYear);

                // as nobody has downloaded the note yet
                note.setDownloads(0);
                savePublicNoteToFirebase(note);
            } else {
                Utility.showToast(UploadFormActivity.this,"Oops! Incorrect form field(s)");
            }


    }

    void savePublicNoteToFirebase(Note note){
        DocumentReference documentReference;

        //create new note
        documentReference = Utility.getCollectionReferenceForPublicNotes().document();
        documentReference.set(note).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                //note is added
                Utility.showToast(UploadFormActivity.this,
                        "Yay! Note added to public library successfully");
                finish();
            }else{
                Utility.showToast(UploadFormActivity.this,
                        "Oops! Failed while adding note");
            }
        });

    }

}
