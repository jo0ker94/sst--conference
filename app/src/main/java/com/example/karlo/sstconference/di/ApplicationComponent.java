package com.example.karlo.sstconference.di;

import com.example.karlo.sstconference.App;
import com.example.karlo.sstconference.modules.chairs.ChairsActivity;
import com.example.karlo.sstconference.modules.committee.CommitteeActivity;
import com.example.karlo.sstconference.modules.home.HomeActivity;
import com.example.karlo.sstconference.modules.keynotespeakers.KeynoteActivity;
import com.example.karlo.sstconference.modules.login.LoginActivity;
import com.example.karlo.sstconference.modules.program.ProgramActivity;
import com.example.karlo.sstconference.modules.program.fragments.BaseProgramFragment;
import com.example.karlo.sstconference.modules.search.SearchActivity;
import com.example.karlo.sstconference.modules.subscribed.SubscriptionActivity;
import com.example.karlo.sstconference.modules.venue.fragments.BaseMapFragment;
import com.example.karlo.sstconference.service.SendReminderService;

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

    void inject(LoginActivity target);
    void inject(HomeActivity target);
    void inject(ProgramActivity target);
    void inject(SearchActivity target);
    void inject(SubscriptionActivity target);
    void inject(CommitteeActivity target);
    void inject(KeynoteActivity target);
    void inject(ChairsActivity target);

    void inject(BaseProgramFragment target);
    void inject(BaseMapFragment target);

    void inject(SendReminderService target);
}
