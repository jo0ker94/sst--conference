package com.example.karlo.learningapplication.modules.search;

import android.arch.lifecycle.MutableLiveData;

import com.example.karlo.learningapplication.commons.BaseViewModel;
import com.example.karlo.learningapplication.commons.Status;
import com.example.karlo.learningapplication.database.program.ProgramDataSource;
import com.example.karlo.learningapplication.models.program.Topic;
import com.example.karlo.learningapplication.models.program.Track;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends BaseViewModel {

    private MutableLiveData<List<Track>> mTracks;
    private MutableLiveData<List<Topic>> mTopics;

    private ProgramDataSource mDataSource;

    @Inject
    public SearchViewModel(ProgramDataSource topicDataSource) {
        this.mDataSource = topicDataSource;
    }

    public MutableLiveData<List<Track>> getTracks() {
        if (mTracks == null) {
            mTracks = new MutableLiveData<>();
            fetchTracks();
        }
        return mTracks;
    }

    public MutableLiveData<List<Topic>> getTopics() {
        if (mTopics == null) {
            mTopics = new MutableLiveData<>();
            fetchTopics();
        }
        return mTopics;
    }

    public void fetchTracks() {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(mDataSource
                .getTracks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tracks -> {
                            mTracks.setValue(tracks);
                            mStatus.setValue(Status.loading(false));
                        }
                        ,
                        throwable -> {
                            mStatus.setValue(Status.error(throwable.getMessage()));
                            mStatus.setValue(Status.loading(false));
                        }
                ));
    }

    public void fetchTopics() {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(mDataSource
                .getTopics()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topics -> {
                            mTopics.setValue(topics);
                            mStatus.setValue(Status.loading(false));
                        }
                        ,
                        throwable -> {
                            mStatus.setValue(Status.error(throwable.getMessage()));
                            mStatus.setValue(Status.loading(false));
                        }
                ));
    }

}
