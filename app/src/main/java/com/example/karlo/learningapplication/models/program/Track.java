package com.example.karlo.learningapplication.models.program;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.api.client.util.DateTime;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

@Entity
public class Track {

    @SerializedName("id")
    @Expose
    @android.arch.persistence.room.PrimaryKey @android.support.annotation.NonNull private int mId;

    @SerializedName("startDate")
    @Expose
    private String mStartDate;

    @SerializedName("endDate")
    @Expose
    private String mEndDate;

    @SerializedName("room")
    @Expose
    private int mRoom;

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("chairs")
    @Expose
    private List<Person> mChairs;

    public Track(@NonNull int mId, String mStartDate, String mEndDate, int mRoom, String mTitle, List<Person> mChairs) {
        this.mId = mId;
        this.mStartDate = mStartDate;
        this.mEndDate = mEndDate;
        this.mRoom = mRoom;
        this.mTitle = mTitle;
        this.mChairs = mChairs;
    }

    @NonNull
    public int getId() {
        return mId;
    }

    public void setId(@NonNull int mId) {
        this.mId = mId;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String mStartDate) {
        this.mStartDate = mStartDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public List<Person> getChairs() {
        return mChairs;
    }

    public void setEndDate(String mEndDate) {
        this.mEndDate = mEndDate;
    }

    public int getRoom() {
        return mRoom;
    }

    public void setRoom(int mRoom) {
        this.mRoom = mRoom;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setChairs(List<Person> mChairs) {
        this.mChairs = mChairs;
    }
}
