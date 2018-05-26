package com.example.karlo.sstconference.modules.venue;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.models.venue.Info;
import com.example.karlo.sstconference.models.venue.Venue;
import com.example.karlo.sstconference.ui.HeaderView;
import com.google.android.gms.maps.SupportMapFragment;
import com.squareup.picasso.Picasso;

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

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        switch (mType) {
            case FOOD:
                setTitle(getString(R.string.food));
                mLocationSet.observe(this, hasLocation -> {
                    if (hasLocation != null && hasLocation) {
                        mViewModel.fetchRestaurants(mCurrentLocation);
                    }
                });
                addFoodSections();
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
            case SIGHTS:
                setTitle(getString(R.string.sights));
                mLocationSet.observe(this, hasLocation -> {
                    if (hasLocation != null && hasLocation) {
                        mViewModel.fetchMuseums(mCurrentLocation);
                    }
                });
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
        }
    }

    private void addFoodSections() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.empty_section_layout, null);
        HeaderView content = view.findViewById(R.id.header_view);
        content.setTitle(getString(R.string.type_of_places));
        View foodSection = LayoutInflater.from(getContext()).inflate(R.layout.food_sections_layout, null);
        content.addView(foodSection);
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
}
