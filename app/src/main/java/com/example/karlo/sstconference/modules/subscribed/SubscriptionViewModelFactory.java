package com.example.karlo.sstconference.modules.subscribed;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.sstconference.database.program.ProgramDataSource;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

public class SubscriptionViewModelFactory implements ViewModelProvider.Factory {

    private UserDataSource mUserDataSource;
    private final ProgramDataSource mDataSource;
    private final FirebaseDatabase mFirebaseDatabase;

    @Inject
    public SubscriptionViewModelFactory(UserDataSource userDataSource, ProgramDataSource dataSource, FirebaseDatabase firebaseDatabase) {
        this.mUserDataSource = userDataSource;
        this.mDataSource = dataSource;
        this.mFirebaseDatabase = firebaseDatabase;
    }

    @Override
    public SubscriptionViewModel create(Class modelClass) {
        return new SubscriptionViewModel(mUserDataSource, mDataSource, mFirebaseDatabase);
    }
}