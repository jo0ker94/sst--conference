package com.example.karlo.learningapplication.modules.search;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.learningapplication.servertasks.interfaces.Api;

import javax.inject.Inject;

public class SearchViewModelFactory implements ViewModelProvider.Factory {

    private final Api mApi;

    @Inject
    public SearchViewModelFactory(Api api) {
        this.mApi = api;
    }

    @Override
    public SearchViewModel create(Class modelClass) {
        return new SearchViewModel(mApi);
    }
}