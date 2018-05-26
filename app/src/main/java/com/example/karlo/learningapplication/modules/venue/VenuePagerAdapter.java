package com.example.karlo.learningapplication.modules.venue;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

public class VenuePagerAdapter extends FragmentStatePagerAdapter {

    private final int COUNT = 5;

    private SparseArray<BaseMapFragment> cachedFragments = new SparseArray<>(COUNT);

    public VenuePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return VenueFragment.newInstance(VenueFragment.VenueType.CONFERENCE);
            case 1:
                return VenueFragment.newInstance(VenueFragment.VenueType.REGION);
            case 2:
                return VenueFragment.newInstance(VenueFragment.VenueType.FOOD);
            case 3:
                return VenueFragment.newInstance(VenueFragment.VenueType.SIGHTS);
            default:
                return VenueFragment.newInstance(VenueFragment.VenueType.FACULTY);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BaseMapFragment fragment = (BaseMapFragment) super.instantiateItem(container, position);
        cachedFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        cachedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    public BaseMapFragment getFragmentByPosition(int position) {
        return cachedFragments.get(position);
    }
}