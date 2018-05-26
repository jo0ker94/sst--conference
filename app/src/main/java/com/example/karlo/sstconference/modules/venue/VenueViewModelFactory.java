package com.example.karlo.sstconference.modules.venue;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.sstconference.servertasks.interfaces.Api;
import com.example.karlo.sstconference.servertasks.interfaces.MapsApi;

import javax.inject.Inject;

public class VenueViewModelFactory implements ViewModelProvider.Factory {

    private Api mApi;
    private MapsApi mMapsApi;

    @Inject
    public VenueViewModelFactory(Api api, MapsApi mapsApi) {
        this.mApi = api;
        this.mMapsApi = mapsApi;
    }

    @Override
    public VenueViewModel create(Class modelClass) {
        return new VenueViewModel(mApi, mMapsApi);
    }
}