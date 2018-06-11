package com.example.karlo.sstconference.modules.login;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.sstconference.database.user.UserDataSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private final UserDataSource mUserDataSource;
    private final FirebaseAuth mFirebaseAuth;
    private final FirebaseDatabase mFirebaseDatabase;

    @Inject
    public LoginViewModelFactory(UserDataSource userDataSource, FirebaseAuth firebaseAuth, FirebaseDatabase firebaseDatabase) {
        this.mUserDataSource = userDataSource;
        this.mFirebaseAuth = firebaseAuth;
        this.mFirebaseDatabase = firebaseDatabase;
    }

    @Override
    public LoginViewModel create(Class modelClass) {
        return new LoginViewModel(mUserDataSource, mFirebaseAuth, mFirebaseDatabase);
    }
}