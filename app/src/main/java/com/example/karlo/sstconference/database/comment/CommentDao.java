package com.example.karlo.sstconference.database.comment;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.karlo.sstconference.models.program.Comment;

import java.util.List;

import io.reactivex.Maybe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CommentDao {

    @Query("SELECT * FROM Comment WHERE mParentId == :parentId")
    Maybe<List<Comment>> getComments(int parentId);

    @Insert(onConflict = REPLACE)
    long insertComment(Comment comment);

    @Delete
    void deleteComment(Comment comment);
}
