package com.example.karlo.sstconference.di;

import com.example.karlo.sstconference.App;
import com.example.karlo.sstconference.modules.gallery.GalleryActivity;
import com.example.karlo.sstconference.modules.home.HomeActivity;
import com.example.karlo.sstconference.modules.login.LoginActivity;
import com.example.karlo.sstconference.modules.program.ProgramActivity;
import com.example.karlo.sstconference.modules.program.fragments.BaseProgramFragment;
import com.example.karlo.sstconference.modules.search.SearchActivity;
import com.example.karlo.sstconference.modules.subscribed.SubscriptionActivity;
import com.example.karlo.sstconference.modules.venue.fragments.BaseMapFragment;

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
    void inject(GalleryActivity target);
    void inject(ProgramActivity target);
    void inject(BaseProgramFragment target);
    void inject(SearchActivity target);
    void inject(SubscriptionActivity target);
    void inject(BaseMapFragment target);
}
