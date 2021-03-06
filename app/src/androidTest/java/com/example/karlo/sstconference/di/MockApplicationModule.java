package com.example.karlo.sstconference.di;

import com.example.karlo.sstconference.App;
import com.example.karlo.sstconference.modules.chairs.ChairsViewModel;
import com.example.karlo.sstconference.modules.committee.CommitteeViewModel;
import com.example.karlo.sstconference.modules.gallery.GalleryViewModel;
import com.example.karlo.sstconference.modules.home.HomeViewModel;
import com.example.karlo.sstconference.modules.keynotespeakers.KeynoteViewModel;
import com.example.karlo.sstconference.modules.login.LoginViewModel;
import com.example.karlo.sstconference.modules.program.ProgramViewModel;
import com.example.karlo.sstconference.modules.search.SearchViewModel;
import com.example.karlo.sstconference.modules.subscribed.SubscriptionViewModel;
import com.example.karlo.sstconference.modules.venue.VenueViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MockApplicationModule extends ApplicationModule {

    public MockApplicationModule(App application) {
        super(application);
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

    @Provides
    @Singleton
    public SearchViewModel provideSearchViewModel() {
        return Mockito.mock(SearchViewModel.class);
    }

    @Provides
    @Singleton
    public SubscriptionViewModel provideSubscriptionViewModel() {
        return Mockito.mock(SubscriptionViewModel.class);
    }

    @Provides
    @Singleton
    public KeynoteViewModel provideKeynoteViewModel() {
        return Mockito.mock(KeynoteViewModel.class);
    }

    @Provides
    @Singleton
    public ChairsViewModel provideChairsViewModel() {
        return Mockito.mock(ChairsViewModel.class);
    }

    @Provides
    @Singleton
    public CommitteeViewModel provideCommitteeViewModel() {
        return Mockito.mock(CommitteeViewModel.class);
    }

    @Provides
    @Singleton
    public GalleryViewModel provideGalleryViewModel() {
        return Mockito.mock(GalleryViewModel.class);
    }

    @Provides
    @Singleton
    public VenueViewModel provideVenueViewModel() {
        return Mockito.mock(VenueViewModel.class);
    }

    @Provides
    @Singleton
    public ProgramViewModel provideProgramViewModel() {
        return Mockito.mock(ProgramViewModel.class);
    }

    @Provides
    @Singleton
    public FirebaseAuth provideFirebaseAuth() {
        return Mockito.mock(FirebaseAuth.class);
    }

    @Provides
    @Singleton
    public FirebaseStorage provideFirebaseStorage() {
        return Mockito.mock(FirebaseStorage.class);
    }

    @Provides
    @Singleton
    public FirebaseDatabase provideFirebaseDatabase() {
        return Mockito.mock(FirebaseDatabase.class);
    }
}