package com.example.karlo.learningapplication.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.karlo.learningapplication.database.topic.TopicDao;
import com.example.karlo.learningapplication.database.track.TrackDao;
import com.example.karlo.learningapplication.database.user.UserDao;
import com.example.karlo.learningapplication.models.User;
import com.example.karlo.learningapplication.models.program.Topic;
import com.example.karlo.learningapplication.models.program.Track;

@Database(entities = {
        User.class,
        Topic.class,
        Track.class
}, version = 1, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {

    private static LocalDatabase INSTANCE;

    public static LocalDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDatabase.class, "user.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract UserDao userModel();

    public abstract TopicDao topicModel();

    public abstract TrackDao trackModel();
}
