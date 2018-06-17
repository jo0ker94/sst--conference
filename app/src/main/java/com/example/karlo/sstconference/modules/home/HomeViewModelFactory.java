package com.example.karlo.sstconference.modules.home;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.sstconference.database.program.ProgramDataSource;
import com.example.karlo.sstconference.database.user.LocalUserDataSource;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final LocalUserDataSource mLocalUserDataSource;
    private final FirebaseAuth mFirebaseAuth;

    @Inject
    public HomeViewModelFactory(LocalUserDataSource localUserDataSource, FirebaseAuth firebaseAuth) {
        this.mLocalUserDataSource = localUserDataSource;
        this.mFirebaseAuth = firebaseAuth;
    }

    @Override
    public HomeViewModel create(Class modelClass) {
        return new HomeViewModel(mLocalUserDataSource, mFirebaseAuth);
    }
}