package com.example.karlo.sstconference.models.venue;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MarkersGroup {

    private List<MarkerOptions> mRestaurants;
    private List<MarkerOptions> mCafe;
    private List<MarkerOptions> mBars;

    private List<MarkerOptions> mMuseum;
    private List<MarkerOptions> mLibrary;
    private List<MarkerOptions> mChurch;
    private List<MarkerOptions> mZoo;

    public List<MarkerOptions> getRestaurants() {
        return mRestaurants;
    }

    public void setRestaurants(List<MarkerOptions> restaurants) {
        this.mRestaurants = restaurants;
    }

    public List<MarkerOptions> getCafe() {
        return mCafe;
    }

    public void setCafe(List<MarkerOptions> cafe) {
        this.mCafe = cafe;
    }

    public List<MarkerOptions> getBars() {
        return mBars;
    }

    public void setBars(List<MarkerOptions> bars) {
        this.mBars = bars;
    }

    public List<MarkerOptions> getMuseum() {
        return mMuseum;
    }

    public void setMuseum(List<MarkerOptions> museum) {
        this.mMuseum = museum;
    }

    public List<MarkerOptions> getLibrary() {
        return mLibrary;
    }

    public void setLibrary(List<MarkerOptions> library) {
        this.mLibrary = library;
    }

    public List<MarkerOptions> getChurch() {
        return mChurch;
    }

    public void setChurch(List<MarkerOptions> church) {
        this.mChurch = church;
    }

    public List<MarkerOptions> getZoo() {
        return mZoo;
    }

    public void setZoo(List<MarkerOptions> zoo) {
        this.mZoo = zoo;
    }
}
