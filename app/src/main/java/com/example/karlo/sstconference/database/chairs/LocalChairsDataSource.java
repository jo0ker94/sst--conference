package com.example.karlo.sstconference.database.chairs;

import com.example.karlo.sstconference.models.ConferenceChair;
import com.example.karlo.sstconference.servertasks.interfaces.Api;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class LocalChairsDataSource implements ChairsDataSource {

    private ChairsDao mDao;
    private Api mApi;

    public LocalChairsDataSource(ChairsDao dao, Api api) {
        this.mDao = dao;
        this.mApi = api;
    }

    @Override
    public Observable<List<ConferenceChair>> getConferenceChairs() {
        return Observable.concat(getConferenceChairsFromDatabase(),
                getConferenceChairsFromApi().onErrorResumeNext(Observable.empty()));
    }

    private Observable<List<ConferenceChair>> getConferenceChairsFromDatabase() {
        return mDao.getConferenceChairs()
                .toObservable();
    }

    private Observable<List<ConferenceChair>> getConferenceChairsFromApi() {
        return mApi.getChairs()
                .flatMap(Observable::fromIterable)
                .subscribeOn(Schedulers.newThread())
                .doOnNext(this::insertConferenceChair)
                .toList()
                .toObservable();
    }

    @Override
    public void insertConferenceChair(ConferenceChair conferenceChair) {
        mDao.insertConferenceChair(conferenceChair);
    }

    @Override
    public void deleteConferenceChair(ConferenceChair conferenceChair) {
        mDao.deleteConferenceChair(conferenceChair);
    }

}
