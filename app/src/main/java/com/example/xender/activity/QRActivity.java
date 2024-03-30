package com.example.xender.activity;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xender.R;
import com.example.xender.fragment.WifiQrFragment;
import com.example.xender.handler.Client;
import com.example.xender.handler.SendReceiveHandler;
import com.example.xender.handler.Server;
import com.example.xender.wifi.MyWifi;
import com.example.xender.wifi.WifiDirectBroadcastReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class QRActivity extends AppCompatActivity {
    Toolbar toolbar;
    NavController navController;

//    private  WifiP2pManager manager;
//    private WifiP2pManager.Channel channel;
//    private BroadcastReceiver receiver;
//
//
//    private WifiManager wifiManager;
    public ImageView qr;
    public WifiQrFragment wifiQrFragment;
    IntentFilter intentFilter;
    static int ACCESS_WIFI_STATE = 101;
    static int NEARBY_WIFI_DEVICE = 102;
    static int WRITE_EXTERNAL_STORAGE= 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);

        toolbar = findViewById(R.id.appbar_send);
        toolbar.setTitle("Kết nối QR");
        toolbar.isBackInvokedCallbackEnabled();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView navigationView = findViewById(R.id.navigation_view);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (MyWifi.wifiManager == null)
            MyWifi.wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        if(MyWifi.wifiP2pManager == null)
            MyWifi.wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);

        if (MyWifi.channel == null)
            MyWifi.channel = MyWifi.wifiP2pManager.initialize(this, getMainLooper(), null);

        qr = findViewById(R.id.Qr_code);


        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_WIFI_STATE},
                    ACCESS_WIFI_STATE);
        } else {
            if (! MyWifi.wifiManager.isWifiEnabled()) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    Log.d("Wifiii", "<Q");
                    MyWifi.wifiManager.setWifiEnabled(true);
                } else {
                    Log.d("Wifiii", ">Q");
                    Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
                    startActivityForResult(panelIntent, 1);
                }
            }
        }

        if(MyWifi.broadcastReceiver == null)
            MyWifi.broadcastReceiver = new WifiDirectBroadcastReceiver(MyWifi.wifiP2pManager, MyWifi.channel, this);

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    NEARBY_WIFI_DEVICE);
        } else {
            Toast.makeText(this, "access nearby devicee", Toast.LENGTH_SHORT).show();
        }
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.NEARBY_WIFI_DEVICES},
                    NEARBY_WIFI_DEVICE);
        } else {
            Toast.makeText(this, "access nearby devicee", Toast.LENGTH_SHORT).show();
        }

        if(ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "write file not access", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                   WRITE_EXTERNAL_STORAGE);
        }else {
            Toast.makeText(this, "write file access", Toast.LENGTH_SHORT).show();
        }



        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

       //wifiQrFragment.generateQRCode(MyWifi.broadcastReceiver.getDeviceAddress());

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

    }



    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(MyWifi.broadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED);

    }

    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MyWifi.broadcastReceiver);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_WIFI_STATE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "access wifi", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    Log.d("Wifiii", "<Q");
                    MyWifi.wifiManager.setWifiEnabled(true);
                } else {
                    Log.d("Wifiii", ">Q");
                    Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
                    startActivityForResult(panelIntent, 1);
                }

            } else {
                Toast.makeText(this, "not access wifi", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == NEARBY_WIFI_DEVICE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "access nearby device", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(this, "not access nearby device", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "write external storage", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "not write external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private Client client;
    public   WifiP2pManager.ConnectionInfoListener connectionInfoListener=new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            final InetAddress groupOwnerAddress = info.groupOwnerAddress;
            if(info.groupFormed && info.isGroupOwner){
                Log.d("wifiDirect","is server");

                MyWifi.isServer= true;
                Server server = Server.getServer();


                if(!server.isAlive())
                    server.start();



            } else if (info.groupFormed){
                Log.d("wifiDirect","is client");
                client = new Client(groupOwnerAddress);

                client.start();


               // handler.start();
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (client != null)
            client.cleanup();
    }
}