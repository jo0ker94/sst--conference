package com.example.karlo.learningapplication.models.wiki;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Karlo on 31.3.2018..
 */

public class Item {

    @SerializedName("title")
    @Expose
    private String mTitle;
    @SerializedName("link")
    @Expose
    private String mLink;
    @SerializedName("snippet")
    @Expose
    private String mSnippet;
    @SerializedName("htmlSnippet")
    @Expose
    private String mHtmlSnippet;
    @SerializedName("pagemap")
    @Expose
    private PageMap mPageMap;

    public String getTitle() {
        return mTitle;
    }

    public String getLink() {
        return mLink;
    }

    public String getSnippet() {
        return mSnippet;
    }

    public String getHtmlSnippet() {
        return mHtmlSnippet;
    }

    public PageMap getPagemap() {
        return mPageMap;
    }

}
