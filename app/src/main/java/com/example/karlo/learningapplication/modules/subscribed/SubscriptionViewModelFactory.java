package com.example.karlo.learningapplication.modules.subscribed;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.learningapplication.database.program.ProgramDataSource;
import com.example.karlo.learningapplication.database.user.UserDataSource;

import javax.inject.Inject;

public class SubscriptionViewModelFactory implements ViewModelProvider.Factory {

    private UserDataSource mUserDataSource;
    private final ProgramDataSource mDataSource;

    @Inject
    public SubscriptionViewModelFactory(UserDataSource userDataSource, ProgramDataSource dataSource) {
        this.mUserDataSource = userDataSource;
        this.mDataSource = dataSource;
    }

    @Override
    public SubscriptionViewModel create(Class modelClass) {
        return new SubscriptionViewModel(mUserDataSource, mDataSource);
    }
}