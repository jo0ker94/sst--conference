package com.example.karlo.learningapplication.modules.home;

import com.example.karlo.learningapplication.commons.BaseView;
import com.example.karlo.learningapplication.models.ConferenceChair;
import com.example.karlo.learningapplication.models.User;

import java.util.List;

/**
 * Created by Karlo on 26.3.2018..
 */

public interface HomeView extends BaseView {
    void showData(List<ConferenceChair> chairs);
    void bindUser(User user);
    void goToSearch();
    void logOut();
}
