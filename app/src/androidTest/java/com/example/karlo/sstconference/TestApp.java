package com.example.karlo.sstconference;

import com.example.karlo.sstconference.di.DaggerMockApplicationComponent;
import com.example.karlo.sstconference.di.MockApplicationComponent;
import com.example.karlo.sstconference.di.MockApplicationModule;

public class TestApp extends App {

    private MockApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerMockApplicationComponent.builder()
                .mockApplicationModule(new MockApplicationModule(this))
                .build();

        component.inject(this);
    }

    @Override
    public MockApplicationComponent getComponent() {
        return component;
    }
}
