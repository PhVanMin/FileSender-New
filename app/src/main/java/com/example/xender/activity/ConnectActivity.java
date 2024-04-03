package com.example.xender.activity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.fragment.DialogFragmentNavigatorDestinationBuilder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.xender.R;
import com.example.xender.handler.Client;
import com.example.xender.handler.Server;
import com.example.xender.wifi.MyWifi;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.net.InetAddress;

public class ConnectActivity extends AppCompatActivity {

    private static final int MY_READ_PERMISSION_CODE = 1;
    Button scan_btn;
    private IntentFilter intentFilter;
//    WifiP2pManager manager;
//    WifiP2pManager.Channel channel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        if(MyWifi.wifiP2pManager == null)
            MyWifi.wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);

        if (MyWifi.channel == null)
            MyWifi.channel = MyWifi.wifiP2pManager.initialize(this, getMainLooper(), null);



        scan_btn = findViewById(R.id.scan_btn);
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(ConnectActivity.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("Scan a QR code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.setCaptureActivity(CaptureAct.class);
                intentIntegrator.initiateScan();

            }
        });
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            String contents = intentResult.getContents();
            if (contents != null) {
                Log.d("QR Scanner", contents);
                connect(contents);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_READ_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }

        }

    }
   public static WifiP2pManager.ConnectionInfoListener connectionInfoListener=new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            final InetAddress groupOwnerAddress = info.groupOwnerAddress;
            if(info.groupFormed && info.isGroupOwner){
                Log.d("wifiDirect","is server");
                MyWifi.isServer= true;
                Server server = Server.getServer();
                MyWifi.socket = server.getSocket();
                if(!server.isAlive())
                    server.start();
            } else if (info.groupFormed){
                Log.d("wifiDirect","is client");
                Client client = new Client(groupOwnerAddress);
                MyWifi.socket = client.getSocket();
                client.start();

            }
        }
    };
    public void connect(String address) {
        Log.d("WifiDirect", address);
        WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
        wifiP2pConfig.deviceAddress = address;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, MY_READ_PERMISSION_CODE);
            Log.d("WifiDirect", "nooo");
        }
        MyWifi.wifiP2pManager.discoverPeers(MyWifi.channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("WifiDirect","discover successs");
            }

            @Override
            public void onFailure(int reason) {
                Log.d("WifiDirect","discover fail");
            }
        });
        MyWifi.wifiP2pManager.connect(MyWifi.channel, wifiP2pConfig, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

                Log.d("WifiDirect", "success");

                MyWifi.wifiP2pManager.requestConnectionInfo(MyWifi.channel,connectionInfoListener);

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