package com.example.karlo.learningapplication.modules.search;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.learningapplication.database.program.ProgramDataSource;
import com.example.karlo.learningapplication.servertasks.interfaces.Api;

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