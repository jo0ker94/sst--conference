package com.example.karlo.sstconference.models.keynote;

import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class KeynoteSpeaker implements Parcelable {

    @SerializedName("id")
    @Expose
    @android.arch.persistence.room.PrimaryKey @android.support.annotation.NonNull private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("facility")
    @Expose
    private String facility;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("abstract")
    @Expose
    private String abstractText;

    public KeynoteSpeaker(int id, String name, String facility, String email, String imageUrl, String title, String abstractText) {
        this.id = id;
        this.name = name;
        this.facility = facility;
        this.email = email;
        this.imageUrl = imageUrl;
        this.title = title;
        this.abstractText = abstractText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.facility);
        dest.writeString(this.email);
        dest.writeString(this.imageUrl);
        dest.writeString(this.title);
        dest.writeString(this.abstractText);
    }

    protected KeynoteSpeaker(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.facility = in.readString();
        this.email = in.readString();
        this.imageUrl = in.readString();
        this.title = in.readString();
        this.abstractText = in.readString();
    }

    public static final Parcelable.Creator<KeynoteSpeaker> CREATOR = new Parcelable.Creator<KeynoteSpeaker>() {
        @Override
        public KeynoteSpeaker createFromParcel(Parcel source) {
            return new KeynoteSpeaker(source);
        }

        @Override
        public KeynoteSpeaker[] newArray(int size) {
            return new KeynoteSpeaker[size];
        }
    };
}
