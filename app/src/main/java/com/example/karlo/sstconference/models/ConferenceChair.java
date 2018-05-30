package com.example.karlo.sstconference.models;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class ConferenceChair {

    @SerializedName("id")
    @Expose
    @android.arch.persistence.room.PrimaryKey @android.support.annotation.NonNull private int id;

    @SerializedName("chairTitle")
    @Expose
    private String chairTitle;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("facility")
    @Expose
    private String facility;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;

    public String getChairTitle() {
        return chairTitle;
    }

    public String getEmail() {
        return email;
    }

    public String getFacility() {
        return facility;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setChairTitle(String chairTitle) {
        this.chairTitle = chairTitle;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
