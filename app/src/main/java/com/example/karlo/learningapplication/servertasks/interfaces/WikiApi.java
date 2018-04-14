package com.example.karlo.learningapplication.servertasks.interfaces;

import com.example.karlo.learningapplication.models.wiki.WikiResult;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Karlo on 31.3.2018..
 */

public interface WikiApi {

    @GET("customsearch/v1")
    io.reactivex.Observable<WikiResult> getResults(@Query("q") String query,
                                                         @Query("cx") String cx,
                                                         @Query("key") String key);
}
