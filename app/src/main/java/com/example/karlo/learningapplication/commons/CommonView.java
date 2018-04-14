package com.example.karlo.learningapplication.commons;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by Karlo on 31.3.2018..
 */

public interface CommonView extends MvpView {
    void loadingData(boolean loading);
    void showError(Throwable error);
}
