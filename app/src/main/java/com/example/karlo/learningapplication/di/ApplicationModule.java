package com.example.karlo.learningapplication.di;

import android.content.Context;

import com.example.karlo.learningapplication.App;
import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.database.LocalDatabase;
import com.example.karlo.learningapplication.database.program.LocalProgramDataSource;
import com.example.karlo.learningapplication.database.program.ProgramDataSource;
import com.example.karlo.learningapplication.database.topic.LocalTopicDataSource;
import com.example.karlo.learningapplication.database.topic.TopicDao;
import com.example.karlo.learningapplication.database.topic.TopicDataSource;
import com.example.karlo.learningapplication.database.track.LocalTrackDataSource;
import com.example.karlo.learningapplication.database.track.TrackDao;
import com.example.karlo.learningapplication.database.track.TrackDataSource;
import com.example.karlo.learningapplication.database.user.LocalUserDataSource;
import com.example.karlo.learningapplication.database.user.UserDao;
import com.example.karlo.learningapplication.database.user.UserDataSource;
import com.example.karlo.learningapplication.servertasks.RetrofitUtil;
import com.example.karlo.learningapplication.servertasks.interfaces.Api;
import com.example.karlo.learningapplication.servertasks.interfaces.MapsApi;
import com.example.karlo.learningapplication.servertasks.interfaces.ProgramApi;
import com.example.karlo.learningapplication.servertasks.interfaces.UserApi;

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
        return LocalDatabase.getDatabase(application).userModel();
    }

    @Provides
    @Singleton
    public TopicDao providesTopicDao() {
        return LocalDatabase.getDatabase(application).topicModel();
    }

    @Provides
    @Singleton
    public TrackDao providesTrackDao() {
        return LocalDatabase.getDatabase(application).trackModel();
    }

    @Provides
    @Singleton
    public UserDataSource providesLocalUserDataSource(UserDao userDao, UserApi userApi) {
        return new LocalUserDataSource(userDao, userApi);
    }

    @Provides
    @Singleton
    public TopicDataSource providesLocalTopicDataSource(TopicDao topicDao, ProgramApi programApi) {
        return new LocalTopicDataSource(topicDao, programApi);
    }

    @Provides
    @Singleton
    public TrackDataSource providesLocalTrackDataSource(TrackDao trackDao, ProgramApi programApi) {
        return new LocalTrackDataSource(trackDao, programApi);
    }

    @Provides
    @Singleton
    public ProgramDataSource providesLocalProgramDataSource(TopicDataSource topicDataSource, TrackDataSource trackDataSource) {
        return new LocalProgramDataSource(topicDataSource, trackDataSource);
    }

    @Provides
    @Singleton
    public Api providesApi() {
        return RetrofitUtil.getRetrofit(Constants.FIREBASE_BASE_URL)
                .create(Api.class);
    }

    @Provides
    @Singleton
    public ProgramApi providesProgramApi() {
        return RetrofitUtil.getRetrofit(Constants.FIREBASE_BASE_URL)
                .create(ProgramApi.class);
    }

    @Provides
    @Singleton
    public UserApi providesUserApi() {
        return RetrofitUtil.getRetrofit(Constants.FIREBASE_BASE_URL)
                .create(UserApi.class);
    }

    @Provides
    @Singleton
    public MapsApi providesMapsApi() {
        return RetrofitUtil.getRetrofit(Constants.GOOGLE_PLACES_BASE_URL)
                .create(MapsApi.class);
    }
}
