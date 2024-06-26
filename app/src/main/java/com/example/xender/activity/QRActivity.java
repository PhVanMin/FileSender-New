package com.example.xender.activity;

import static com.example.xender.wifi.MyWifi.myWifiAddress;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xender.Bluetooth.MyBluetooth;
import com.example.xender.Dialog.MyApplication;
import com.example.xender.R;
import com.example.xender.fragment.BluetoothQrFragment;
import com.example.xender.fragment.WifiQrFragment;
import com.example.xender.handler.Client;
import com.example.xender.handler.Server;
import com.example.xender.permission.PermissionChecker;
import com.example.xender.service.WifiDirectHandlerService;
import com.example.xender.wifi.MyWifi;
import com.example.xender.wifi.WifiDirectBroadcastReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.InetAddress;

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
    public BluetoothQrFragment bluetoothQrFragment;
    IntentFilter intentFilter;
    public static int ACCESS_WIFI_STATE = 101;
    public static int NEARBY_WIFI_DEVICE = 102;
    public static int WRITE_EXTERNAL_STORAGE = 103;
    public static int BLUETOOTH = 105;

    public static int ACCESS_FINE_LOCATION = 104;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);

        toolbar = findViewById(R.id.appbar_send);
        toolbar.setTitle("Kết nối QR");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> finish());

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView navigationView = findViewById(R.id.navigation_view);
        NavigationUI.setupWithNavController(navigationView, navController);


//        if (MyWifi.wifiManager == null)
//            MyWifi.wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        if (MyWifi.wifiP2pManager == null)
//            MyWifi.wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
//
//        if (MyWifi.channel == null)
//            MyWifi.channel = MyWifi.wifiP2pManager.initialize(this, getMainLooper(), null);
//
//        if (MyWifi.broadcastReceiver == null)
//            MyWifi.broadcastReceiver = new WifiDirectBroadcastReceiver(MyWifi.wifiP2pManager, MyWifi.channel, this);
//

        if (!WifiDirectHandlerService.isRunning ) {
            Intent intent = new Intent(this, WifiDirectHandlerService.class);
            startService(intent);
        }
        qr = findViewById(R.id.Qr_code);
//        if (PermissionChecker.checkAccessWifiState(this) ){
//            MyWifi.wifiManager.setWifiEnabled(true);
//
//            if (PermissionChecker.CheckFineLocation(this)) {
//
//            }
//        };



//        intentFilter = new IntentFilter();
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        //wifiQrFragment.generateQRCode(MyWifi.broadcastReceiver.getDeviceAddress());


//        MyWifi.wifiP2pManager.createGroup(MyWifi.channel, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                // Device is ready to accept incoming connections from peers.
//            }
//
//            @Override
//            public void onFailure(int reason) {
//
//            }
//        });




    }
    @Override
    protected void onResume() {
        super.onResume();
        if(myWifiAddress != null)
            wifiQrFragment.generateQRCode(myWifiAddress);
      // registerReceiver(MyWifi.myBroadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED);
        MyApplication.setActivity(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
       // unregisterReceiver(MyWifi.broadcastReceiver);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_WIFI_STATE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "access wifi", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

                    MyWifi.wifiManager.setWifiEnabled(true);
                } else {
                    Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
                    startActivityForResult(panelIntent, 1);
                }
                PermissionChecker.CheckFineLocation(this) ;
            } else {
                Toast.makeText(this, "not access wifi", Toast.LENGTH_SHORT).show();
            }
        }
        

        if (requestCode == ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PermissionChecker.checkWriteExternalStorage(this) ;
            } else {
                Toast.makeText(this, "not access wifi", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == NEARBY_WIFI_DEVICE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "access nearby device", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == BLUETOOTH) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bluetoothQrFragment.getConnection();
            } else {
                Toast.makeText(this, "not access bluetooth", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Client client;
//    public   WifiP2pManager.ConnectionInfoListener connectionInfoListener=new WifiP2pManager.ConnectionInfoListener() {
//        @Override
//        public void onConnectionInfoAvailable(WifiP2pInfo info) {
//            final InetAddress groupOwnerAddress = info.groupOwnerAddress;
//            if(info.groupFormed && info.isGroupOwner){
//                Log.d("wifiDirect","is server");
//                MyWifi.isServer= true;
//                Server server = Server.getServer();
//                if(!server.isAlive())
//                    server.start();
//
//            } else if (info.groupFormed){
//                Log.d("wifiDirect","is client");
//                if(MyWifi.socket == null) {
//                    client = new Client(groupOwnerAddress);
//                    client.start();
//                }
//            }
//        }
//    };

    @Override
    protected void onStop() {
        super.onStop();
        if (client != null)
            client.cleanup();
    }
}