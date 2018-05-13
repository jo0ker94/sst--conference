package com.example.karlo.learningapplication.modules.program;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.learningapplication.servertasks.interfaces.Api;

import javax.inject.Inject;

public class ProgramViewModelFactory implements ViewModelProvider.Factory {

    private final Api mApi;

    @Inject
    public ProgramViewModelFactory(Api api) {
        this.mApi = api;
    }

    @Override
    public ProgramViewModel create(Class modelClass) {
        return new ProgramViewModel(mApi);
    }
}