package com.example.karlo.learningapplication;

import android.app.Application;

import com.example.karlo.learningapplication.di.ApplicationComponent;
import com.example.karlo.learningapplication.di.ApplicationModule;
import com.example.karlo.learningapplication.di.DaggerApplicationComponent;
import com.example.karlo.learningapplication.di.NetworkModule;
import com.example.karlo.learningapplication.di.StorageModule;
import com.google.android.gms.auth.api.signin.internal.Storage;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .build();

        Realm.setDefaultConfiguration(config);
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
