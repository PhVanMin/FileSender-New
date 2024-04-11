package com.example.xender.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xender.R;
import com.example.xender.activity.QRActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;

    private QRActivity activity;
    private ImageView qr_code;
    private  String address;


    public WifiDirectBroadcastReceiver(WifiP2pManager wifiP2pManager,
                                       WifiP2pManager.Channel channel,
                                       QRActivity activity) {
        this.wifiP2pManager = wifiP2pManager;
        this.channel = channel;
        this.activity = activity;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.d("Wifi actionn",action);
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Log.v("WiFiii","Enable");
                Toast.makeText(context,"Wifi is ON",Toast.LENGTH_SHORT).show();
            } else {
                Log.v("WiFiii","Disable");
                Toast.makeText(context,"Wifi is OFF",Toast.LENGTH_SHORT).show();
            }


        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            Log.d("Wifi ", "changed action peers");

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            Log.d("Wifi device", "changed action connection");
            if(wifiP2pManager == null){
                return ;
            }
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if(networkInfo.isConnected()){
                wifiP2pManager.requestConnectionInfo(channel,activity.connectionInfoListener);
            } else {
                Toast.makeText(activity, "Disconnect", Toast.LENGTH_SHORT).show();
            }
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

            WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            String thisDeviceName = device.deviceName;


            address = device.deviceAddress;
            Log.d("Wifi device", address);

            activity.wifiQrFragment.generateQRCode(address);
        }

    }


    public String getDeviceAddress() {
        return address;
    }
}
