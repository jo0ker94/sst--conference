package com.example.karlo.learningapplication.modules.home;

import com.example.karlo.learningapplication.commons.BasePresenter;
import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.database.user.LocalUserDataSource;
import com.example.karlo.learningapplication.models.User;
import com.example.karlo.learningapplication.servertasks.RetrofitUtil;
import com.example.karlo.learningapplication.servertasks.interfaces.Api;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Karlo on 26.3.2018..
 */

public class HomePresenter extends BasePresenter<HomeView> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private LocalUserDataSource dataSource;
    private User mUser;

    public void fetchData() {
        ifViewAttached(view -> view.loadingData(true));
        compositeDisposable.add(RetrofitUtil
                .getRetrofit(Constants.FIREBASE_BASE_URL)
                .create(Api.class)
                .getChairs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(chairs ->
                                ifViewAttached(view ->  {
                                    view.showData(chairs);
                                    view.loadingData(false);
                                })
                        ,
                        throwable -> ifViewAttached(view -> {
                                    view.showError(throwable);
                                    view.loadingData(false);
                                }
                        )));
    }

    public void fetchUser() {
        compositeDisposable.add(dataSource.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    mUser = user;
                    ifViewAttached(view -> view.bindUser(user));
                }));
    }

    public void signOut() {
        compositeDisposable.add( Completable.fromAction(() -> dataSource.deleteUser(mUser))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> ifViewAttached(HomeView::logOut)));
    }

    @Override
    public void destroy() {
        super.destroy();
        compositeDisposable.clear();
    }

    public void setDataSource(LocalUserDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
