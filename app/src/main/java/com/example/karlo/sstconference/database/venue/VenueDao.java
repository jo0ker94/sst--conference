package com.example.karlo.sstconference.database.venue;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.karlo.sstconference.models.venue.Venue;

import io.reactivex.Maybe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface VenueDao {

    @Query("SELECT * FROM Venue")
    Maybe<Venue> getVenue();

    @Insert(onConflict = REPLACE)
    long insertVenue(Venue venue);

    @Delete
    void deleteVenue(Venue venue);
}
