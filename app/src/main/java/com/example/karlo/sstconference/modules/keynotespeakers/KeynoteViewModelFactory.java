package com.example.karlo.sstconference.modules.keynotespeakers;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.sstconference.database.keynote.KeynoteDataSource;

import javax.inject.Inject;

public class KeynoteViewModelFactory implements ViewModelProvider.Factory {

    private final KeynoteDataSource mKeynoteDataSource;

    @Inject
    public KeynoteViewModelFactory(KeynoteDataSource keynoteDataSource) {
        this.mKeynoteDataSource = keynoteDataSource;
    }

    @Override
    public KeynoteViewModel create(Class modelClass) {
        return new KeynoteViewModel(mKeynoteDataSource);
    }
}