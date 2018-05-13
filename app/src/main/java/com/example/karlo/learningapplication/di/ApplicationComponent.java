package com.example.karlo.learningapplication.di;

import com.example.karlo.learningapplication.App;
import com.example.karlo.learningapplication.modules.gallery.GalleryActivity;
import com.example.karlo.learningapplication.modules.home.HomeActivity;
import com.example.karlo.learningapplication.modules.login.LoginActivity;
import com.example.karlo.learningapplication.modules.program.BaseProgramFragment;
import com.example.karlo.learningapplication.modules.program.ProgramActivity;

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
}
