package com.dukeg.wifikeeper.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by john on 8/29/17.
 * This is a broadcast receiver for wifi switch on/off
 */


public class WiFiListener {

    private Context mContext;
    private WiFiBroadcastReceiver receiver;
    private WiFiStateListener mWiFiStateListener;

    public WiFiListener(Context context) {
        mContext = context;
        receiver = new WiFiBroadcastReceiver();
    }

    public void register(WiFiStateListener listener) {
        mWiFiStateListener = listener;
        if (receiver != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            mContext.registerReceiver(receiver, filter);
        }
    }

    public void unregister() {
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
        }
    }

    private class WiFiBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                /**
                 * wifi状态改变
                 *
                 */
                if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                    if (mWiFiStateListener != null) {
                        Log.e("zhang", "WiFiBroadcastReceiver --> onReceive--> WIFI_STATE_CHANGED_ACTION");
                        mWiFiStateListener.onStateChanged();
                    }
                }
                /**
                 * WIFI_STATE_DISABLED    WLAN已经关闭
                 * WIFI_STATE_DISABLING   WLAN正在关闭
                 * WIFI_STATE_ENABLED     WLAN已经打开
                 * WIFI_STATE_ENABLING    WLAN正在打开
                 * WIFI_STATE_UNKNOWN     未知
                 */
                int wifiSwitchState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (wifiSwitchState) {
                    case WifiManager.WIFI_STATE_DISABLED:
                        if (mWiFiStateListener != null) {
                            Log.e("WiFi Keeper", "WiFiBroadcastReceiver --> onReceive--> WIFI_STATE_DISABLED");
                            mWiFiStateListener.onStateDisabled();
                        }
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                        if (mWiFiStateListener != null) {
                            Log.e("WiFi Keeper", "WiFiBroadcastReceiver --> onReceive--> WIFI_STATE_DISABLING");
                            mWiFiStateListener.onStateDisabling();
                        }
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        if (mWiFiStateListener != null) {
                            Log.e("WiFi Keeper", "WiFiBroadcastReceiver --> onReceive--> WIFI_STATE_ENABLED");
                            mWiFiStateListener.onStateEnabled();
                            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
                                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                                if(info.getState().equals(NetworkInfo.State.DISCONNECTED)){
                                    Log.e("WiFi Keeper", "wifi网络连接断开");
                                }
                                else if(info.getState().equals(NetworkInfo.State.CONNECTED)){

                                    WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                                    //获取当前wifi名称
                                    Log.e("WiFi Keeper", "连接到网络 " + wifiInfo.getSSID());
                                }
                            }
                        }
                        break;
                    case WifiManager.WIFI_STATE_ENABLING:
                        if (mWiFiStateListener != null) {
                            Log.e("WiFi Keeper", "WiFiBroadcastReceiver --> onReceive--> WIFI_STATE_ENABLING");
                            mWiFiStateListener.onStateEnabling();
                        }
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                        if (mWiFiStateListener != null) {
                            Log.e("WiFi Keeper", "WiFiBroadcastReceiver --> onReceive--> WIFI_STATE_UNKNOWN");
                            mWiFiStateListener.onStateUnknow();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }


    public interface WiFiStateListener {
        void onStateChanged();

        void onStateDisabled();

        void onStateDisabling();

        void onStateEnabled();

        void onStateEnabling();

        void onStateUnknow();

    }
}
