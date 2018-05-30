package com.example.karlo.sstconference.database.keynote;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.karlo.sstconference.models.keynote.KeynoteSpeaker;

import java.util.List;

import io.reactivex.Maybe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface KeynoteDao {

    @Query("SELECT * FROM KeynoteSpeaker")
    Maybe<List<KeynoteSpeaker>> getKeynoteSpeakers();

    @Insert(onConflict = REPLACE)
    long insertKeynoteSpeaker(KeynoteSpeaker keynoteSpeaker);

    @Delete
    void deleteKeynoteSpeaker(KeynoteSpeaker keynoteSpeaker);
}
