package com.example.xender.wifi;

import android.content.BroadcastReceiver;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;

import com.example.xender.handler.Client;
import com.example.xender.handler.Server;

import java.net.Socket;

public class MyWifi {
    public static WifiP2pManager wifiP2pManager ;
    public static WifiManager wifiManager;
    public static WifiP2pManager.Channel channel;
    public static WifiDirectBroadcastReceiver broadcastReceiver;
    public static boolean isServer = false;
    public static Socket socket = null;




}
