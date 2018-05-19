package com.example.karlo.learningapplication.servertasks.interfaces;

import com.example.karlo.learningapplication.models.User;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserApi {

    @GET("users/{id}.json")
    Single<User> getUser(@Path("id") String id);

    @GET("users.json")
    io.reactivex.Observable<Map<String, User>> getUsers();

    @GET("users/{id}/displayName.json")
    io.reactivex.Observable<String> getDisplayName(@Path("id") String id);
}
