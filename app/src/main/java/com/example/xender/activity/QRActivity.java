package com.example.xender.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.xender.R;
import com.example.xender.fragment.WifiQrFragment;
import com.example.xender.wifi.WifiDirectBroadcastReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class QRActivity extends AppCompatActivity  {
    Toolbar toolbar;
    NavController navController;

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver receiver;

    private String myWifiAddress ="abcs";
    private WifiManager wifiManager;
   public ImageView qr;
    IntentFilter intentFilter;
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
        NavigationUI.setupWithNavController(navigationView,navController);

        wifiManager= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        qr=findViewById(R.id.Qr_code);
        receiver = new WifiDirectBroadcastReceiver(manager, channel, this,qr);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


    }



    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public String getDeviceAddress() {
        WifiDirectBroadcastReceiver broadcastReceiver = (WifiDirectBroadcastReceiver) receiver;
        return broadcastReceiver.getDeviceAddress();
    }

    public String getMyWifiAddress() {
        return myWifiAddress;
    }

    public void setMyWifiAddress(String myWifiAddress) {
        this.myWifiAddress = myWifiAddress;
    }
}