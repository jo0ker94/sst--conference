package com.example.karlo.sstconference.models.wiki;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Karlo on 31.3.2018..
 */

public class WikiResult {

    @SerializedName("items")
    @Expose
    private List<Item> mItems;
    @SerializedName("searchInformation")
    @Expose
    private SearchInformation mSearchInformation;

    public List<Item> getItems() {
        return mItems;
    }

    public SearchInformation getSearchInformation() {
        return mSearchInformation;
    }
}
