package com.example.karlo.sstconference.di;

import android.content.Context;

import com.example.karlo.sstconference.App;
import com.example.karlo.sstconference.database.chairs.ChairsDao;
import com.example.karlo.sstconference.database.chairs.ChairsDataSource;
import com.example.karlo.sstconference.database.comment.CommentDao;
import com.example.karlo.sstconference.database.comment.CommentDataSource;
import com.example.karlo.sstconference.database.committee.CommitteeDao;
import com.example.karlo.sstconference.database.committee.CommitteeDataSource;
import com.example.karlo.sstconference.database.gallery.GalleryDao;
import com.example.karlo.sstconference.database.gallery.GalleryDataSource;
import com.example.karlo.sstconference.database.keynote.KeynoteDao;
import com.example.karlo.sstconference.database.keynote.KeynoteDataSource;
import com.example.karlo.sstconference.database.program.ProgramDataSource;
import com.example.karlo.sstconference.database.topic.TopicDao;
import com.example.karlo.sstconference.database.topic.TopicDataSource;
import com.example.karlo.sstconference.database.track.TrackDao;
import com.example.karlo.sstconference.database.track.TrackDataSource;
import com.example.karlo.sstconference.database.user.UserDao;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.example.karlo.sstconference.database.venue.VenueDao;
import com.example.karlo.sstconference.database.venue.VenueDataSource;
import com.example.karlo.sstconference.modules.home.HomeViewModel;
import com.example.karlo.sstconference.modules.login.LoginViewModel;
import com.example.karlo.sstconference.servertasks.interfaces.Api;
import com.example.karlo.sstconference.servertasks.interfaces.MapsApi;
import com.example.karlo.sstconference.servertasks.interfaces.ProgramApi;
import com.example.karlo.sstconference.servertasks.interfaces.UserApi;
import com.example.karlo.sstconference.servertasks.interfaces.VenueApi;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MockApplicationModule {

    private App application;

    public MockApplicationModule(App application) {
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
        return Mockito.mock(UserDao.class);
    }

    @Provides
    @Singleton
    public TopicDao providesTopicDao() {
        return Mockito.mock(TopicDao.class);
    }

    @Provides
    @Singleton
    public TrackDao providesTrackDao() {
        return Mockito.mock(TrackDao.class);
    }

    @Provides
    @Singleton
    public CommentDao providesCommentDao() {
        return Mockito.mock(CommentDao.class);
    }

    @Provides
    @Singleton
    public VenueDao providesVenueDao() {
        return Mockito.mock(VenueDao.class);
    }

    @Provides
    @Singleton
    public CommitteeDao providesCommitteeDao() {
        return Mockito.mock(CommitteeDao.class);
    }

    @Provides
    @Singleton
    public ChairsDao providesChairsDao() {
        return Mockito.mock(ChairsDao.class);
    }

    @Provides
    @Singleton
    public KeynoteDao providesKeynoteDao() {
        return Mockito.mock(KeynoteDao.class);
    }

    @Provides
    @Singleton
    public GalleryDao providesGalleryDao() {
        return Mockito.mock(GalleryDao.class);
    }

    @Provides
    @Singleton
    public UserDataSource providesLocalUserDataSource(UserDao userDao, UserApi userApi) {
        return Mockito.mock(UserDataSource.class);
    }

    @Provides
    @Singleton
    public TopicDataSource providesLocalTopicDataSource(TopicDao topicDao, ProgramApi programApi) {
        return Mockito.mock(TopicDataSource.class);
    }

    @Provides
    @Singleton
    public TrackDataSource providesLocalTrackDataSource(TrackDao trackDao, ProgramApi programApi) {
        return Mockito.mock(TrackDataSource.class);
    }

    @Provides
    @Singleton
    public CommentDataSource providesLocalCommentDataSource(CommentDao commentDao, ProgramApi programApi) {
        return Mockito.mock(CommentDataSource.class);
    }

    @Provides
    @Singleton
    public ProgramDataSource providesLocalProgramDataSource(TopicDataSource topicDataSource, TrackDataSource trackDataSource, CommentDataSource commentDataSource) {
        return Mockito.mock(ProgramDataSource.class);
    }

    @Provides
    @Singleton
    public VenueDataSource providesLocalVenueDataSource(VenueDao venueDao, VenueApi venueApi) {
        return Mockito.mock(VenueDataSource.class);
    }

    @Provides
    @Singleton
    public CommitteeDataSource providesLocalCommitteeDataSource(CommitteeDao committeeDao, Api api) {
        return Mockito.mock(CommitteeDataSource.class);
    }

    @Provides
    @Singleton
    public ChairsDataSource providesLocalChairsDataSource(ChairsDao chairsDao, Api api) {
        return Mockito.mock(ChairsDataSource.class);
    }

    @Provides
    @Singleton
    public KeynoteDataSource providesLocalKeynoteDataSource(KeynoteDao keynoteDao, Api api) {
        return Mockito.mock(KeynoteDataSource.class);
    }

    @Provides
    @Singleton
    public GalleryDataSource providesLocalGalleryDataSource(GalleryDao galleryDao, Api api) {
        return Mockito.mock(GalleryDataSource.class);
    }

    @Provides
    @Singleton
    public Api providesApi() {
        return Mockito.mock(Api.class);
    }

    @Provides
    @Singleton
    public ProgramApi providesProgramApi() {
        return Mockito.mock(ProgramApi.class);
    }

    @Provides
    @Singleton
    public UserApi providesUserApi() {
        return Mockito.mock(UserApi.class);
    }

    @Provides
    @Singleton
    public MapsApi providesMapsApi() {
        return Mockito.mock(MapsApi.class);
    }

    @Provides
    @Singleton
    public VenueApi provideVenueApi() {
        return Mockito.mock(VenueApi.class);
    }

    @Provides
    @Singleton
    public LoginViewModel provideLoginViewModel() {
        return Mockito.mock(LoginViewModel.class);
    }

    @Provides
    @Singleton
    public HomeViewModel provideHomeViewModel() {
        return Mockito.mock(HomeViewModel.class);
    }
}