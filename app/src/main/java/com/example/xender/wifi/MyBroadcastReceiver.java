package com.example.xender.wifi;

import static com.example.xender.wifi.MyWifi.channel;
import static com.example.xender.wifi.MyWifi.connectionInfoListener;
import static com.example.xender.wifi.MyWifi.myWifiAddress;
import static com.example.xender.wifi.MyWifi.wifiP2pManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.xender.Dialog.MyApplication;
import com.example.xender.activity.QRActivity;

import java.util.Objects;

public class MyBroadcastReceiver extends BroadcastReceiver {
    String TAG="MyBroadcastReceiver";
    public static String ACTION_CANCEL = "actionCancelAlarm";

   public MyBroadcastReceiver(){

   }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, action);
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
             //   Log.v(TAG,"Enable");
                Toast.makeText(context,"Wifi is ON",Toast.LENGTH_SHORT).show();
            } else {
                //Log.v(TAG,"Disable");
                Toast.makeText(context,"Wifi is OFF",Toast.LENGTH_SHORT).show();
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            wifiP2pManager.requestConnectionInfo(channel,connectionInfoListener);
        }
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

//            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
//            if(networkInfo.isConnected())
//                wifiP2pManager.requestConnectionInfo(channel,activity.connectionInfoListener);

            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            myWifiAddress = device.deviceAddress;

            Log.d("Wifi device", myWifiAddress);
            if(MyApplication.getActivity().getClass() == QRActivity.class){
              QRActivity qr = (QRActivity) MyApplication.getActivity();
                  qr.wifiQrFragment.generateQRCode(myWifiAddress);
            }
          //  activity.wifiQrFragment.generateQRCode(address);
        }
    }
}
