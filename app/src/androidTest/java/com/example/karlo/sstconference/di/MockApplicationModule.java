package com.example.karlo.sstconference.di;

import com.example.karlo.sstconference.App;
import com.example.karlo.sstconference.modules.home.HomeViewModel;
import com.example.karlo.sstconference.modules.login.LoginViewModel;

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
}