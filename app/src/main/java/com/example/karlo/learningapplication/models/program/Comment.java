package com.example.karlo.learningapplication.models.program;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Comment {

    @SerializedName("id")
    @Expose
    @android.arch.persistence.room.PrimaryKey @android.support.annotation.NonNull private int mId;

    @SerializedName("text")
    @Expose
    private String mText;

    @SerializedName("userId")
    @Expose
    private String mUserId;

    @SerializedName("parentId")
    @Expose
    private int mParentId;

    @SerializedName("timestamp")
    @Expose
    private String mTimestamp;

    public Comment(@NonNull int mId, String mText, String mUserId, int parentId, String mTimestamp) {
        this.mId = mId;
        this.mText = mText;
        this.mUserId = mUserId;
        this.mTimestamp = mTimestamp;
        this.mParentId = parentId;
    }

    @NonNull
    public int getId() {
        return mId;
    }

    public void setId(@NonNull int mId) {
        this.mId = mId;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String mTimestamp) {
        this.mTimestamp = mTimestamp;
    }

    public int getParentId() {
        return mParentId;
    }

    public void setParentId(int mParentId) {
        this.mParentId = mParentId;
    }
}
