package com.example.karlo.learningapplication.models;

import android.arch.persistence.room.Entity;
import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.reactivex.annotations.NonNull;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Karlo on 25.3.2018..
 */

@Entity
public class User extends RealmObject {

    @PrimaryKey
    @SerializedName("userId")
    @Expose
    @android.arch.persistence.room.PrimaryKey @android.support.annotation.NonNull private String userId;

    @SerializedName("mail")
    @Expose
    private String mail;

    @SerializedName("displayName")
    @Expose
    private String displayName;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    public User() {}

    public User(String userId, String mail, String displayName, Uri imageUrl) {
        this.userId = userId;
        this.mail = mail;
        this.displayName = displayName;
        if (imageUrl != null) {
            this.imageUrl = imageUrl.toString();
        }
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
}