package com.example.karlo.sstconference.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.net.Uri;

import com.example.karlo.sstconference.models.converters.IntegerListConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by Karlo on 25.3.2018..
 */

@Entity
public class User {

    @SerializedName("userId")
    @PrimaryKey
    @Expose
    @android.support.annotation.NonNull
    private String userId;

    @SerializedName("mail")
    @Expose
    private String mail;

    @SerializedName("displayName")
    @Expose
    private String displayName;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @TypeConverters(IntegerListConverter.class)
    @SerializedName("subscribedEvents")
    @Expose
    private List<Integer> subscribedEvents;

    public User() {}

    public User(@NonNull String userId, String mail, String displayName, Uri imageUrl) {
        this(userId, mail, displayName, imageUrl, null);
    }

    public User(@NonNull String userId, String mail, String displayName, Uri imageUrl, List<Integer> subscribedEvents) {
        this.userId = userId;
        this.mail = mail;
        this.displayName = displayName;
        if (imageUrl != null) {
            this.imageUrl = imageUrl.toString();
        }
        this.subscribedEvents = subscribedEvents;
    }

    public String getUserId() {
        return userId;
    }

    public String getMail() {
        return mail;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Integer> getSubscribedEvents() {
        return subscribedEvents;
    }

    public void setSubscribedEvents(List<Integer> subscribedEvents) {
        this.subscribedEvents = subscribedEvents;
    }
}