package com.example.karlo.sstconference.modules.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.karlo.sstconference.base.BaseViewModel;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.database.user.UserDataSource;
import com.example.karlo.sstconference.models.User;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends BaseViewModel {

    private static final String TAG = "HomeViewModel";
    private MutableLiveData<User> mUserData;

    private UserDataSource mDataSource;
    private FirebaseAuth mFirebaseAuth;
    private User mUser;

    @Inject
    public HomeViewModel(UserDataSource dataSource, FirebaseAuth firebaseAuth) {
        this.mDataSource = dataSource;
        this.mFirebaseAuth = firebaseAuth;
    }

    public LiveData<User> getUser() {
        if (mUserData == null) {
            mUserData = new MutableLiveData<>();
            fetchUser();
        }
        return mUserData;
    }

    private void fetchUser() {
        mDataSource.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(User user) {
                        mUser = user;
                        mUserData.setValue(user);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mUserData.setValue(null);
                    }
                });
    }

    public void signOut() {
        mFirebaseAuth.signOut();
        mCompositeDisposable.add(mDataSource.deleteUser(mUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> mStatus.setValue(Status.logout())));
    }
}
