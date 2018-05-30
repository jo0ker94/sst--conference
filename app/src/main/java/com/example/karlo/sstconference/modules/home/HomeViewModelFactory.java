package com.example.karlo.sstconference.modules.home;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.sstconference.database.program.ProgramDataSource;
import com.example.karlo.sstconference.database.user.LocalUserDataSource;

import javax.inject.Inject;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final LocalUserDataSource mLocalUserDataSource;
    private final ProgramDataSource mProgramDataSource;

    @Inject
    public HomeViewModelFactory(LocalUserDataSource localUserDataSource, ProgramDataSource programDataSource) {
        this.mLocalUserDataSource = localUserDataSource;
        this.mProgramDataSource = programDataSource;
    }

    @Override
    public HomeViewModel create(Class modelClass) {
        return new HomeViewModel(mLocalUserDataSource, mProgramDataSource);
    }
}