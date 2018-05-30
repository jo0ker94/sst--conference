package com.example.karlo.sstconference.modules.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.karlo.sstconference.base.BaseViewModel;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.database.program.ProgramDataSource;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.example.karlo.sstconference.models.User;
import com.example.karlo.sstconference.models.program.Topic;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends BaseViewModel {

    private static final String TAG = "HomeViewModel";
    private MutableLiveData<User> mUserData;
    private MutableLiveData<List<Topic>> mTopics;

    private UserDataSource mDataSource;
    private ProgramDataSource mProgramDataSource;
    private User mUser;

    @Inject
    public HomeViewModel(UserDataSource dataSource, ProgramDataSource programDataSource) {
        this.mDataSource = dataSource;
        this.mProgramDataSource = programDataSource;
        fetchUser();
    }

    public LiveData<User> getUser() {
        if (mUserData == null) {
            mUserData = new MutableLiveData<>();
        }
        return mUserData;
    }

    public MutableLiveData<List<Topic>> getTopics() {
        if (mTopics == null) {
            mTopics = new MutableLiveData<>();
        }
        return mTopics;
    }

    public void fetchUser() {
        mCompositeDisposable.add(mDataSource.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    mUser = user;
                    mUserData.setValue(user);
                }));
    }

    public void fetchSubscribedTopics() {
        mStatus.setValue(Status.loading(true));
        mCompositeDisposable.add(mProgramDataSource
                .getTopics()
                .flatMap(Observable::fromIterable)
                .filter(topic -> mUser.getSubscribedEvents().contains(topic.getId()))
                .distinct(Topic::getId)
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

    public void signOut() {
        mCompositeDisposable.add( Completable.fromAction(() -> mDataSource.deleteUser(mUser))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> mStatus.setValue(Status.logout())));
    }
}
