package com.example.karlo.sstconference.di;

import com.example.karlo.sstconference.ui.HomeActivityTest;
import com.example.karlo.sstconference.ui.LoginActivityTest;
import com.example.karlo.sstconference.ui.SearchActivityTest;
import com.example.karlo.sstconference.ui.SubscribedActivityTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { MockApplicationModule.class })
public interface MockApplicationComponent extends ApplicationComponent {

    void inject(LoginActivityTest target);
    void inject(HomeActivityTest target);
    void inject(SearchActivityTest target);
    void inject(SubscribedActivityTest target);

}
