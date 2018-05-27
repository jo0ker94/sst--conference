package com.example.karlo.sstconference;

import android.app.Application;

import com.example.karlo.sstconference.di.ApplicationComponent;
import com.example.karlo.sstconference.di.ApplicationModule;
import com.example.karlo.sstconference.di.DaggerApplicationComponent;
import com.example.karlo.sstconference.di.NetworkModule;
import com.example.karlo.sstconference.di.StorageModule;

/**
 * Created by Karlo on 25.3.2018..
 */

public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule())
                .storageModule(new StorageModule())
                .build();

        component.inject(this);
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
