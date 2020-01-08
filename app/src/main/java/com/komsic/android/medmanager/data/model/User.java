package com.komsic.android.medmanager.data.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String email;
    private String fullName;
    private String username;
    private String uid;

    public User() {}

    public User(String email, String fullName, String username, String uid) {
        this.email = email;
        this.fullName = fullName;
        this.username = username;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return uid;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("fullName", fullName);
        result.put("username", username);
        result.put("uid", uid);

        return result;
    }
}
