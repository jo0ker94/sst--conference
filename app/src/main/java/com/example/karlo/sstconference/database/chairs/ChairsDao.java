package com.example.karlo.sstconference.database.chairs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.karlo.sstconference.models.ConferenceChair;

import java.util.List;

import io.reactivex.Maybe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ChairsDao {

    @Query("SELECT * FROM ConferenceChair")
    Maybe<List<ConferenceChair>> getConferenceChairs();

    @Insert(onConflict = REPLACE)
    long insertConferenceChair(ConferenceChair conferenceChair);

    @Delete
    void deleteConferenceChair(ConferenceChair conferenceChair);
}
