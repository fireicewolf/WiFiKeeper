package com.dukeg.wifikeeper.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dukeg.wifikeeper.BroadcastReceiver.WLANListener;
import com.dukeg.wifikeeper.LogUtils.Logger;

/**
 * Created by John on 09/09/2017.
 * This is a service for WiFi broadcast receiver working.
 */

public class WiFiMonitor extends Service{

    private WLANListener listener;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("onCreate() executed");

        final WifiManager mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        listener = new WLANListener(this);
        listener.register(new WLANListener.WLANStateListener() {

            @Override
            public void onStateDisabled() {
                if (!mWifiManager.isWifiEnabled()) {
                    Logger.d("重新打开WiFi开关");
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
        Logger.d("onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("onDestroy() executed");
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
