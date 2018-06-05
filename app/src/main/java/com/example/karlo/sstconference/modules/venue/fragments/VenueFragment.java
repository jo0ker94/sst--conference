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
import com.example.karlo.sstconference.models.venue.VenueMarker;
import com.example.karlo.sstconference.modules.venue.VenueActivity;
import com.example.karlo.sstconference.ui.HeaderView;
import com.example.karlo.sstconference.ui.FunctionalButton;
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

    private List<String> mAddedViews = new ArrayList<>();

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
            if (hasLocation != null && hasLocation && (mType == VenueType.FOOD || mType == VenueType.SIGHTS)) {
                mViewModel.fetchAllPlaces(mType, mCurrentLocation);
            }
        });
        mViewModel.getMarkerGroup().observe(this, markers -> {
            if (markers != null && (mType == VenueType.FOOD || mType == VenueType.SIGHTS)) {
                mMarkersGroup = markers;
                filterMarkers();
            }
        });
    }

    private void filterMarkers() {
        List<MarkerOptions> markers = new ArrayList<>();
        clearMarkers();
        if (mMarkersGroup != null) {
            if (mType == VenueType.FOOD) {
                if (mMarkersGroup.getRestaurants() != null && EasyPrefs.getShowRestaurants(mActivity)) {
                    markers.addAll(mMarkersGroup.getRestaurants());
                }
                if (mMarkersGroup.getBars() != null && EasyPrefs.getShowBars(mActivity)) {
                    markers.addAll(mMarkersGroup.getBars());
                }
                if (mMarkersGroup.getCafe() != null && EasyPrefs.getShowCafe(mActivity)) {
                    markers.addAll(mMarkersGroup.getCafe());
                }

            } else {
                if (mMarkersGroup.getMuseum() != null && EasyPrefs.getShowMuseums(mActivity)) {
                    markers.addAll(mMarkersGroup.getMuseum());
                }
                if (mMarkersGroup.getLibrary() != null && EasyPrefs.getShowLibrary(mActivity)) {
                    markers.addAll(mMarkersGroup.getLibrary());
                }
                if (mMarkersGroup.getChurch() != null && EasyPrefs.getShowChurch(mActivity)) {
                    markers.addAll(mMarkersGroup.getChurch());
                }
                if (mMarkersGroup.getZoo() != null && EasyPrefs.getShowZoo(mActivity)) {
                    markers.addAll(mMarkersGroup.getZoo());
                }
            }
        }
        showMarkers(markers);
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
                break;

            case FOOD:
                setTitle(getString(R.string.food));
                addFoodSwitchSection();
                break;

            case SIGHTS:
                setTitle(getString(R.string.sights));
                addSightsSwitchSection();
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
                break;
        }
    }

    private void addFoodSwitchSection() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.empty_section_layout, null);
        HeaderView content = view.findViewById(R.id.header_view);
        content.setTitle(getString(R.string.type_of_places));
        View foodSection = LayoutInflater.from(getContext()).inflate(R.layout.food_sections_layout, null);

        FunctionalButton restaurantsSwitch = foodSection.findViewById(R.id.restaurants_switch);
        FunctionalButton cafeSwitch = foodSection.findViewById(R.id.cafe_switch);
        FunctionalButton barSwitch = foodSection.findViewById(R.id.bar_switch);

        restaurantsSwitch.setChecked(EasyPrefs.getShowRestaurants(mActivity));
        cafeSwitch.setChecked(EasyPrefs.getShowCafe(mActivity));
        barSwitch.setChecked(EasyPrefs.getShowBars(mActivity));

        restaurantsSwitch.setOnCheckedChangeListener(new SwitchListener(PlaceType.RESTAURANT));
        cafeSwitch.setOnCheckedChangeListener(new SwitchListener(PlaceType.CAFE));
        barSwitch.setOnCheckedChangeListener(new SwitchListener(PlaceType.BAR));

        content.addView(foodSection);
        mBaseLayout.addView(view);
    }

    private void addSightsSwitchSection() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.empty_section_layout, null);
        HeaderView content = view.findViewById(R.id.header_view);
        content.setTitle(getString(R.string.type_of_places));
        View sightsSection = LayoutInflater.from(getContext()).inflate(R.layout.sights_section_layout, null);

        FunctionalButton museumSwitch = sightsSection.findViewById(R.id.museum_switch);
        FunctionalButton librarySwitch = sightsSection.findViewById(R.id.library_switch);
        FunctionalButton churchSwitch = sightsSection.findViewById(R.id.church_switch);
        FunctionalButton zooSwitch = sightsSection.findViewById(R.id.zoo_switch);

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
            if (infoList.get(i).getMarker() != null) {
                setVenueMarker(infoList.get(i).getMarker());
            }
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

        if (!mAddedViews.contains(model.getTitle())) {
            mAddedViews.add(model.getTitle());
            mBaseLayout.addView(view);
        }
    }

    private void setVenueMarker(VenueMarker marker) {
        MarkerOptions options = new MarkerOptions()
                .position(new LatLng(marker.getLat(), marker.getLng()))
                .title(marker.getTitle())
                .snippet(marker.getSnippet());

        List<MarkerOptions> markerOptions = new ArrayList<>();
        markerOptions.add(options);
        showMarkers(markerOptions);
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
