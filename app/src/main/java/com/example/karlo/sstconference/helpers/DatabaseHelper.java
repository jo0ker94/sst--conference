package com.example.karlo.sstconference.helpers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Karlo on 25.3.2018..
 */

public class DatabaseHelper {

    private static FirebaseDatabase INSTANCE = null;

    private static FirebaseDatabase getDatabaseHelper(FirebaseDatabase firebaseDatabase) {
        if (INSTANCE == null) {
            INSTANCE = firebaseDatabase;
        }
        return INSTANCE;
    }

    public static DatabaseReference getUserReference(FirebaseDatabase firebaseDatabase) {
        return getDatabaseHelper(firebaseDatabase)
                .getReference("users");
    }

    public static DatabaseReference getImagesReference(FirebaseDatabase firebaseDatabase) {
        return getDatabaseHelper(firebaseDatabase)
                .getReference("2017/images/");
    }

    public static DatabaseReference getCommentsReference(FirebaseDatabase firebaseDatabase) {
        return getDatabaseHelper(firebaseDatabase)
                .getReference("2017/comments/");
    }
}
