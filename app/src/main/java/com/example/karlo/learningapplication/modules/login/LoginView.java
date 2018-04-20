package com.example.karlo.learningapplication.modules.login;

import com.example.karlo.learningapplication.commons.BaseView;
import com.example.karlo.learningapplication.models.LoginRequest;

/**
 * Created by Karlo on 25.3.2018..
 */

public interface LoginView extends BaseView {
    void onLoggedIn();
    void onSignUp(LoginRequest loginRequest);
}