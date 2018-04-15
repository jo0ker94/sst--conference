package com.example.karlo.learningapplication.di;

import com.example.karlo.learningapplication.App;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        NetworkModule.class,
        StorageModule.class
})
public interface ApplicationComponent {
    void inject(App target);
}
