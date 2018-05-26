package com.example.karlo.sstconference.database.track;

import com.example.karlo.sstconference.models.program.Track;
import com.example.karlo.sstconference.servertasks.interfaces.ProgramApi;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class LocalTrackDataSource implements TrackDataSource {

    private TrackDao mTrackDao;
    private ProgramApi mApi;

    public LocalTrackDataSource(TrackDao mTrackDao, ProgramApi mApi) {
        this.mTrackDao = mTrackDao;
        this.mApi = mApi;
    }

    @Override
    public Observable<List<Track>> getTracks() {
        return Observable.concat(getTracksFromDatabase(),
                getTracksFromApi().onErrorResumeNext(Observable.empty()));
    }

    private Observable<List<Track>> getTracksFromDatabase() {
        return mTrackDao.getTracks()
                .toObservable();
    }

    private Observable<List<Track>> getTracksFromApi() {
        return mApi.getTracks()
                .flatMap(Observable::fromIterable)
                .subscribeOn(Schedulers.newThread())
                .doOnNext(this::insertOrUpdateTrack)
                .toList()
                .toObservable();
    }

    @Override
    public void insertOrUpdateTrack(Track track) {
        mTrackDao.insertTrack(track);
    }

    @Override
    public void deleteTrack(Track track) {
        mTrackDao.deleteTrack(track);
    }
}
