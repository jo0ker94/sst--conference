package com.example.karlo.learningapplication.modules.home;

import com.example.karlo.learningapplication.commons.BasePresenter;
import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.helpers.DatabaseHelper;
import com.example.karlo.learningapplication.models.User;
import com.example.karlo.learningapplication.servertasks.RetrofitUtil;
import com.example.karlo.learningapplication.servertasks.interfaces.Api;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Karlo on 26.3.2018..
 */

public class HomePresenter extends BasePresenter<HomeView> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        User profile = DatabaseHelper.getObject(User.class);
        if (profile != null) {
            ifViewAttached(view -> view.bindUser(profile));
        }
    }

    public void signOut() {
        User profile = DatabaseHelper.getObject(User.class);
        if (profile != null) {
            DatabaseHelper.deleteUser(profile.getUserId());
            ifViewAttached(HomeView::logOut);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        compositeDisposable.clear();
    }
}
