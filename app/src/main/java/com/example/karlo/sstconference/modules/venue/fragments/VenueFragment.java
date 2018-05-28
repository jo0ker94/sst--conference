package com.example.karlo.sstconference.modules.venue.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.models.enums.PlaceType;
import com.example.karlo.sstconference.models.venue.Info;
import com.example.karlo.sstconference.models.venue.MarkersGroup;
import com.example.karlo.sstconference.models.venue.Venue;
import com.example.karlo.sstconference.modules.venue.VenueActivity;
import com.example.karlo.sstconference.ui.HeaderView;
import com.example.karlo.sstconference.ui.SwitchButton;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import net.globulus.easyprefs.EasyPrefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VenueFragment extends BaseMapFragment {

    public enum VenueType { CONFERENCE, REGION, FOOD, SIGHTS, FACULTY }

    private Unbinder mUnbinder;

    private VenueActivity mActivity;
    private VenueType mType;

    LinearLayout mBaseLayout;

    private Venue mVenue;
    private MarkersGroup mMarkersGroup;
    private List<MarkerOptions> mMarkers;

    public static VenueFragment newInstance(VenueType venueType) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.TYPE, venueType);
        VenueFragment fragment = new VenueFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            mType = (VenueType) bundle.getSerializable(Constants.TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_venue_layout, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        mActivity = (VenueActivity) getActivity();
        readBundle(getArguments());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mBaseLayout = rootView.findViewById(R.id.base_layout);
        setUpObserver();
        return rootView;
    }

    private void setUpObserver() {
        mLocationSet.observe(this, hasLocation -> {
            if (hasLocation != null && hasLocation) {
                mViewModel.fetchAllPlaces(mCurrentLocation);
            }
        });
        mViewModel.getMarkerGroup().observe(this, markers -> {
            if (markers != null) {
                mMarkersGroup = markers;
                filterMarkers();
            }
        });
    }

    private void filterMarkers() {
        mMarkers = new ArrayList<>();
        clearMarkers();
        if (mMarkersGroup != null) {
            if (mType == VenueType.FOOD) {
                if (mMarkersGroup.getRestaurants() != null && EasyPrefs.getShowRestaurants(mActivity)) {
                    mMarkers.addAll(mMarkersGroup.getRestaurants());
                }
                if (mMarkersGroup.getBars() != null && EasyPrefs.getShowBars(mActivity)) {
                    mMarkers.addAll(mMarkersGroup.getBars());
                }
                if (mMarkersGroup.getCafe() != null && EasyPrefs.getShowCafe(mActivity)) {
                    mMarkers.addAll(mMarkersGroup.getCafe());
                }

            } else {
                if (mMarkersGroup.getMuseum() != null && EasyPrefs.getShowMuseums(mActivity)) {
                    mMarkers.addAll(mMarkersGroup.getMuseum());
                }
                if (mMarkersGroup.getLibrary() != null && EasyPrefs.getShowLibrary(mActivity)) {
                    mMarkers.addAll(mMarkersGroup.getLibrary());
                }
                if (mMarkersGroup.getChurch() != null && EasyPrefs.getShowChurch(mActivity)) {
                    mMarkers.addAll(mMarkersGroup.getChurch());
                }
                if (mMarkersGroup.getZoo() != null && EasyPrefs.getShowZoo(mActivity)) {
                    mMarkers.addAll(mMarkersGroup.getZoo());
                }
            }
        }
        showMarkers(mMarkers);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        switch (mType) {

            case CONFERENCE:
                setTitle(getString(R.string.conference));
                if (mVenue != null) {
                    populateSection(mVenue.getHotel());
                } else {
                    mViewModel.getVenueDetails().observe(this, venue -> {
                        mVenue = venue;
                        populateSection(mVenue.getHotel());
                    });
                }
                mLocationSet.observe(this, hasLocation -> {
                    if (hasLocation != null && hasLocation) {
                        setConferenceMarkers();
                    }
                });
                break;

            case REGION:
                setTitle(getString(R.string.region));
                if (mVenue != null) {
                    populateSection(mVenue.getRegion());
                } else {
                    mViewModel.getVenueDetails().observe(this, venue -> {
                        mVenue = venue;
                        populateSection(mVenue.getRegion());
                    });
                }
                mLocationSet.observe(this, hasLocation -> {
                    if (hasLocation != null && hasLocation) {
                        setRegionMarkers();
                    }
                });
                break;

            case FOOD:
                setTitle(getString(R.string.food));
                //mLocationSet.observe(this, hasLocation -> {
                //    if (hasLocation != null && hasLocation) {
                //        mViewModel.fetchAllPlaces(mCurrentLocation);
                //    }
                //});
                addFoodSections();
                break;

            case SIGHTS:
                setTitle(getString(R.string.sights));
                //mLocationSet.observe(this, hasLocation -> {
                //    if (hasLocation != null && hasLocation) {
                //        mViewModel.fetchAllPlaces(mCurrentLocation);
                //    }
                //});
                addSightsSections();
                break;

            case FACULTY:
                setTitle(getString(R.string.faculty));
                if (mVenue != null) {
                    populateSection(mVenue.getFaculty());
                } else {
                    mViewModel.getVenueDetails().observe(this, venue -> {
                        mVenue = venue;
                        populateSection(mVenue.getFaculty());
                    });
                }
                mLocationSet.observe(this, hasLocation -> {
                    if (hasLocation != null && hasLocation) {
                        setFacultyMarkers();
                    }
                });
                break;
        }
    }

    private void setRegionMarkers() {
        MarkerOptions osijek = new MarkerOptions()
                .position(new LatLng(45.5548, 18.6955))
                .title(getString(R.string.osijek));

        List<MarkerOptions> markerOptions = new ArrayList<>();
        markerOptions.add(osijek);
        showMarkers(markerOptions);
    }

    private void setFacultyMarkers() {
        MarkerOptions ferit = new MarkerOptions()
                .position(new LatLng(45.556784916805064, 18.695619106292725))
                .title(getString(R.string.ferit_osijek))
                .snippet(getString(R.string.ferit_address));

        MarkerOptions university = new MarkerOptions()
                .position(new LatLng(45.5609596, 18.69593169999996))
                .title(getString(R.string.university))
                .snippet(getString(R.string.university_address));

        List<MarkerOptions> markerOptions = new ArrayList<>();
        markerOptions.add(ferit);
        markerOptions.add(university);
        showMarkers(markerOptions);
    }

    private void setConferenceMarkers() {
        MarkerOptions hotel = new MarkerOptions()
                .position(new LatLng(45.56214079999999, 18.67980280000006))
                .title(getString(R.string.hotel_osijek))
                .snippet(getString(R.string.hotel_address));

        List<MarkerOptions> markerOptions = new ArrayList<>();
        markerOptions.add(hotel);
        showMarkers(markerOptions);
    }

    private void addFoodSections() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.empty_section_layout, null);
        HeaderView content = view.findViewById(R.id.header_view);
        content.setTitle(getString(R.string.type_of_places));
        View foodSection = LayoutInflater.from(getContext()).inflate(R.layout.food_sections_layout, null);

        SwitchButton restaurantsSwitch = foodSection.findViewById(R.id.restaurants_switch);
        SwitchButton cafeSwitch = foodSection.findViewById(R.id.cafe_switch);
        SwitchButton barSwitch = foodSection.findViewById(R.id.bar_switch);

        restaurantsSwitch.setChecked(EasyPrefs.getShowRestaurants(mActivity));
        cafeSwitch.setChecked(EasyPrefs.getShowCafe(mActivity));
        barSwitch.setChecked(EasyPrefs.getShowBars(mActivity));

        restaurantsSwitch.setOnCheckedChangeListener(new SwitchListener(PlaceType.RESTAURANT));
        cafeSwitch.setOnCheckedChangeListener(new SwitchListener(PlaceType.CAFE));
        barSwitch.setOnCheckedChangeListener(new SwitchListener(PlaceType.BAR));

        content.addView(foodSection);
        mBaseLayout.addView(view);
    }

    private void addSightsSections() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.empty_section_layout, null);
        HeaderView content = view.findViewById(R.id.header_view);
        content.setTitle(getString(R.string.type_of_places));
        View sightsSection = LayoutInflater.from(getContext()).inflate(R.layout.sights_section_layout, null);

        SwitchButton museumSwitch = sightsSection.findViewById(R.id.museum_switch);
        SwitchButton librarySwitch = sightsSection.findViewById(R.id.library_switch);
        SwitchButton churchSwitch = sightsSection.findViewById(R.id.church_switch);
        SwitchButton zooSwitch = sightsSection.findViewById(R.id.zoo_switch);

        museumSwitch.setChecked(EasyPrefs.getShowMuseums(mActivity));
        librarySwitch.setChecked(EasyPrefs.getShowLibrary(mActivity));
        churchSwitch.setChecked(EasyPrefs.getShowChurch(mActivity));
        zooSwitch.setChecked(EasyPrefs.getShowZoo(mActivity));

        museumSwitch.setOnCheckedChangeListener(new SwitchListener(PlaceType.MUSEUM));
        librarySwitch.setOnCheckedChangeListener(new SwitchListener(PlaceType.LIBRARY));
        churchSwitch.setOnCheckedChangeListener(new SwitchListener(PlaceType.CHURCH));
        zooSwitch.setOnCheckedChangeListener(new SwitchListener(PlaceType.ZOO));

        content.addView(sightsSection);
        mBaseLayout.addView(view);
    }

    private void populateSection(List<Info> infoList) {
        for (int i = 0; i < infoList.size(); i++) {
            addSection(infoList.get(i));
        }
    }

    private void addSection(Info model) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.section_layout, null);

        HeaderView headerView = view.findViewById(R.id.header_view);
        ImageView imageView = headerView.findViewById(R.id.image_view);
        TextView textView = headerView.findViewById(R.id.text_view);

        headerView.setTitle(model.getTitle());
        textView.setText(model.getDescription());
        imageView.setOnClickListener(v -> {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getLink()));
            startActivity(myIntent);
        });

        Picasso.get()
                .load(model.getImageUrl())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.no_img)
                .into(imageView);

        mBaseLayout.addView(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    private class SwitchListener implements CompoundButton.OnCheckedChangeListener {

        private PlaceType mType;

        SwitchListener(PlaceType type) {
            this.mType = type;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            switch (mType) {
                case RESTAURANT:
                    EasyPrefs.putShowRestaurants(mActivity, b);
                    filterMarkers();
                    break;
                case CAFE:
                    EasyPrefs.putShowCafe(mActivity, b);
                    filterMarkers();
                    break;
                case BAR:
                    EasyPrefs.putShowBars(mActivity, b);
                    filterMarkers();
                    break;
                case MUSEUM:
                    EasyPrefs.putShowMuseums(mActivity, b);
                    filterMarkers();
                    break;
                case LIBRARY:
                    EasyPrefs.putShowLibrary(mActivity, b);
                    filterMarkers();
                    break;
                case CHURCH:
                    EasyPrefs.putShowChurch(mActivity, b);
                    filterMarkers();
                    break;
                case ZOO:
                    EasyPrefs.putShowZoo(mActivity, b);
                    filterMarkers();
                    break;
            }
        }
    }
}
