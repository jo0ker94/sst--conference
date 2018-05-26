package com.example.karlo.sstconference.models.wiki;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Karlo on 31.3.2018..
 */

public class SearchInformation {

    @SerializedName("formattedSearchTime")
    @Expose
    private String mSearchTime;
    @SerializedName("formattedTotalResults")
    @Expose
    private String mSearchResults;

    public String getSearchTime() {
        return mSearchTime;
    }

    public String getSearchResults() {
        return mSearchResults;
    }
}
