package com.dukeg.wifikeeper.LogUtils;

import android.util.Log;

import com.dukeg.wifikeeper.BuildConfig;

/**
 * Created by John on 2017/9/7.
 * This is a logger for debug version.
 */

public class Logger {
    private static final boolean isDebug = BuildConfig.DEBUG;

    private static final String TAG = "WiFi_Keeper";

    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }
}
