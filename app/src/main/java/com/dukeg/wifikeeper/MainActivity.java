package com.dukeg.wifikeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dukeg.wifikeeper.BroadcastReceiver.WiFiListener;

public class MainActivity extends AppCompatActivity {
    private WiFiListener mWiFiListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWiFiListener = new WiFiListener(this);
        mWiFiListener.register(new WiFiListener.WiFiStateListener() {
            @Override
            public void onStateChanged() {
                Log.e("WiFi Keeper", "MainActivity --> onStateChanged--> ");
            }

            @Override
            public void onStateDisabled() {
                Log.e("WiFi Keeper", "MainActivity --> onStateDisabled--> ");
            }

            @Override
            public void onStateDisabling() {
                Log.e("WiFi Keeper", "MainActivity --> onStateDisabling--> ");
            }

            @Override
            public void onStateEnabled() {
                Log.e("WiFi Keeper", "MainActivity --> onStateEnabled--> ");
            }

            @Override
            public void onStateEnabling() {
                Log.e("WiFi Keeper", "MainActivity --> onStateEnabling--> ");
            }

            @Override
            public void onStateUnknow() {
                Log.e("WiFi Keeper", "MainActivity --> onStateUnknow--> ");
            }
        });
    }
    @Override
    protected void onDestroy() {
        if (mWiFiListener != null) {
            mWiFiListener.unregister();
        }
        super.onDestroy();
    }
}
