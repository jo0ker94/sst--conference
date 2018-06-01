package com.example.karlo.sstconference;

import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;

public class BaseTest {
    private Resources mResources;

    public BaseTest() {
        mResources = InstrumentationRegistry.getContext().getResources();
    }

    protected String getString(int resId) {
        return mResources.getString(resId);
    }

    protected String getQuantityString(int resId, int quantity) {
        return mResources.getQuantityString(resId, quantity);
    }

    protected void sleep(int length) {
        try {
            Thread.sleep(length);
        } catch (InterruptedException e) {  e.printStackTrace();}
    }

}
