package com.example.karlo.sstconference.modules.login;

import com.example.karlo.sstconference.base.BaseView;
import com.example.karlo.sstconference.models.LoginRequest;

/**
 * Created by Karlo on 25.3.2018..
 */

public interface LoginView extends BaseView {
    void onLoggedIn();
    void onSignUp(LoginRequest loginRequest);
}