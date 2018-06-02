package com.example.karlo.sstconference.models.venue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VenueMarker {

    @SerializedName("lat")
    @Expose
    double lat;

    @SerializedName("lng")
    @Expose
    double lng;

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("snippet")
    @Expose
    String snippet;

    public VenueMarker() {
    }

    public VenueMarker(double lat, double lng, String title, String snippet) {
        this.lat = lat;
        this.lng = lng;
        this.title = title;
        this.snippet = snippet;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(long lng) {
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}
