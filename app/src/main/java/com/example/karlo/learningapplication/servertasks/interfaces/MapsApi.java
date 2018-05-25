package com.example.karlo.learningapplication.servertasks.interfaces;

import com.example.karlo.learningapplication.models.nearbyplaces.NearbyPlaces;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Karlo on 31.3.2018..
 */

public interface MapsApi {

    @GET("nearbysearch/json?")
    Observable<NearbyPlaces> getNearbyPlaces(@Query("location") String location,
                                             @Query("radius") long radius,
                                             @Query("type") String keyword,
                                             @Query("key") String key);
}
