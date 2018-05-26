package com.example.karlo.sstconference.modules.program;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.sstconference.database.program.ProgramDataSource;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.example.karlo.sstconference.servertasks.interfaces.Api;

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