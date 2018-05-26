package com.example.karlo.sstconference.modules.search;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.sstconference.database.program.ProgramDataSource;
import com.example.karlo.sstconference.servertasks.interfaces.Api;

import javax.inject.Inject;

public class SearchViewModelFactory implements ViewModelProvider.Factory {

    private final ProgramDataSource mDataSource;

    @Inject
    public SearchViewModelFactory(Api api, ProgramDataSource dataSource) {
        this.mDataSource = dataSource;
    }

    @Override
    public SearchViewModel create(Class modelClass) {
        return new SearchViewModel(mDataSource);
    }
}