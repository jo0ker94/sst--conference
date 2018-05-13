package com.example.karlo.learningapplication.modules.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.karlo.learningapplication.commons.BaseViewModel;
import com.example.karlo.learningapplication.commons.Status;
import com.example.karlo.learningapplication.database.user.UserDataSource;
import com.example.karlo.learningapplication.models.User;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends BaseViewModel {

    private static final String TAG = "HomeViewModel";
    private final MutableLiveData<User> mUserData = new MutableLiveData<>();

    private UserDataSource mDataSource;
    private User mUser;

    @Inject
    public HomeViewModel(UserDataSource dataSource) {
        this.mDataSource = dataSource;
        fetchUser();
    }

    public LiveData<User> getUser() {
        return mUserData;
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

    public void signOut() {
        mCompositeDisposable.add( Completable.fromAction(() -> mDataSource.deleteUser(mUser))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> mStatus.setValue(Status.logout())));
    }
}
