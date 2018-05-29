
package com.example.karlo.sstconference.models.venue;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.karlo.sstconference.models.converters.InfoListConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class Venue {

    @SerializedName("id")
    @Expose
    @android.arch.persistence.room.PrimaryKey @android.support.annotation.NonNull private int id;

    @SerializedName("faculty")
    @Expose
    @TypeConverters(InfoListConverter.class)
    private List<Info> faculty = null;

    @SerializedName("region")
    @Expose
    @TypeConverters(InfoListConverter.class)
    private List<Info> region = null;

    @SerializedName("hotel")
    @Expose
    @TypeConverters(InfoListConverter.class)
    private List<Info> hotel = null;

    public Venue(@NonNull int id, List<Info> faculty, List<Info> region, List<Info> hotel) {
        this.id = id;
        this.faculty = faculty;
        this.region = region;
        this.hotel = hotel;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

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

    public List<Info> getHotel() {
        return hotel;
    }

    public void setHotel(List<Info> hotel) {
        this.hotel = hotel;
    }
}
