package com.example.thedigitalnotelibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PublicNoteItemAdapter extends FirestoreRecyclerAdapter<Note, PublicNoteItemAdapter.NoteViewHolder> {
    Context context;

    public PublicNoteItemAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.titleTextView.setText(note.title);
        holder.contentTextView.setText(note.content);
        String subjectText = "Subject: " + note.subject;
        holder.subjectTextView.setText(subjectText);
        String yearText = "Year: " + note.getYear();
        holder.yearTextView.setText(yearText);
        String dateText = "Date uploaded: " + Utility.timestampToString(note.timestamp);
        holder.timestampTextView.setText(dateText);
        String downloadsText = "Downloads: " + note.getDownloads();
        holder.downloadsTextView.setText(downloadsText);
        String authorText = "Author: " + note.author;
        holder.authorTextView.setText(authorText);

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context,PreviewPublicNoteActivity.class);
            intent.putExtra("title",note.title);
            intent.putExtra("content",note.content);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_recycler_note_item,parent,false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView,contentTextView,timestampTextView, downloadsTextView, yearTextView,
                subjectTextView, authorTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.public_note_title_text_view);
            contentTextView = itemView.findViewById(R.id.public_note_content_text_view);
            timestampTextView = itemView.findViewById(R.id.public_note_timestamp_text_view);
            downloadsTextView = itemView.findViewById(R.id.public_note_downloads_text_view);
            yearTextView = itemView.findViewById(R.id.public_note_year_text_view);
            subjectTextView = itemView.findViewById(R.id.public_note_subject_text_view);
            authorTextView = itemView.findViewById(R.id.public_note_author_text_view);
        }
    }
}


//            intent.putExtra("subject", note.subject);
//            intent.putExtra("year", note.year);
//            intent.putExtra("author", note.author);
//            intent.putExtra("downloads", note.downloads);