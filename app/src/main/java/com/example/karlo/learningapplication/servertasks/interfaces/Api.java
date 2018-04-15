package com.example.karlo.learningapplication.servertasks.interfaces;

import com.example.karlo.learningapplication.models.ConferenceChair;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Karlo on 26.3.2018..
 */

public interface Api {

    @GET("strings.json")
    io.reactivex.Observable<List<String>> getStrings();

    @GET("users/{id}/displayName.json")
    io.reactivex.Observable<String> getDisplayName(@Path("id") String id);

    @GET("2017/conferenceChairs.json")
    io.reactivex.Observable<List<ConferenceChair>> getChairs();
}
