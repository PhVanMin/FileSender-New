package com.example.xender.activity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.xender.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ConnectActivity extends AppCompatActivity {

    private static final int MY_READ_PERMISSION_CODE = 1;
    Button scan_btn;
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);

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


    public void connect(String address) {
        Log.d("WifiDirect", address);
        WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
        wifiP2pConfig.deviceAddress = address;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, MY_READ_PERMISSION_CODE);
            Log.d("WifiDirect", "nooo");
        }
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("WifiDirect","discover successs");
            }

            @Override
            public void onFailure(int reason) {
                Log.d("WifiDirect","discover fail");
            }
        });
        manager.connect(channel, wifiP2pConfig, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("WifiDirect", "success");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_READ_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }

        }

    }

}