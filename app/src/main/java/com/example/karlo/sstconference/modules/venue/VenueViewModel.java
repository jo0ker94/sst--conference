package com.example.karlo.sstconference.modules.venue;

import android.arch.lifecycle.MutableLiveData;

import com.annimon.stream.Stream;
import com.example.karlo.sstconference.base.BaseViewModel;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.models.nearbyplaces.LocationCoordinates;
import com.example.karlo.sstconference.models.venue.MarkersGroup;
import com.example.karlo.sstconference.models.venue.Venue;
import com.example.karlo.sstconference.servertasks.interfaces.Api;
import com.example.karlo.sstconference.servertasks.interfaces.MapsApi;
import com.example.karlo.sstconference.utility.AppConfig;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class VenueViewModel extends BaseViewModel {

    private MutableLiveData<MarkersGroup> mGroup;
    private MutableLiveData<List<MarkerOptions>> mMarkers;
    private MutableLiveData<Venue> mVenue;

    private MarkersGroup mPlacesGroup = new MarkersGroup();

    private Api mApi;
    private MapsApi mMapsApi;

    @Inject
    public VenueViewModel(Api api, MapsApi mapsApi) {
        this.mApi = api;
        this.mMapsApi = mapsApi;
    }

    public MutableLiveData<MarkersGroup> getMarkerGroup() {
        if (mGroup == null) {
            mGroup = new MutableLiveData<>();
        }
        return mGroup;
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

    public void fetchAllPlaces(LatLng latLng) {
        if (mGroup == null) {
            mGroup = new MutableLiveData<>();
        }
        mCompositeDisposable.add(fetchRestaurants(latLng)
                .andThen(fetchCafe(latLng))
                .andThen(fetchBar(latLng))
                .andThen(fetchMuseums(latLng))
                .andThen(fetchLibrary(latLng))
                .andThen(fetchChurch(latLng))
                .andThen(fetchZoo(latLng))
                .subscribe(() -> mGroup.setValue(mPlacesGroup)));
    }

    private void fetchGooglePlaces(PlaceType type, LatLng latLng) {
        mCompositeDisposable.add(mMapsApi.getNearbyPlaces(
                getLocationString(latLng),
                AppConfig.MAP_RADIUS,
                type.toString(),
                Constants.GOOGLE_API_KEY)
                .flatMap(nearbyPlaces -> Observable.just(
                        Stream.of(nearbyPlaces.getResults())
                                .map(place -> {
                                    LocationCoordinates location = place.getGeometry().getLocationCoordinates();
                                    return new MarkerOptions()
                                            .position(new LatLng(location.getLat(), location.getLng()))
                                            .title(place.getName())
                                            .snippet(place.getVicinity());
                                }).toList())
                )
                .subscribe(markerOptions -> {
                    switch (type) {
                        case RESTAURANT:
                            mPlacesGroup.setRestaurants(markerOptions);
                            break;

                        case BAR:
                            mPlacesGroup.setBars(markerOptions);
                            break;

                        case CAFE:
                            mPlacesGroup.setCafe(markerOptions);
                            break;

                        case MUSEUM:
                            mPlacesGroup.setMuseum(markerOptions);
                            break;

                        case LIBRARY:
                            mPlacesGroup.setLibrary(markerOptions);
                            break;

                        case CHURCH:
                            mPlacesGroup.setChurch(markerOptions);
                            break;

                        case ZOO:
                            mPlacesGroup.setZoo(markerOptions);
                            break;
                    }
                }));
    }

    private Completable fetchRestaurants(LatLng latLng) {
        return Completable.fromAction(() -> fetchGooglePlaces(PlaceType.RESTAURANT, latLng));
    }

    private Completable fetchCafe(LatLng latLng) {
        return Completable.fromAction(() -> fetchGooglePlaces(PlaceType.CAFE, latLng));
    }

    private Completable fetchBar(LatLng latLng) {
        return Completable.fromAction(() -> fetchGooglePlaces(PlaceType.BAR, latLng));
    }

    private Completable fetchMuseums(LatLng latLng) {
        return Completable.fromAction(() -> fetchGooglePlaces(PlaceType.MUSEUM, latLng));
    }

    private Completable fetchLibrary(LatLng latLng) {
        return Completable.fromAction(() -> fetchGooglePlaces(PlaceType.LIBRARY, latLng));
    }

    private Completable fetchChurch(LatLng latLng) {
        return Completable.fromAction(() -> fetchGooglePlaces(PlaceType.CHURCH, latLng));
    }

    private Completable fetchZoo(LatLng latLng) {
        return Completable.fromAction(() -> fetchGooglePlaces(PlaceType.ZOO, latLng));
    }

    private void fetchVenueDetails() {
        mCompositeDisposable.add(mApi
                .getVenue()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(venue -> mVenue.setValue(venue))
        );
    }

    private String getLocationString(LatLng latLng) {
        return String.format("%s,%s", latLng.latitude, latLng.longitude);
    }
}
