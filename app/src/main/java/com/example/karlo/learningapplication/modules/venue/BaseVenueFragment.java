package com.example.karlo.learningapplication.modules.venue;

import android.support.v4.app.Fragment;

public class BaseVenueFragment extends Fragment {

    protected String mTitle;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }
}
