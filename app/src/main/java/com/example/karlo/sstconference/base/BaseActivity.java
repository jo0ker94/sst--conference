package com.example.karlo.sstconference.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<V extends BaseView, P extends BasePresenter<V>>
        extends AppCompatActivity {

    protected P presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        attachView();
    }

    public abstract void attachView();

    @NonNull
    public abstract P createPresenter();

    @NonNull
    public P getPresenter() {
        return presenter;
    }

    public void setPresenter(@NonNull P presenter) {
        this.presenter = presenter;
    }
}
