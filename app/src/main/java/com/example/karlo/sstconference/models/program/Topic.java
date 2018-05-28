package com.example.karlo.sstconference.models.program;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.karlo.sstconference.models.converters.PersonConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class Topic extends Program implements Parcelable {

    public enum ProgramType { TOPIC, TRACK }

    @SerializedName("id")
    @Expose
    @android.arch.persistence.room.PrimaryKey @android.support.annotation.NonNull private int mId;

    @SerializedName("parentId")
    @Expose
    private int mParentId;

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("type")
    @Expose
    private int mType;

    @SerializedName("lecturer")
    @Expose
    @TypeConverters(PersonConverter.class)
    private List<Person> mLecturers;

    public Topic(@NonNull int mId, int mParentId, String mTitle, List<Person> mLecturers, int type) {
        this.mId = mId;
        this.mParentId = mParentId;
        this.mTitle = mTitle;
        this.mLecturers = mLecturers;
        this.mType = type;
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

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public boolean isTrack() {
        return mType == ProgramType.TRACK.ordinal();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeInt(this.mParentId);
        dest.writeString(this.mTitle);
        dest.writeInt(this.mType);
        dest.writeTypedList(this.mLecturers);
    }

    protected Topic(Parcel in) {
        this.mId = in.readInt();
        this.mParentId = in.readInt();
        this.mTitle = in.readString();
        this.mType = in.readInt();
        this.mLecturers = in.createTypedArrayList(Person.CREATOR);
    }

    public static final Parcelable.Creator<Topic> CREATOR = new Parcelable.Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel source) {
            return new Topic(source);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };
}
