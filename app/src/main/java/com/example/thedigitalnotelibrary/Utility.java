package com.example.thedigitalnotelibrary;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    static CollectionReference getCollectionReferenceForNotes(){
        // get the "my notes" collection reference ID for the current user.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        return FirebaseFirestore.getInstance().collection("users")
                .document(currentUser.getUid()).collection("my notes");

    }

    static CollectionReference getCollectionReferenceForPublicNotes(){
        //get the collection reference ID for the public library notes
        return FirebaseFirestore.getInstance().collection("public notes");

    }


    static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }


    public static boolean doesNotContainNumbers(String str) {
        //used in the validation methods to check if a string contains any numbers
        if (str == null || str.isEmpty()) {
            return true; // The empty string does not contain any numbers.
        }
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return false; // A digit character is found in the string.
            }
        }
        return true; // No digit characters are found in the string.
    }

    //return true if string contains no special characters
    public static boolean containSpecialChars(String str){
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }


}
