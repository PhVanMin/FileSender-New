package com.example.xender.activity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.xender.Bluetooth.BluetoothBroadcastReceiver;
import com.example.xender.Dialog.MyApplication;
import com.example.xender.R;
import com.example.xender.handler.Client;
import com.example.xender.handler.Server;
import com.example.xender.service.WifiDirectConnectionService;
import com.example.xender.service.WifiDirectHandlerService;
import com.example.xender.wifi.MyWifi;
import com.example.xender.wifi.WifiDirectBroadcastReceiver;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.net.InetAddress;

public class ConnectActivity extends AppCompatActivity {

    private static final int MY_READ_PERMISSION_CODE = 1;
    Button scan_btn;
    Toolbar toolbar;
    private IntentFilter intentFilter;

    static boolean isConnect = false;

//    WifiP2pManager manager;
//    WifiP2pManager.Channel channel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        toolbar = findViewById(R.id.appbar_send);
        toolbar.setTitle("Scan QR");
        toolbar.isBackInvokedCallbackEnabled();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());




        scan_btn = findViewById(R.id.scan_btn);
        scan_btn.setOnClickListener(v -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(ConnectActivity.this);
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.setPrompt("Scan a QR code");
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            intentIntegrator.setCaptureActivity(CaptureAct.class);
            intentIntegrator.initiateScan();
        });


        if (!WifiDirectHandlerService.isRunning ) {
            Intent intent = new Intent(this, WifiDirectHandlerService.class);
            startService(intent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            String contents = intentResult.getContents();
            if (contents != null) {
                Log.d("QR Scanner", contents);
                Intent intent = new Intent(this, WifiDirectConnectionService.class);
                intent.putExtra("address",contents);
                startService(intent);

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
    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.setActivity(this);
        //MyWifi.wifiP2pManager.requestConnectionInfo(MyWifi.channel, MyWifi.connectionInfoListener);
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
//        MyWifi.wifiP2pManager.discoverPeers(MyWifi.channel, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                Log.d("WifiDirect","discover successs");
//            }
//
//            @Override
//            public void onFailure(int reason) {
//                Log.d("WifiDirect","discover fail");
//            }
//        });
        MyWifi.wifiP2pManager.connect(MyWifi.channel, wifiP2pConfig, new WifiP2pManager.ActionListener() {
            @Override
            public synchronized  void onSuccess() {
                Log.d("WifiDirect", "success");
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

    private BluetoothDevice bluetoothDevice;

    public void connectBluetooth(String uuid){
        Log.d("Bluetooth", uuid);
        BluetoothManager bluetoothManager = (BluetoothManager) this.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothBroadcastReceiver bluetoothBroadcastReceiver = new BluetoothBroadcastReceiver(this, bluetoothManager, uuid);
    }

}