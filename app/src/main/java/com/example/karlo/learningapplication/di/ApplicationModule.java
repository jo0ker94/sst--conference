package com.example.karlo.learningapplication.di;

import android.content.Context;

import com.example.karlo.learningapplication.App;
import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.database.user.LocalUserDataSource;
import com.example.karlo.learningapplication.database.user.UserDao;
import com.example.karlo.learningapplication.database.user.UserDataSource;
import com.example.karlo.learningapplication.database.user.UserDatabase;
import com.example.karlo.learningapplication.servertasks.RetrofitUtil;
import com.example.karlo.learningapplication.servertasks.interfaces.Api;

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
    public UserDao providesUserDao() {
        return UserDatabase.getDatabase(application).userModel();
    }

    @Provides
    @Singleton
    public UserDataSource providesLocalUserDataSource(UserDao userDao) {
        return new LocalUserDataSource(userDao);
    }

    @Provides
    @Singleton
    public Api providesApi() {
        return RetrofitUtil.getRetrofit(Constants.FIREBASE_BASE_URL)
                .create(Api.class);
    }
}
