package com.example.karlo.sstconference.di;

import android.content.Context;

import com.example.karlo.sstconference.App;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.database.LocalDatabase;
import com.example.karlo.sstconference.database.committee.CommitteeDao;
import com.example.karlo.sstconference.database.committee.CommitteeDataSource;
import com.example.karlo.sstconference.database.committee.LocalCommitteeDataSource;
import com.example.karlo.sstconference.database.keynote.KeynoteDao;
import com.example.karlo.sstconference.database.keynote.KeynoteDataSource;
import com.example.karlo.sstconference.database.keynote.LocalKeynoteDataSource;
import com.example.karlo.sstconference.database.program.LocalProgramDataSource;
import com.example.karlo.sstconference.database.program.ProgramDataSource;
import com.example.karlo.sstconference.database.topic.LocalTopicDataSource;
import com.example.karlo.sstconference.database.topic.TopicDao;
import com.example.karlo.sstconference.database.topic.TopicDataSource;
import com.example.karlo.sstconference.database.track.LocalTrackDataSource;
import com.example.karlo.sstconference.database.track.TrackDao;
import com.example.karlo.sstconference.database.track.TrackDataSource;
import com.example.karlo.sstconference.database.user.LocalUserDataSource;
import com.example.karlo.sstconference.database.user.UserDao;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.example.karlo.sstconference.database.venue.LocalVenueDataSource;
import com.example.karlo.sstconference.database.venue.VenueDao;
import com.example.karlo.sstconference.database.venue.VenueDataSource;
import com.example.karlo.sstconference.servertasks.RetrofitUtil;
import com.example.karlo.sstconference.servertasks.interfaces.Api;
import com.example.karlo.sstconference.servertasks.interfaces.MapsApi;
import com.example.karlo.sstconference.servertasks.interfaces.ProgramApi;
import com.example.karlo.sstconference.servertasks.interfaces.UserApi;
import com.example.karlo.sstconference.servertasks.interfaces.VenueApi;

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
    public VenueDao providesVenueDao() {
        return LocalDatabase.getDatabase(application).venueModel();
    }

    @Provides
    @Singleton
    public CommitteeDao providesCommitteeDao() {
        return LocalDatabase.getDatabase(application).committeeModel();
    }

    @Provides
    @Singleton
    public KeynoteDao providesKeynoteDao() {
        return LocalDatabase.getDatabase(application).keynoteModel();
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
    public VenueDataSource providesLocalVenueDataSource(VenueDao venueDao, VenueApi venueApi) {
        return new LocalVenueDataSource(venueDao, venueApi);
    }

    @Provides
    @Singleton
    public CommitteeDataSource providesLocalCommitteeDataSource(CommitteeDao committeeDao, Api api) {
        return new LocalCommitteeDataSource(committeeDao, api);
    }

    @Provides
    @Singleton
    public KeynoteDataSource providesLocalKeynoteDataSource(KeynoteDao keynoteDao, Api api) {
        return new LocalKeynoteDataSource(keynoteDao, api);
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

    @Provides
    @Singleton
    public VenueApi providesVenueApi() {
        return RetrofitUtil.getRetrofit(Constants.FIREBASE_BASE_URL)
                .create(VenueApi.class);
    }
}
