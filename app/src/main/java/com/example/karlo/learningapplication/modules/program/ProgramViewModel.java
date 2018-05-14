package com.example.karlo.learningapplication.modules.program;

import android.arch.lifecycle.MutableLiveData;

import com.example.karlo.learningapplication.commons.BaseViewModel;
import com.example.karlo.learningapplication.commons.Status;
import com.example.karlo.learningapplication.models.program.Comment;
import com.example.karlo.learningapplication.models.program.Topic;
import com.example.karlo.learningapplication.models.program.Track;
import com.example.karlo.learningapplication.servertasks.interfaces.Api;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProgramViewModel extends BaseViewModel {

    private static final String TAG = "ProgramViewModel";

    private final MutableLiveData<List<Track>> mTracks = new MutableLiveData<>();
    private final MutableLiveData<List<Topic>> mTopics = new MutableLiveData<>();
    private final MutableLiveData<List<Comment>> mComments = new MutableLiveData<>();

    private Api mApi;

    @Inject
    public ProgramViewModel(Api api) {
        this.mApi = api;
        fetchTracks();
    }

    public MutableLiveData<List<Track>> getTracks() {
        return mTracks;
    }

    public MutableLiveData<List<Topic>> getTopics() {
        return mTopics;
    }

    public MutableLiveData<List<Comment>> getComments() {
        return mComments;
    }

    public void fetchComments(int commentID) {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(mApi
                .getComments(commentID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(comments -> {
                            mComments.setValue(comments);
                            mStatus.setValue(Status.loading(false));
                        }
                        ,
                        throwable -> {
                            mStatus.setValue(Status.error(throwable.getMessage()));
                            mStatus.setValue(Status.loading(false));
                        }
                ));
    }

    public void fetchTracks() {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(mApi
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

    public void fetchTopics(int position) {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(mApi
                .getTopics()
                .flatMap(Observable::fromIterable)
                .filter(topic -> topic.getParentId() == position)
                .toList()
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
