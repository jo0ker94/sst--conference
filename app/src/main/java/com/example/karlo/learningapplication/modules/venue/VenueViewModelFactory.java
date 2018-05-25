package com.example.karlo.learningapplication.modules.venue;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.learningapplication.servertasks.interfaces.MapsApi;

import javax.inject.Inject;

public class VenueViewModelFactory implements ViewModelProvider.Factory {

    private MapsApi mApi;

    @Inject
    public VenueViewModelFactory(MapsApi api) {
        this.mApi = api;
    }

    @Override
    public VenueViewModel create(Class modelClass) {
        return new VenueViewModel(mApi);
    }
}