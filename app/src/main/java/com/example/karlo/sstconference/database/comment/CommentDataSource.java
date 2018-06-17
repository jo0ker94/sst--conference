package com.example.karlo.sstconference.database.comment;

import com.example.karlo.sstconference.models.program.Comment;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface CommentDataSource {

    Observable<List<Comment>> getComments(int parentId);

    void insertOrUpdateComment(Comment comment);

    Completable updateComments(List<Comment> comment);

    void deleteComment(Comment comment);
}
