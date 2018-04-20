package com.example.karlo.learningapplication.commons;

/**
 * Created by Karlo on 31.3.2018..
 */

public interface BaseView {
    void loadingData(boolean loading);
    void showError(Throwable error);
}
