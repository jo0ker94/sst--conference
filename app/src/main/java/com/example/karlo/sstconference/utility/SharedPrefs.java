package com.example.karlo.sstconference.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import net.globulus.easyprefs.annotation.Pref;
import net.globulus.easyprefs.annotation.PrefClass;
import net.globulus.easyprefs.annotation.PrefMaster;

@PrefClass(autoInclude = false, staticClass = false)
public class SharedPrefs {

    private static SharedPreferences sSecureInstance;

    @PrefMaster
    public static SharedPreferences getSecureInstance(@NonNull Context context) {
        if (sSecureInstance == null) {
            sSecureInstance = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sSecureInstance;
    }


    @Pref boolean guestMode;

    //
    //  Venue places
    //

    @Pref final boolean showRestaurants = true;
    @Pref boolean showCafe;
    @Pref boolean showBars;

    @Pref final boolean showMuseums = true;
    @Pref boolean showChurch;
    @Pref boolean showLibrary;
    @Pref boolean showZoo;

    @Pref final long mapRadius = 2000;

}
