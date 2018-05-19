package com.example.karlo.learningapplication.database.track;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.karlo.learningapplication.models.program.Track;

import java.util.List;

import io.reactivex.Maybe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TrackDao {

    @Query("SELECT * FROM Track")
    Maybe<List<Track>> getTracks();

    @Insert(onConflict = REPLACE)
    long insertTrack(Track track);

    @Delete
    void deleteTrack(Track track);
}
