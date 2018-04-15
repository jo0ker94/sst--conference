package com.example.karlo.learningapplication.di;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {

    @Singleton
    @Provides
    public FirebaseAuth providesFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }
}
