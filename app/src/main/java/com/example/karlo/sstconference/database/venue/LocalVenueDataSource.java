package com.example.karlo.sstconference.database.venue;

import com.example.karlo.sstconference.models.venue.Venue;
import com.example.karlo.sstconference.servertasks.interfaces.VenueApi;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class LocalVenueDataSource implements VenueDataSource {

    private VenueDao mVenueDao;
    private VenueApi mApi;

    public LocalVenueDataSource(VenueDao mVenueDao, VenueApi mApi) {
        this.mVenueDao = mVenueDao;
        this.mApi = mApi;
    }

    @Override
    public Observable<Venue> getVenue() {
        return Observable.concat(getVenueFromDatabase(),
                getVenueFromApi().onErrorResumeNext(Observable.empty()));
    }

    private Observable<Venue> getVenueFromDatabase() {
        return mVenueDao.getVenue()
                .toObservable();
    }

    private Observable<Venue> getVenueFromApi() {
        return mApi.getVenue()
                .subscribeOn(Schedulers.newThread())
                .doOnNext(this::insertOrUpdateVenue)
                .firstElement()
                .toObservable();
    }

    @Override
    public void insertOrUpdateVenue(Venue venue) {
        mVenueDao.insertVenue(venue);
    }

    @Override
    public void deleteVenue(Venue venue) {
        mVenueDao.deleteVenue(venue);
    }
}
