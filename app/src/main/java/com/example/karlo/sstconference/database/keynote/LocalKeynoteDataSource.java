package com.example.karlo.sstconference.database.keynote;

import com.example.karlo.sstconference.models.keynote.KeynoteSpeaker;
import com.example.karlo.sstconference.servertasks.interfaces.Api;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class LocalKeynoteDataSource implements KeynoteDataSource {

    private Api mApi;
    private KeynoteDao mDao;

    @Inject
    public LocalKeynoteDataSource(KeynoteDao dao, Api api) {
        this.mDao = dao;
        this.mApi = api;
    }

    @Override
    public Observable<List<KeynoteSpeaker>> getKeynoteSpeakers() {
        return Observable.concat(getKeynoteSpeakersFromDatabase(),
                getKeynoteSpeakersFromApi().onErrorResumeNext(Observable.empty()));
    }

    private Observable<List<KeynoteSpeaker>> getKeynoteSpeakersFromDatabase() {
        return mDao.getKeynoteSpeakers()
                .toObservable();
    }

    private Observable<List<KeynoteSpeaker>> getKeynoteSpeakersFromApi() {
        return mApi.getKeynoteSpeakers()
                .flatMap(Observable::fromIterable)
                .subscribeOn(Schedulers.newThread())
                .doOnNext(this::insertOrUpdateKeynoteSpeaker)
                .toList()
                .toObservable();
    }

    @Override
    public void insertOrUpdateKeynoteSpeaker(KeynoteSpeaker keynoteSpeaker) {
        mDao.insertKeynoteSpeaker(keynoteSpeaker);
    }

    @Override
    public void deleteKeynoteSpeaker(KeynoteSpeaker keynoteSpeaker) {
        mDao.deleteKeynoteSpeaker(keynoteSpeaker);
    }
}
