package com.example.karlo.sstconference.database.comment;

import com.example.karlo.sstconference.models.program.Comment;
import com.example.karlo.sstconference.servertasks.interfaces.ProgramApi;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class LocalCommentDataSource implements CommentDataSource {

    private CommentDao mDao;
    private ProgramApi mApi;

    @Inject
    public LocalCommentDataSource(CommentDao dao, ProgramApi api) {
        this.mDao = dao;
        this.mApi = api;
    }

    @Override
    public Observable<List<Comment>> getComments(int parentId) {
        return Observable.concat(getConferenceChairsFromDatabase(parentId),
                getConferenceChairsFromApi(parentId).onErrorResumeNext(Observable.empty()));
    }

    private Observable<List<Comment>> getConferenceChairsFromDatabase(int parentId) {
        return mDao.getComments(parentId)
                .toObservable();
    }

    private Observable<List<Comment>> getConferenceChairsFromApi(int parentId) {
        return mApi.getComments(parentId)
                .flatMap(Observable::fromIterable)
                .subscribeOn(Schedulers.newThread())
                .doOnNext(this::insertOrUpdateComment)
                .toList()
                .toObservable();
    }

    @Override
    public void insertOrUpdateComment(Comment comment) {
        mDao.insertComment(comment);
    }

    @Override
    public Completable updateComments(List<Comment> comment) {
        return mApi.updateComments(String.valueOf(comment.get(0).getParentId()), comment);
    }

    @Override
    public void deleteComment(Comment comment) {
        mDao.deleteComment(comment);
    }
}
