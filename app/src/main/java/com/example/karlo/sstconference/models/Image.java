package com.example.karlo.sstconference.models;

import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Image implements Parcelable {

    @SerializedName("id")
    @Expose
    @android.arch.persistence.room.PrimaryKey @android.support.annotation.NonNull private int id;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    public Image(@NonNull int id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.imageUrl);
    }

    protected Image(Parcel in) {
        this.id = in.readInt();
        this.imageUrl = in.readString();
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
