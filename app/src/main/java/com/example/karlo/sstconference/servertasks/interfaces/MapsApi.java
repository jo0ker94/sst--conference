package com.example.karlo.sstconference.servertasks.interfaces;

import com.example.karlo.sstconference.models.nearbyplaces.NearbyPlaces;

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
                                             @Query("type") String type,
                                             @Query("keyword") String keyword,
                                             @Query("key") String key);
}
