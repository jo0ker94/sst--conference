package com.example.karlo.learningapplication.modules.venue;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.example.karlo.learningapplication.App;
import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.commons.Constants;
import com.example.karlo.learningapplication.models.nearbyplaces.LocationCoordinates;
import com.example.karlo.learningapplication.servertasks.RetrofitUtil;
import com.example.karlo.learningapplication.servertasks.interfaces.MapsApi;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VenueActivity extends AppCompatActivity
        implements VenueView,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_LOCATION_PERMISSION = 10;
    private static final long REFRESH_INTERVAL = 10000;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    VenueViewModel mViewModel;

    private Unbinder mUnbinder;
    private VenuePagerAdapter mVenuePagerAdapter;

    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    private LatLng mCurrentLocation;
    private boolean mFirstChange = true;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private List<MarkerOptions> mMarkerOptions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);
        mUnbinder = ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = getLocationCallback();

        GoogleApiClient mClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mClient.connect();

        setUpToolbar();
        setUpTab();
        setUpObservers();
    }

    private void setUpObservers() {
        mViewModel.getMarkers().observe(this, markers -> {
            if (markers != null && !markers.isEmpty()) {
                mMarkerOptions.clear();
                mMarkerOptions.addAll(markers);
                showMarkers();
            }
        });

        mViewModel.getStatus().observe(this, status -> {
            switch(status.getResponse()) {
                case LOADING:
                    loadingData(status.getState());
                    break;
                case MESSAGE:
                    showError(new Throwable(status.getMessage()));
                    break;
                case ERROR:
                    showError(new Throwable(status.getMessage()));
                    break;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!hasLocationPermission()){
            requestPermission();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.searchMenu:
                if (hasLocationPermission()) {
                    mViewModel.fetchRestaurants(mCurrentLocation);
                } else {
                    requestPermission();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpTab() {
        mVenuePagerAdapter = new VenuePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mVenuePagerAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(mVenuePagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mVenuePagerAdapter.getFragmentByPosition(position) != null) {
                    setTitle(mVenuePagerAdapter.getFragmentByPosition(position).getTitle());
                    mVenuePagerAdapter.getFragmentByPosition(position).onResume();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setCustomView(R.layout.venue_tab_conference);
        mTabLayout.getTabAt(1).setCustomView(R.layout.venue_tab_region);
        mTabLayout.getTabAt(2).setCustomView(R.layout.venue_tab_food);
        mTabLayout.getTabAt(3).setCustomView(R.layout.venue_tab_sights);
        mTabLayout.getTabAt(4).setCustomView(R.layout.venue_tab_faculty);

        mViewPager.setCurrentItem(1);
        mViewPager.setCurrentItem(0);
        setTitle(R.string.conference);
    }

    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void loadingData(boolean loading) {
        mProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(Throwable error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMarkers() {
        for (MarkerOptions marker : mMarkerOptions) {
            mGoogleMap.addMarker(marker);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        UiSettings uiSettings = this.mGoogleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.mGoogleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(REFRESH_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
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
                    if (location == null) {
                        showError(new Throwable("LocationCoordinates not found!"));
                    } else {
                        mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        if (mFirstChange) {
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 15));
                            mFirstChange = false;
                        }
                        showError(new Throwable("Changed!"));
                    }
                }
            }
        };
    }

    private boolean hasLocationPermission(){
        int status = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return status == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        String[] permissions = new String[]{ Manifest.permission.ACCESS_FINE_LOCATION };
        ActivityCompat.requestPermissions(VenueActivity.this,
                permissions, REQUEST_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0) {
                    if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                        askForPermission();
                    }
                }
        }
    }

    private void askForPermission(){
        boolean shouldExplain = ActivityCompat.shouldShowRequestPermissionRationale(
                VenueActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldExplain) {
            this.displayDialog();
        }
    }

    private void displayDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.location_permission)
                .setMessage(R.string.permission_explain)
                .setNegativeButton(R.string.dismiss, null)
                .setPositiveButton(R.string.grant, (dialog, which) -> {
                    requestPermission();
                    dialog.dismiss();
                })
                .show();
    }
}
