package com.example.karlo.sstconference.modules.venue;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.sstconference.database.venue.VenueDataSource;
import com.example.karlo.sstconference.servertasks.interfaces.MapsApi;
import com.example.karlo.sstconference.servertasks.interfaces.VenueApi;

import javax.inject.Inject;

public class VenueViewModelFactory implements ViewModelProvider.Factory {

    private VenueDataSource mVenueDataSource;
    private MapsApi mMapsApi;

    @Inject
    public VenueViewModelFactory(VenueDataSource venueApi, MapsApi mapsApi) {
        this.mVenueDataSource = venueApi;
        this.mMapsApi = mapsApi;
    }

    @Override
    public VenueViewModel create(Class modelClass) {
        return new VenueViewModel(mVenueDataSource, mMapsApi);
    }
}