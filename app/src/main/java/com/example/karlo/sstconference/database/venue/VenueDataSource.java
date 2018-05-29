package com.example.karlo.sstconference.database.venue;

import com.example.karlo.sstconference.models.venue.Venue;

import io.reactivex.Observable;

public interface VenueDataSource {

    Observable<Venue> getVenue();

    void insertOrUpdateVenue(Venue venue);

    void deleteVenue(Venue venue);
}
