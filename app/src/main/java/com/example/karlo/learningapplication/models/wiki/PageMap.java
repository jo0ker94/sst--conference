package com.example.karlo.learningapplication.models.wiki;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Karlo on 31.3.2018..
 */

public class PageMap {

    @SerializedName("cse_image")
    @Expose
    private List<CseImage> mCseImage;

    public List<CseImage> getCseImage() {
        return mCseImage;
    }
}
