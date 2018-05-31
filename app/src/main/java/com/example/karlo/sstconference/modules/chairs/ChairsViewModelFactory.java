package com.example.karlo.sstconference.modules.chairs;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.sstconference.database.chairs.ChairsDataSource;

import javax.inject.Inject;

public class ChairsViewModelFactory implements ViewModelProvider.Factory {

    private final ChairsDataSource mDataSource;

    @Inject
    public ChairsViewModelFactory(ChairsDataSource dataSource) {
        this.mDataSource = dataSource;
    }

    @Override
    public ChairsViewModel create(Class modelClass) {
        return new ChairsViewModel(mDataSource);
    }
}