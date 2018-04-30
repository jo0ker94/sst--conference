package com.example.karlo.learningapplication.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.karlo.learningapplication.models.User;

import io.reactivex.Flowable;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User LIMIT 1")
    Flowable<User> getUser();

    @Insert(onConflict = REPLACE)
    long insertUser(User user);

    @Delete
    void deleteUser(User user);
}
