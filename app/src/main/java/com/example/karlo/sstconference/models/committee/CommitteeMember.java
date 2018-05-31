package com.example.karlo.sstconference.models.committee;

import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class CommitteeMember implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mName);
        dest.writeString(this.mFacility);
        dest.writeString(this.mType);
    }

    protected CommitteeMember(Parcel in) {
        this.mId = in.readInt();
        this.mName = in.readString();
        this.mFacility = in.readString();
        this.mType = in.readString();
    }

    public static final Parcelable.Creator<CommitteeMember> CREATOR = new Parcelable.Creator<CommitteeMember>() {
        @Override
        public CommitteeMember createFromParcel(Parcel source) {
            return new CommitteeMember(source);
        }

        @Override
        public CommitteeMember[] newArray(int size) {
            return new CommitteeMember[size];
        }
    };
}
