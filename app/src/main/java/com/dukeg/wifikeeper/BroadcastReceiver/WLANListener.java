package com.dukeg.wifikeeper.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.dukeg.wifikeeper.LogUtils.Logger;

/**
 * Created by John on 09/09/2017.
 * This is a dynamic method for Wi-Fi status broadcast receiver.
 */

public class WLANListener {

    private Context mContext;
    private WLANBroadcastReceiver receiver;
    private WLANStateListener mWLANStateListener;

    public WLANListener(Context context) {
        mContext = context;
        receiver = new WLANBroadcastReceiver();
    }

    public void register(WLANStateListener listener) {
        mWLANStateListener = listener;
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

    private class WLANBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();

                int switchState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (switchState) {
                    case WifiManager.WIFI_STATE_DISABLED:
                        if (mWLANStateListener != null) {
                            Logger.d("Wi-Fi开关关闭");
                            mWLANStateListener.onStateDisabled();
                        }
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        if (mWLANStateListener != null) {
                            Logger.d("Wi-Fi开关打开");
                            mWLANStateListener.onStateEnabled();
                        }
                        break;
                    default:
                        break;
                }

                WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                NetworkInfo mNetworkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                //wifi是否连接上
                if(WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)){
                    if(mNetworkInfo.getState().equals(NetworkInfo.State.DISCONNECTED)){
                        Logger.d("Wi-Fi网络连接断开");
                        mWLANStateListener.onStateDisconnected();
                    }
                    else if(mNetworkInfo.getState().equals(NetworkInfo.State.CONNECTED)){
                        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                        //获取当前wifi名称
                        String currentWifiSSID = wifiInfo.getSSID();
                        Logger.d("连接到Wi-Fi网络 " + currentWifiSSID);
                        mWLANStateListener.onStateConnected();
                    }
                }
            }
        }
    }


    public interface WLANStateListener {

        void onStateDisabled();

        void onStateEnabled();

        void onStateDisconnected();

        void onStateConnected();
    }
}
