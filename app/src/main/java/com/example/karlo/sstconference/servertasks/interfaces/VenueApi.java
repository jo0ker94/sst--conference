package com.example.karlo.sstconference.servertasks.interfaces;

import com.example.karlo.sstconference.models.venue.Venue;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface VenueApi {

    @GET("2017/venue.json")
    Observable<Venue> getVenue();

}
