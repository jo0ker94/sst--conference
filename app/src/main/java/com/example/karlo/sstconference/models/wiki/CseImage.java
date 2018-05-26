package com.example.karlo.sstconference.models.wiki;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Karlo on 31.3.2018..
 */

public class CseImage {

    @SerializedName("src")
    @Expose
    private String mSrc;

    public String getSrc() {
        return mSrc;
    }
}
