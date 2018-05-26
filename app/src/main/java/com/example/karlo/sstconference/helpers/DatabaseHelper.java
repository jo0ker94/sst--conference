package com.example.karlo.sstconference.helpers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Karlo on 25.3.2018..
 */

public class DatabaseHelper {

    private final static FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static DatabaseReference getUserReference() {
        return database.getReference("users");
    }

    public static DatabaseReference getImagesReference() {
        return database.getReference("2017/images/");
    }

    public static DatabaseReference getCommentsReference() {
        return database.getReference("2017/comments/");
    }
}
