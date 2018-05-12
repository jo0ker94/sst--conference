package com.example.karlo.learningapplication.modules.gallery;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import javax.inject.Inject;

public class GalleryViewModelFactory implements ViewModelProvider.Factory {

    private final Context application;

    @Inject
    public GalleryViewModelFactory(Context application) {
        this.application = application;
    }

    @Override
    public GalleryViewModel create(Class modelClass) {
        return new GalleryViewModel(application);
    }
}