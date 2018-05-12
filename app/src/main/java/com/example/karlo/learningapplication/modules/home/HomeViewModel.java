package com.example.karlo.learningapplication.modules.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.commons.Status;
import com.example.karlo.learningapplication.database.user.LocalUserDataSource;
import com.example.karlo.learningapplication.database.user.UserDataSource;
import com.example.karlo.learningapplication.models.User;
import com.example.karlo.learningapplication.servertasks.RetrofitUtil;
import com.example.karlo.learningapplication.servertasks.interfaces.Api;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {

    private static final String TAG = "HomeViewModel";
    private final MutableLiveData<Status> status = new MutableLiveData<>();
    private final MutableLiveData<User> mUserData = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private UserDataSource dataSource;
    private User mUser;

    @Inject
    public HomeViewModel(UserDataSource dataSource) {
        this.dataSource = dataSource;
        fetchUser();
    }

    public void setDataSource(LocalUserDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public LiveData<User> getUser() {
        return mUserData;
    }

    public MutableLiveData<Status> getStatus() {
        return status;
    }

    public void fetchData() {
        status.setValue(Status.loading(true));
        compositeDisposable.add(RetrofitUtil
                .getRetrofit(Constants.FIREBASE_BASE_URL)
                .create(Api.class)
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
        compositeDisposable.add(RetrofitUtil
                .getRetrofit(Constants.FIREBASE_BASE_URL)
                .create(Api.class)
                .getTracks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tracks -> {
                            //view.showTracks(tracks);
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
        compositeDisposable.add(RetrofitUtil
                .getRetrofit(Constants.FIREBASE_BASE_URL)
                .create(Api.class)
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

    public void fetchUser() {
        compositeDisposable.add(dataSource.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    mUser = user;
                    mUserData.setValue(user);
                }));
    }

    public void signOut() {
        compositeDisposable.add( Completable.fromAction(() -> dataSource.deleteUser(mUser))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> status.setValue(Status.logout())));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
