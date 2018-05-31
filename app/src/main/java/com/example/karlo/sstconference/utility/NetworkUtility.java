package com.example.karlo.sstconference.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.modules.home.HomeActivity;

public class NetworkUtility {

    public static boolean hasNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static void showNoNetworkDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.we_are_sorry)
                .setCancelable(false)
                .setMessage(R.string.error_check_internet_connection)
                .setPositiveButton(R.string.ok, null)
                .show();
    }
}
