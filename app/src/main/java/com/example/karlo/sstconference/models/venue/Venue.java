
package com.example.karlo.sstconference.models.venue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Venue {

    @SerializedName("faculty")
    @Expose
    private List<Info> faculty = null;
    @SerializedName("region")
    @Expose
    private List<Info> region = null;

    public List<Info> getFaculty() {
        return faculty;
    }

    public void setFaculty(List<Info> faculty) {
        this.faculty = faculty;
    }

    public List<Info> getRegion() {
        return region;
    }

    public void setRegion(List<Info> region) {
        this.region = region;
    }

}
