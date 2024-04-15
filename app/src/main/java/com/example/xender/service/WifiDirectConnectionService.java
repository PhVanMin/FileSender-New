package com.example.xender.service;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.xender.Dialog.MyApplication;
import com.example.xender.wifi.MyWifi;

public class WifiDirectConnectionService extends Service {
    private static final int MY_READ_PERMISSION_CODE =1 ;
    private boolean isSuccess ;
    public static final int NUM_OF_COLLECTION = 10;
    private Thread connectThread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String address = intent.getStringExtra("address");
        isSuccess = false;

        connectThread = new Thread(new Runnable() {
            int count = 0;
            @Override
            public synchronized void run() {
                while(true){
                    if(!isSuccess) {

                        connect(address);
                        count++;
                        if (count >= NUM_OF_COLLECTION)
                        {

                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder b = new AlertDialog.Builder(MyApplication.getActivity());
                                    b.setTitle("Connection Fail");
                                    b.setMessage("Check your wifi and try again!!");
                                    AlertDialog al = b.create();
                                    al.show();
                                }
                            });
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        connectThread.start();



        return super.onStartCommand(intent, flags, startId);
    }
    public  void connect(String address) {
        Log.d("WifiDirect", address);
        WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
        wifiP2pConfig.deviceAddress = address;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MyApplication.getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, MY_READ_PERMISSION_CODE);
            Log.d("WifiDirect", "nooo");
        }

        MyWifi.wifiP2pManager.connect(MyWifi.channel, wifiP2pConfig, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Log.d("WifiDirect", "success");
                isSuccess = true;
                // MyWifi.wifiP2pManager.requestConnectionInfo(MyWifi.channel, MyWifi.connectionInfoListener);
            }

            @Override
            public void onFailure(int reason) {
                if (reason == WifiP2pManager.ERROR) {
                    Log.d("WifiDirect", "Connection failed due to an error.");
                } else if (reason == WifiP2pManager.P2P_UNSUPPORTED) {
                    Log.d("WifiDirect", "Wi-Fi Direct is not supported on this device.");
                } else if (reason == WifiP2pManager.BUSY) {
                    Log.d("WifiDirect", "Connection failed because the system is busy.");
                } else if (reason == WifiP2pManager.NO_SERVICE_REQUESTS) {
                    Log.d("WifiDirect", "No valid service requests to connect to.");
                } else {
                    Log.d("WifiDirect", "Connection failed with unknown reason: " + reason);
                }

            }

        });

    }


}
