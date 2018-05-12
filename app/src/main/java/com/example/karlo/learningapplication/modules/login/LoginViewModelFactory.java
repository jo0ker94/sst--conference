package com.example.karlo.learningapplication.modules.login;

import android.arch.lifecycle.ViewModelProvider;

import com.example.karlo.learningapplication.database.user.UserDataSource;

import javax.inject.Inject;

public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private final UserDataSource userDataSource;

    @Inject
    public LoginViewModelFactory(UserDataSource userDataSource) {
        this.userDataSource = userDataSource;
    }

    @Override
    public LoginViewModel create(Class modelClass) {
        return new LoginViewModel(userDataSource);
    }
}