package com.example.karlo.sstconference.modules.program;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.sstconference.database.program.ProgramDataSource;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

public class ProgramViewModelFactory implements ViewModelProvider.Factory {

    private final UserDataSource mUserDataSource;
    private final ProgramDataSource mProgramDataSource;
    private final FirebaseDatabase mFirebaseDatabase;


    @Inject
    public ProgramViewModelFactory(UserDataSource userDataSource, ProgramDataSource programDataSource, FirebaseDatabase firebaseDatabase) {
        this.mUserDataSource = userDataSource;
        this.mProgramDataSource = programDataSource;
        this.mFirebaseDatabase = firebaseDatabase;
    }

    @Override
    public ProgramViewModel create(Class modelClass) {
        return new ProgramViewModel(mUserDataSource, mProgramDataSource, mFirebaseDatabase);
    }
}