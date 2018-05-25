package com.example.karlo.learningapplication.modules.venue;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.commons.Constants;
import com.google.android.gms.maps.SupportMapFragment;

import butterknife.Unbinder;

public class VenueFragment extends BaseMapFragment {

    public enum VenueType { CONFERENCE, REGION, FOOD, SIGHTS, FACULTY }

    private Unbinder mUnbinder;

    private VenueActivity mActivity;
    private VenueType mType;

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
        View rootView = inflater.inflate(R.layout.fragment_venue_conference, container, false);
//        mUnbinder = ButterKnife.bind(this, rootView);
        mActivity = (VenueActivity) getActivity();
        readBundle(getArguments());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
                break;
            case REGION:
                setTitle(getString(R.string.region));
                //mText.setText(R.string.region);
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
                //mText.setText(R.string.faculty);
                break;
            case CONFERENCE:
                setTitle(getString(R.string.conference));
                //mText.setText(R.string.conference);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
