package com.example.karlo.learningapplication.modules.program;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.learningapplication.database.user.UserDataSource;
import com.example.karlo.learningapplication.servertasks.interfaces.Api;

import javax.inject.Inject;

public class ProgramViewModelFactory implements ViewModelProvider.Factory {

    private final Api mApi;
    private final UserDataSource mUserDataSource;

    @Inject
    public ProgramViewModelFactory(Api api, UserDataSource userDataSource) {
        this.mApi = api;
        this.mUserDataSource = userDataSource;
    }

    @Override
    public ProgramViewModel create(Class modelClass) {
        return new ProgramViewModel(mApi, mUserDataSource);
    }
}