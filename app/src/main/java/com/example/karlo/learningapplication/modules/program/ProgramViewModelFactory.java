package com.example.karlo.learningapplication.modules.program;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.learningapplication.database.program.ProgramDataSource;
import com.example.karlo.learningapplication.database.user.UserDataSource;
import com.example.karlo.learningapplication.servertasks.interfaces.Api;
import com.example.karlo.learningapplication.servertasks.interfaces.ProgramApi;

import javax.inject.Inject;

public class ProgramViewModelFactory implements ViewModelProvider.Factory {

    private final Api mApi;
    private final UserDataSource mUserDataSource;
    private final ProgramDataSource mProgramDataSource;


    @Inject
    public ProgramViewModelFactory(Api api, UserDataSource userDataSource, ProgramDataSource programDataSource) {
        this.mApi = api;
        this.mUserDataSource = userDataSource;
        this.mProgramDataSource = programDataSource;
    }

    @Override
    public ProgramViewModel create(Class modelClass) {
        return new ProgramViewModel(mApi, mUserDataSource, mProgramDataSource);
    }
}