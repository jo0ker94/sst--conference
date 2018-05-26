package com.example.karlo.sstconference.base;

/**
 * Created by Karlo on 31.3.2018..
 */

public interface BaseView {
    void loadingData(boolean loading);
    void showError(Throwable error);
}
