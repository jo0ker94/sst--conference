package com.example.karlo.sstconference.modules.venue;

import android.arch.lifecycle.MutableLiveData;

import com.annimon.stream.Stream;
import com.example.karlo.sstconference.commons.BaseViewModel;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.models.nearbyplaces.LocationCoordinates;
import com.example.karlo.sstconference.models.venue.Venue;
import com.example.karlo.sstconference.servertasks.interfaces.Api;
import com.example.karlo.sstconference.servertasks.interfaces.MapsApi;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class VenueViewModel extends BaseViewModel {

    private MutableLiveData<List<MarkerOptions>> mMarkers;
    private MutableLiveData<Venue> mVenue;

    private Api mApi;
    private MapsApi mMapsApi;

    @Inject
    public VenueViewModel(Api api, MapsApi mapsApi) {
        this.mApi = api;
        this.mMapsApi = mapsApi;
    }

    public MutableLiveData<List<MarkerOptions>> getMarkers() {
        if (mMarkers == null) {
            mMarkers = new MutableLiveData<>();
        }
        return mMarkers;
    }

    public MutableLiveData<Venue> getVenueDetails() {
        if (mVenue == null) {
            mVenue = new MutableLiveData<>();
            fetchVenueDetails();
        }
        return mVenue;
    }

    public void fetchRestaurants(LatLng latLng) {
        mCompositeDisposable.add(mMapsApi
                .getNearbyPlaces(
                        String.format("%s,%s", latLng.latitude, latLng.longitude),
                        2000,
                        "restaurant",
                        "",
                        Constants.GOOGLE_API_KEY)
                .map(nearbyPlaces -> Stream.of(nearbyPlaces.getResults())
                        .map(place -> {
                            LocationCoordinates location = place.getGeometry().getLocationCoordinates();
                            return new MarkerOptions()
                                    .position(new LatLng(location.getLat(), location.getLng()))
                                    .title(place.getName())
                                    .snippet(place.getVicinity());
                        }).toList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(markerOptions -> mMarkers.setValue(markerOptions),
                        throwable -> mStatus.setValue(Status.error(throwable.getMessage()))
                )
        );
    }

    public void fetchMuseums(LatLng latLng) {
        mCompositeDisposable.add(mMapsApi
                .getNearbyPlaces(
                        String.format("%s,%s", latLng.latitude, latLng.longitude),
                        2000,
                        "museum",
                        "",
                        Constants.GOOGLE_API_KEY)
                .map(nearbyPlaces -> Stream.of(nearbyPlaces.getResults())
                        .map(place -> {
                            LocationCoordinates location = place.getGeometry().getLocationCoordinates();
                            return new MarkerOptions()
                                    .position(new LatLng(location.getLat(), location.getLng()))
                                    .title(place.getName())
                                    .snippet(place.getVicinity());
                        }).toList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(markerOptions -> mMarkers.setValue(markerOptions),
                        throwable -> mStatus.setValue(Status.error(throwable.getMessage()))
                )
        );
    }

    public void fetchVenueDetails() {
        mCompositeDisposable.add(mApi
                .getVenue()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(venue -> mVenue.setValue(venue))
        );
    }
}
