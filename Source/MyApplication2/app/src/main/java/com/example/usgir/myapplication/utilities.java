package com.example.usgir.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by girish on 4/19/2017.
 */

public class utilities {

    public static boolean isConnectionAvailable(Context ctx) {
        boolean bConnection = false;
        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return bConnection;
    }
}
