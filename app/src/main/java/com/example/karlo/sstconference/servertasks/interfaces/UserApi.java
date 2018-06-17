package com.example.karlo.sstconference.servertasks.interfaces;

import com.example.karlo.sstconference.models.User;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {

    @GET("users/{id}.json")
    Observable<User> getUser(@Path("id") String id);

    @GET("users.json")
    Observable<Map<String, User>> getUsers();

    @PUT("users/{id}.json")
    Completable putUser(@Path("id") String id, @Body User user);
}
