package com.example.karlo.sstconference.commons;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseViewModel extends ViewModel {

    protected MutableLiveData<Status> mStatus = new MutableLiveData<>();
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public LiveData<Status> getStatus() {
        return mStatus;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.clear();
    }
}
