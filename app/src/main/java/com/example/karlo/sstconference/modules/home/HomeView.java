package com.example.karlo.sstconference.modules.home;

import com.example.karlo.sstconference.base.BaseView;
import com.example.karlo.sstconference.models.User;

/**
 * Created by Karlo on 26.3.2018..
 */

public interface HomeView extends BaseView {
    void bindUser(User user);
    void goToSearch();
    void goToProgram();
    void goToGallery();
    void goToSubscribed();
    void goToVenue();
    void logOut();
}
