package com.example.karlo.sstconference.modules.venue;

import android.arch.lifecycle.MutableLiveData;

import com.example.karlo.sstconference.base.BaseViewModel;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.commons.Status;
import com.example.karlo.sstconference.database.venue.VenueDataSource;
import com.example.karlo.sstconference.models.enums.PlaceType;
import com.example.karlo.sstconference.models.nearbyplaces.LocationCoordinates;
import com.example.karlo.sstconference.models.nearbyplaces.Result;
import com.example.karlo.sstconference.models.venue.MarkersGroup;
import com.example.karlo.sstconference.models.venue.Venue;
import com.example.karlo.sstconference.modules.venue.fragments.VenueFragment;
import com.example.karlo.sstconference.servertasks.interfaces.MapsApi;
import com.example.karlo.sstconference.utility.AppConfig;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VenueViewModel extends BaseViewModel {

    private MutableLiveData<MarkersGroup> mGroup;
    private MutableLiveData<List<MarkerOptions>> mMarkers;
    private MutableLiveData<Venue> mVenue;

    private MarkersGroup mPlacesGroup = new MarkersGroup();

    private VenueDataSource mVenueDataSource;
    private MapsApi mMapsApi;

    @Inject
    public VenueViewModel(VenueDataSource venueDataSource, MapsApi mapsApi) {
        this.mVenueDataSource = venueDataSource;
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

    public void fetchAllPlaces(VenueFragment.VenueType type, LatLng latLng) {
        if (mGroup == null) {
            mGroup = new MutableLiveData<>();
        }
        mStatus.setValue(Status.loading(true));

        Completable fetchPlaces = type == VenueFragment.VenueType.FOOD ?
                fetchFoodSection(latLng) : fetchSightsSection(latLng);

        fetchPlaces.delay(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        mGroup.setValue(mPlacesGroup);
                        mStatus.setValue(Status.loading(false));

                    }

                    @Override
                    public void onError(Throwable e) {
                        mStatus.setValue(Status.loading(false));
                    }
                });
    }

    private Completable fetchFoodSection(LatLng latLng) {
        return fetchRestaurants(latLng)
                .andThen(fetchCafe(latLng))
                .andThen(fetchBar(latLng));
    }

    private Completable fetchSightsSection(LatLng latLng) {
        return fetchMuseums(latLng)
                .andThen(fetchLibrary(latLng))
                .andThen(fetchChurch(latLng))
                .andThen(fetchZoo(latLng));
    }

    private Observable<List<MarkerOptions>> fetchGooglePlaces(PlaceType type, LatLng latLng) {
        return mMapsApi.getNearbyPlaces(
                getLocationString(latLng),
                AppConfig.MAP_RADIUS,
                type.toString(),
                Constants.GOOGLE_API_KEY)
                .flatMap(nearbyPlaces -> Observable.fromArray(nearbyPlaces.getResults()))
                .flatMap(results -> {
                    List<MarkerOptions> markers = new ArrayList<>();
                    for (Result result : results) {
                        LocationCoordinates location = result.getGeometry().getLocationCoordinates();
                        markers.add(new MarkerOptions()
                                .position(new LatLng(location.getLat(), location.getLng()))
                                .title(result.getName())
                                .snippet(result.getVicinity()));
                    }
                    return Observable.fromArray(markers);
                });
    }

    private Completable fetchRestaurants(LatLng latLng) {
        return Completable.fromAction(() -> fetchGooglePlaces(PlaceType.RESTAURANT, latLng)
                .subscribe(markerOptions -> mPlacesGroup.setRestaurants(markerOptions)));
    }

    private Completable fetchCafe(LatLng latLng) {
        return Completable.fromAction(() -> fetchGooglePlaces(PlaceType.CAFE, latLng)
                .subscribe(markerOptions -> mPlacesGroup.setCafe(markerOptions)));
    }

    private Completable fetchBar(LatLng latLng) {
        return Completable.fromAction(() -> fetchGooglePlaces(PlaceType.BAR, latLng)
                .subscribe(markerOptions -> mPlacesGroup.setBars(markerOptions)));
    }

    private Completable fetchMuseums(LatLng latLng) {
        return Completable.fromAction(() -> fetchGooglePlaces(PlaceType.MUSEUM, latLng)
                .subscribe(markerOptions -> mPlacesGroup.setMuseum(markerOptions)));
    }

    private Completable fetchLibrary(LatLng latLng) {
        return Completable.fromAction(() -> fetchGooglePlaces(PlaceType.LIBRARY, latLng)
                .subscribe(markerOptions -> mPlacesGroup.setLibrary(markerOptions)));
    }

    private Completable fetchChurch(LatLng latLng) {
        return Completable.fromAction(() -> fetchGooglePlaces(PlaceType.CHURCH, latLng)
                .subscribe(markerOptions -> mPlacesGroup.setChurch(markerOptions)));
    }

    private Completable fetchZoo(LatLng latLng) {
        return Completable.fromAction(() -> fetchGooglePlaces(PlaceType.ZOO, latLng)
                .subscribe(markerOptions -> mPlacesGroup.setZoo(markerOptions)));
    }

    private void fetchVenueDetails() {
        mCompositeDisposable.add(mVenueDataSource
                .getVenue()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(venue -> mVenue.setValue(venue),
                        throwable -> mStatus.setValue(Status.error(throwable.getMessage())))
        );
    }

    private String getLocationString(LatLng latLng) {
        return String.format("%s,%s", latLng.latitude, latLng.longitude);
    }
}
