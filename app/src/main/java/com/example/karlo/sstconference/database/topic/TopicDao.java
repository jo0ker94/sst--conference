package com.example.karlo.sstconference.database.topic;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.karlo.sstconference.models.program.Topic;

import java.util.List;

import io.reactivex.Maybe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TopicDao {

    @Query("SELECT * FROM Topic")
    Maybe<List<Topic>> getTopics();

    @Insert(onConflict = REPLACE)
    long insertTopic(Topic topic);

    @Delete
    void deleteTopic(Topic topic);
}
