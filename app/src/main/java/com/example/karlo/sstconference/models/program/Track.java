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
public class Track extends Program implements Parcelable {

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
    @TypeConverters(PersonConverter.class)
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mStartDate);
        dest.writeString(this.mEndDate);
        dest.writeInt(this.mRoom);
        dest.writeString(this.mTitle);
        dest.writeTypedList(this.mChairs);
    }

    protected Track(Parcel in) {
        this.mId = in.readInt();
        this.mStartDate = in.readString();
        this.mEndDate = in.readString();
        this.mRoom = in.readInt();
        this.mTitle = in.readString();
        this.mChairs = in.createTypedArrayList(Person.CREATOR);
    }

    public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel source) {
            return new Track(source);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };
}
