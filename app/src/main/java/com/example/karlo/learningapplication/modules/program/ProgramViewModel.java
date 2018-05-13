package com.example.karlo.learningapplication.modules.program;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.karlo.learningapplication.commons.Status;
import com.example.karlo.learningapplication.models.program.Track;
import com.example.karlo.learningapplication.servertasks.interfaces.Api;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProgramViewModel extends ViewModel {

    private static final String TAG = "ProgramViewModel";
    private final MutableLiveData<Status> status = new MutableLiveData<>();
    private final MutableLiveData<List<Track>> mTracks = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Api mApi;

    @Inject
    public ProgramViewModel(Api api) {
        this.mApi = api;
        fetchTracks();
    }

    public MutableLiveData<Status> getStatus() {
        return status;
    }

    public MutableLiveData<List<Track>> getTracks() {
        return mTracks;
    }

    public void fetchData() {
        status.setValue(Status.loading(true));
        compositeDisposable.add(mApi
                .getChairs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(chairs -> {
                            //view.showData(chairs);
                            status.setValue(Status.loading(false));
                        }
                        ,
                        throwable -> {
                            status.setValue(Status.error(throwable.getMessage()));
                            status.setValue(Status.loading(false));
                        }
                ));
    }

    public void fetchTracks() {
        status.setValue(Status.loading(true));
        compositeDisposable.add(mApi
                .getTracks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tracks -> {
                            mTracks.setValue(tracks);
                            status.setValue(Status.loading(false));
                        }
                        ,
                        throwable -> {
                            status.setValue(Status.error(throwable.getMessage()));
                            status.setValue(Status.loading(false));
                        }
                ));
    }

    public void fetchTopics(int position) {
        status.setValue(Status.loading(true));
        compositeDisposable.add(mApi
                .getTopics()
                .flatMap(Observable::fromIterable)
                .filter(topic -> topic.getParentId() == position)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topics -> {
                            //view.showTopics(topics);
                            status.setValue(Status.loading(false));
                        }
                        ,
                        throwable -> {
                            status.setValue(Status.error(throwable.getMessage()));
                            status.setValue(Status.loading(false));
                        }
                ));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
