package com.example.xender.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.xender.Dialog.MyApplication;
import com.example.xender.permission.PermissionChecker;
import com.example.xender.wifi.MyBroadcastReceiver;
import com.example.xender.wifi.MyWifi;
import com.example.xender.wifi.WifiDirectBroadcastReceiver;

public class WifiDirectHandlerService extends Service {

    String TAG = "WifiDirectHandlerService";


    public static boolean isRunning = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Log.d(TAG, "onStartCommand: ");
        MyWifi.wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        MyWifi.wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        MyWifi.channel = MyWifi.wifiP2pManager.initialize(this, getMainLooper(), null);
        MyWifi.myBroadcastReceiver = new MyBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        registerReceiver(MyWifi.myBroadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED);

        if (PermissionChecker.checkAccessWifiState(MyApplication.getActivity())) {
            MyWifi.wifiManager.setWifiEnabled(true);

            if (PermissionChecker.CheckFineLocation(MyApplication.getActivity())) {

            }
        }
        ;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED) {

        }
        MyWifi.wifiP2pManager.discoverPeers(MyWifi.channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("WifiDirect", "discover successs");
            }

            @Override
            public void onFailure(int reason) {
                Log.d("WifiDirect", "discover fail");
            }
        });


        isRunning = true;
        return super.onStartCommand(intent, flags, startId);

    }


}
