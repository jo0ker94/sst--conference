package com.example.karlo.learningapplication.di;

import android.content.Context;

import com.example.karlo.learningapplication.App;
import com.example.karlo.learningapplication.modules.login.LoginPresenter;
import com.example.karlo.learningapplication.modules.login.LoginView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private App application;

    public ApplicationModule(App application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    public LoginPresenter provideLoginPresenter() {
        return new LoginPresenter();
    }
}
