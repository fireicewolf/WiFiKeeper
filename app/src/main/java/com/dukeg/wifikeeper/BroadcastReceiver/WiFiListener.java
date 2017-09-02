package com.dukeg.wifikeeper.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by john on 8/29/17.
 * This is a broadcast receiver for wifi switch on/off
 */


public class WiFiListener extends BroadcastReceiver{
    private final String TAG = "WiFi Keeper";

    @Override
    public void onReceive(Context context, Intent intent) {
        WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        NetworkInfo mNetworkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

        if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){//wifi连接上与否

            if (mNetworkInfo.getState().equals(NetworkInfo.State.DISCONNECTING)){
                Log.e(TAG,"wifi网络正在断开");
            }
            else if(mNetworkInfo.getState().equals(NetworkInfo.State.DISCONNECTED)){
                Log.e(TAG,"wifi网络连接断开");
            }
            else if(mNetworkInfo.getState().equals(NetworkInfo.State.CONNECTING)){
                Log.e(TAG,"wifi网络正在连接");
            }
            else if(mNetworkInfo.getState().equals(NetworkInfo.State.CONNECTED)){


                WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                //获取当前wifi名称
                String currentWifiSSID = wifiInfo.getSSID();

                Log.e(TAG,"连接到网络 " + currentWifiSSID);

            }

        }
        else if(intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){//wifi打开与否
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);

            if(wifiState == WifiManager.WIFI_STATE_DISABLED){
                Log.e(TAG,"系统关闭wifi");
                mWifiManager.setWifiEnabled(true);
            }
            else if(wifiState == WifiManager.WIFI_STATE_ENABLED){
                Log.e(TAG,"系统开启wifi");
            }
        }
    }
}