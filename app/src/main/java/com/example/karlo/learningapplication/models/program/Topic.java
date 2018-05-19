package com.example.karlo.learningapplication.models.program;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.karlo.learningapplication.models.program.converters.PersonConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Topic implements Parcelable {

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
    @TypeConverters(PersonConverter.class)
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeInt(this.mParentId);
        dest.writeString(this.mTitle);
        dest.writeList(this.mLecturers);
    }

    protected Topic(Parcel in) {
        this.mId = in.readInt();
        this.mParentId = in.readInt();
        this.mTitle = in.readString();
        this.mLecturers = new ArrayList<Person>();
        in.readList(this.mLecturers, Person.class.getClassLoader());
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
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
