package com.dukeg.wifikeeper.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.dukeg.wifikeeper.LogUtils.Logger;

/**
 * Created by John on 8/29/17.
 * This is a broadcast receiver for wifi switch on/off
 */


public class WiFiListener extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        NetworkInfo mNetworkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        //wifi是否连接上
        if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
            if(mNetworkInfo.getState().equals(NetworkInfo.State.DISCONNECTED)){
                Logger.d("WiFi网络连接断开");
                Toast.makeText(context, "WiFi网络连接断开", Toast.LENGTH_SHORT).show();
            }
            else if(mNetworkInfo.getState().equals(NetworkInfo.State.CONNECTED)){
                WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                //获取当前wifi名称
                String currentWifiSSID = wifiInfo.getSSID();
                Logger.d("连接到网络 " + currentWifiSSID);
                Toast.makeText(context, "连接到WiFI:" + currentWifiSSID, Toast.LENGTH_SHORT).show();
            }
        }
        //wifi打开与否
        else if(intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);

            if(wifiState == WifiManager.WIFI_STATE_DISABLED){
                Logger.d("WiFi开关已关闭");
                if (!mWifiManager.isWifiEnabled()) {
                    Logger.d("重新打开WiFi开关");
                    mWifiManager.setWifiEnabled(true);
                }
            }
            else if(wifiState == WifiManager.WIFI_STATE_ENABLED){
                Logger.d("WiFi开关已打开");
            }
        }
    }
}