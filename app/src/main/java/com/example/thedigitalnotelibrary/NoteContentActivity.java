package com.example.thedigitalnotelibrary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class NoteContentActivity extends AppCompatActivity {

    //fields
    EditText titleEditText,contentEditText;
    ImageButton saveNoteBtn, deleteNoteBtn, uploadBtn, blackBtn, greenBtn, redBtn, eraserBtn, undoBtn;
    boolean isEditMode = false;
    String title, content, docId;
    StorageReference fileReference;
    DrawView paint;
    ImageView image;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note_content);

        titleEditText = findViewById(R.id.note_title_txt);
        contentEditText = findViewById(R.id.note_content_txt);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        deleteNoteBtn = findViewById(R.id.delete_note_btn);
        uploadBtn = findViewById(R.id.upload_note_btn);
        eraserBtn = findViewById(R.id.eraser_btn);
        undoBtn = findViewById(R.id.undo_btn);
        blackBtn = findViewById(R.id.draw_black_colour);
        greenBtn = findViewById(R.id.draw_green_colour);
        redBtn = findViewById(R.id.draw_red_colour);
        paint = findViewById(R.id.draw_note_view);

        //receive data for an already saved note
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        //this means a doc already exists in firebase
        if (docId != null && !docId.isEmpty()) {
            isEditMode = true;
            titleEditText.setText(title);
            contentEditText.setText(content);
        }

        //since it's not a new note, delete option visible
        if (isEditMode) {
            deleteNoteBtn.setVisibility(View.VISIBLE);
        } else {
            deleteNoteBtn.setVisibility(View.INVISIBLE);
            uploadBtn.setVisibility(View.INVISIBLE);
        }

        saveNoteBtn.setOnClickListener((v) -> {
            try {
                saveNote();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        deleteNoteBtn.setOnClickListener((v) -> deleteNoteFromFirebase());
        uploadBtn.setOnClickListener((v) -> {
                Intent intent = new Intent(NoteContentActivity.this, UploadFormActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("content", content);
                startActivity(intent);
        });
        //show
        redBtn.setOnClickListener(v -> {
            paint.setColor(Color.RED);
            paint.setStrokeWidth(15);
        });
        greenBtn.setOnClickListener(v -> {
            paint.setColor(Color.GREEN);
            paint.setStrokeWidth(15);
        });
        blackBtn.setOnClickListener(v -> {
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(15);
        });
        eraserBtn.setOnClickListener(v -> {
            paint.setColor(Color.YELLOW);
            paint.setStrokeWidth(50);} );

        // the undo button will remove the most recent stroke from the canvas
        undoBtn.setOnClickListener(v -> paint.undo());

        ViewTreeObserver vto = paint.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                paint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = paint.getMeasuredWidth();
                int height = paint.getMeasuredHeight();
                paint.init(height, width);
            }
        });
    }

    void saveNote() throws IOException {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        //validation
        if(noteTitle.isEmpty()){
            titleEditText.setError("Title is required");
            return;
        }
        if(noteTitle.length() > 70) {
            titleEditText.setError("Title is too long!");
            return;
        }
        //forming the note
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());
        String[] keywords = noteTitle.toLowerCase().split(" ");
        ArrayList<String> keywordsArray = new ArrayList<>(Arrays.asList(keywords));
        note.setKeywords(keywordsArray);
        saveNoteToFirebase(note);

        //for future methods to save drawing
        //saveDrawingToFirebase();
        //note.setImageRef(fileReference.getName());
    }

    void saveNoteToFirebase(Note note) {
        DocumentReference documentReference;
        if(isEditMode){
            //update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }else{
            //create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }


        documentReference.set(note).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                //note is added
                Utility.showToast(NoteContentActivity.this,"Note saved successfully");
                finish();
            }else{
                Utility.showToast(NoteContentActivity.this,"Failed while saving note");
            }
        });


    }

    void deleteNoteFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                //note is deleted
                Utility.showToast(NoteContentActivity.this,"Note deleted successfully");
                finish();
            }else{
                Utility.showToast(NoteContentActivity.this,"Failed while deleting note");
            }
        });
    }



}