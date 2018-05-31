package com.example.karlo.sstconference.servertasks.interfaces;

import com.example.karlo.sstconference.models.User;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserApi {

    @GET("users/{id}.json")
    Observable<User> getUser(@Path("id") String id);

    @GET("users.json")
    Observable<Map<String, User>> getUsers();
}