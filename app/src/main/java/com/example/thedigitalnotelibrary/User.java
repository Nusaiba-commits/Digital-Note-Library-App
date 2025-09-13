package com.example.thedigitalnotelibrary;

import java.util.ArrayList;

public class User {

    //fields

    String username, role, email, password, reference;
    ArrayList<String> keywords;

    public User(){
        //default constructor
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }
    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }
    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


// EXTRA TEXT

//    public User(String username){
//        //custom constructor
//        this.username = username;
//        this.role = "user";
//    }