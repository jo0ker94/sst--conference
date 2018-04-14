package com.example.karlo.learningapplication.models;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Karlo on 25.3.2018..
 */

public class User extends RealmObject {

    @PrimaryKey
    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("mail")
    @Expose
    private String mail;

    @SerializedName("display_name")
    @Expose
    private String displayName;

    @SerializedName("image_url")
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
}