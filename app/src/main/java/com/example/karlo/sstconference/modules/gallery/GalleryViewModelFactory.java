package com.example.karlo.sstconference.modules.gallery;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.sstconference.database.gallery.GalleryDataSource;
import com.google.firebase.storage.FirebaseStorage;

import javax.inject.Inject;

public class GalleryViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private GalleryDataSource mDataSource;
    private FirebaseStorage mStorage;

    @Inject
    public GalleryViewModelFactory(Application application,
                                   GalleryDataSource dataSource,
                                   FirebaseStorage storage) {
        this.mApplication = application;
        this.mDataSource = dataSource;
        this.mStorage = storage;
    }

    @Override
    public GalleryViewModel create(Class modelClass) {
        return new GalleryViewModel(mApplication, mDataSource, mStorage);
    }
}
