package com.example.karlo.sstconference.modules.home;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.sstconference.database.user.LocalUserDataSource;

import javax.inject.Inject;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final LocalUserDataSource localUserDataSource;

    @Inject
    public HomeViewModelFactory(LocalUserDataSource localUserDataSource) {
        this.localUserDataSource = localUserDataSource;
    }

    @Override
    public HomeViewModel create(Class modelClass) {
        return new HomeViewModel(localUserDataSource);
    }
}