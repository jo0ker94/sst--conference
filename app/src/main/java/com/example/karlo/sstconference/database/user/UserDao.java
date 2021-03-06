package com.example.karlo.sstconference.database.user;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.karlo.sstconference.models.User;

import io.reactivex.Maybe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User LIMIT 1")
    Maybe<User> getUser();

    @Insert(onConflict = REPLACE)
    long insertUser(User user);

    @Delete
    void deleteUser(User user);
}
