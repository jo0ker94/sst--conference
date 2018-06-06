package com.example.karlo.sstconference.di;

import com.example.karlo.sstconference.App;
import com.example.karlo.sstconference.modules.chairs.ChairsViewModel;
import com.example.karlo.sstconference.modules.committee.CommitteeViewModel;
import com.example.karlo.sstconference.modules.home.HomeViewModel;
import com.example.karlo.sstconference.modules.keynotespeakers.KeynoteViewModel;
import com.example.karlo.sstconference.modules.login.LoginViewModel;
import com.example.karlo.sstconference.modules.search.SearchViewModel;
import com.example.karlo.sstconference.modules.subscribed.SubscriptionViewModel;

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
}