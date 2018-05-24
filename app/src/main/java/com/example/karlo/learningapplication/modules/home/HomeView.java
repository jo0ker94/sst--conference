package com.example.karlo.learningapplication.modules.home;

import com.example.karlo.learningapplication.commons.BaseView;
import com.example.karlo.learningapplication.models.ConferenceChair;
import com.example.karlo.learningapplication.models.User;
import com.example.karlo.learningapplication.models.program.Topic;
import com.example.karlo.learningapplication.models.program.Track;

import java.util.List;

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
