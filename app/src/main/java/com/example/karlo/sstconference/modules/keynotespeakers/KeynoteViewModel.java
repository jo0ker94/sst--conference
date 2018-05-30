package com.example.karlo.sstconference.modules.keynotespeakers;

import android.arch.lifecycle.MutableLiveData;

import com.example.karlo.sstconference.base.BaseViewModel;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.database.keynote.KeynoteDataSource;
import com.example.karlo.sstconference.models.keynote.KeynoteSpeaker;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class KeynoteViewModel extends BaseViewModel {

    private MutableLiveData<List<KeynoteSpeaker>> mSpeakers;

    private KeynoteDataSource mDataSource;

    @Inject
    public KeynoteViewModel(KeynoteDataSource keynoteDataSource) {
        this.mDataSource = keynoteDataSource;
    }

    public MutableLiveData<List<KeynoteSpeaker>> getSpeakers() {
        if (mSpeakers == null) {
            mSpeakers = new MutableLiveData<>();
            fetchKeynoteSpeakers();
        }
        return mSpeakers;
    }

    private void fetchKeynoteSpeakers() {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(mDataSource
                .getKeynoteSpeakers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(keynoteSpeakers -> {
                            mSpeakers.setValue(keynoteSpeakers);
                            mStatus.setValue(Status.loading(false));
                        }
                        , throwable -> {
                            mStatus.setValue(Status.error(throwable.getMessage()));
                            mStatus.setValue(Status.loading(false));
                        }));

    }
}
