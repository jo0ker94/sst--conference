package com.example.karlo.learningapplication.modules.search;

import android.app.Activity;
import android.text.TextUtils;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.servertasks.RetrofitUtil;
import com.example.karlo.learningapplication.servertasks.interfaces.WikiApi;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Karlo on 31.3.2018..
 */

public class SearchPresenter extends MvpBasePresenter<SearchView> implements MvpPresenter<SearchView> {

    private static final String CUSTOM_SEARCH_ID = "004785902684984064423:npuxlr36ea0";
    private static final String API_KEY = "AIzaSyBCvwdTitSfeF53hy-uvM_RR-klHk_OB2k";

    public void searchWiki(String query) {
        if (TextUtils.isEmpty(query)) {
            ifViewAttached(view -> {
                view.showError(new Throwable(((Activity) getView()).getString(R.string.no_text_message)));
                view.showNoResult();
            });
            return;
        }
        ifViewAttached(view -> view.loadingData(true));
        RetrofitUtil
                .getRetrofit(Constants.WIKI_BASE_URL)
                .create(WikiApi.class)
                .getResults(query, CUSTOM_SEARCH_ID, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results ->
                        ifViewAttached(view -> {
                            view.showResult(results);
                            view.loadingData(false);

                        }),
                        throwable ->
                            ifViewAttached(view -> {
                                view.showError(throwable);
                                view.loadingData(false);
                                view.showNoResult();

                            }));
    }
}
