package com.example.karlo.learningapplication.modules.home;

import com.example.karlo.learningapplication.commons.CommonView;
import com.example.karlo.learningapplication.models.User;

import java.util.List;

/**
 * Created by Karlo on 26.3.2018..
 */

public interface HomeView extends CommonView {
    void showData(List<String> data);
    void bindUser(User user);
    void goToSearch();
    void logOut();
}
