package com.dukeg.wifikeeper.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dukeg.wifikeeper.BroadcastReceiver.wifiListener;
import com.dukeg.wifikeeper.LogUtils.logger;

/**
 * Created by John on 09/09/2017.
 * This is a service for WiFi broadcast receiver working.
 */

public class wifiMonitor extends Service{

    private wifiListener listener;

    @Override
    public void onCreate() {
        super.onCreate();
        logger.d("onCreate() executed");

        final WifiManager mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        listener = new wifiListener(this);
        listener.register(new wifiListener.WLANStateListener() {

            @Override
            public void onStateDisabled() {
                if (!mWifiManager.isWifiEnabled()) {
                    logger.d("重新打开WiFi开关");
                    mWifiManager.setWifiEnabled(true);
                }
            }

            @Override
            public void onStateDisconnected() {
            }

            @Override
            public void onStateEnabled() {
            }

            @Override
            public void onStateConnected() {
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        logger.d("onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.d("onDestroy() executed");
        if (listener != null) {
            listener.unregister();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
