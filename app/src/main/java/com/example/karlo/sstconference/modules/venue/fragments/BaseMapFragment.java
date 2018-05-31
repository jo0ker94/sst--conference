package com.example.karlo.sstconference.modules.venue.fragments;

import android.Manifest;
import android.arch.lifecycle.MutableLiveData;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.karlo.sstconference.App;
import com.example.karlo.sstconference.modules.venue.VenueActivity;
import com.example.karlo.sstconference.modules.venue.VenueViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

public class BaseMapFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "BaseMapFragment";
    private static final long REFRESH_INTERVAL = 30000;

    private VenueActivity mActivity;

    @Inject
    VenueViewModel mViewModel;

    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    protected LatLng mCurrentLocation;
    private boolean mFirstChange = true;
    protected String mTitle;
    private boolean mHasLocation = false;

    protected MutableLiveData<Boolean> mLocationSet = new MutableLiveData<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (VenueActivity) getActivity();
        ((App) mActivity.getApplication()).getComponent().inject(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);
        mLocationCallback = getLocationCallback();

        GoogleApiClient mClient = new GoogleApiClient.Builder(mActivity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mClient.connect();
        setUpObservers();
    }

    private void setUpObservers() {
        mViewModel.getStatus().observe(this, status -> {
            switch(status.getResponse()) {
                case LOADING:
                    mActivity.loadingData(status.getState());
                    break;
                case MESSAGE:
                    mActivity.showError(new Throwable(status.getMessage()));
                    break;
                case ERROR:
                    mActivity.showError(new Throwable(status.getMessage()));
                    break;
            }
        });
    }

    protected void showMarkers(List<MarkerOptions> markers) {
        for (MarkerOptions marker : markers) {
            mGoogleMap.addMarker(marker);
        }
    }

    protected void clearMarkers() {
        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }
    }

    public void radiusChanged() {
        if (mHasLocation) {
            mLocationSet.setValue(true);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        UiSettings uiSettings = this.mGoogleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.mGoogleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(REFRESH_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private LocationCallback getLocationCallback() {
        return new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if (mCurrentLocation != null && location != null && location.getLatitude() == mCurrentLocation.latitude
                            && location.getLongitude() == mCurrentLocation.longitude) {
                        Log.e(TAG, "Skipped location: " + location.toString());
                        return;
                    }
                    if (location == null) {
                        mActivity.showError(new Throwable("Location not found!"));
                    } else {
                        mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        if (mFirstChange && mGoogleMap != null) {
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 15));
                            mFirstChange = false;
                        }
                        mHasLocation = true;
                        mLocationSet.setValue(true);
                        Log.e(TAG, "Location has changed: " + location.toString());
                    }
                }
            }
        };
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }
}
