
package com.example.karlo.learningapplication.models.nearbyplaces;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geometry {

    @SerializedName("location")
    @Expose
    private LocationCoordinates locationCoordinates;

    public LocationCoordinates getLocationCoordinates() {
        return locationCoordinates;
    }

    public void setLocationCoordinates(LocationCoordinates locationCoordinates) {
        this.locationCoordinates = locationCoordinates;
    }

}
