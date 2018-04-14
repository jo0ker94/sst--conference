package com.example.karlo.learningapplication.modules.login;

import com.example.karlo.learningapplication.commons.CommonView;
import com.example.karlo.learningapplication.models.LoginRequest;

/**
 * Created by Karlo on 25.3.2018..
 */

public interface LoginView extends CommonView {
    void onLoggedIn();
    void onSignUp(LoginRequest loginRequest);
}