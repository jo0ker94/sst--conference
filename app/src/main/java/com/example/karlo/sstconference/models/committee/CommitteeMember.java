package com.example.karlo.sstconference.models.committee;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class CommitteeMember {

    @SerializedName("id")
    @Expose
    @android.arch.persistence.room.PrimaryKey @android.support.annotation.NonNull private int mId;

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("facility")
    @Expose
    private String mFacility;

    @SerializedName("type")
    @Expose
    private String mType;

    public CommitteeMember(int id, String name, String facility, String type) {
        this.mId = id;
        this.mName = name;
        this.mFacility = facility;
        this.mType = type;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getFacility() {
        return mFacility;
    }

    public void setFacility(String facility) {
        this.mFacility = facility;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }
}
