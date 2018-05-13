package com.example.karlo.learningapplication.models.program;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class Topic {

    @SerializedName("id")
    @Expose
    @android.arch.persistence.room.PrimaryKey @android.support.annotation.NonNull private int mId;

    @SerializedName("parentId")
    @Expose
    private int mParentId;

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("lecturer")
    @Expose
    private List<Person> mLecturers;

    public Topic(@NonNull int mId, int mParentId, String mTitle, List<Person> mLecturers) {
        this.mId = mId;
        this.mParentId = mParentId;
        this.mTitle = mTitle;
        this.mLecturers = mLecturers;
    }

    @NonNull
    public int getId() {
        return mId;
    }

    public void setId(@NonNull int mId) {
        this.mId = mId;
    }

    public int getParentId() {
        return mParentId;
    }

    public void setParentId(int mParentId) {
        this.mParentId = mParentId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public List<Person> getLecturers() {
        return mLecturers;
    }

    public void setLecturers(List<Person> mLecturers) {
        this.mLecturers = mLecturers;
    }
}
