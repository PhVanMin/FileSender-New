package com.example.xender.wifi;

import android.content.BroadcastReceiver;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.example.xender.handler.Client;
import com.example.xender.handler.SendReceiveHandler;
import com.example.xender.handler.Server;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyWifi {
    public static WifiP2pManager wifiP2pManager ;
    public static WifiManager wifiManager;
    public static WifiP2pManager.Channel channel;
    public static WifiDirectBroadcastReceiver broadcastReceiver;
    public static boolean isServer = false;
    public static Socket socket = null;

    public static void sendFile(File current) {

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(current.getAbsolutePath()));
            Log.d("WifiDirect", "onItemClick: " + MyWifi.socket.toString());
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    SendReceiveHandler handler = new SendReceiveHandler(MyWifi.socket);
                    try {
                        handler.writeLong(current.length());
                        handler.writeUTF(current.getName());
                        handler.write(bytes);
                    } catch (IOException e) {
                        Log.d("WifiDirect", "Exception " + e.toString());
                        throw new RuntimeException(e);
                    }
                }
            });
            thread.start();
            Log.d("WifiDirect", "onItemClick: ");
        } catch (Exception e) {
            Log.d("WifiDirect", "onItemClick: " + e.toString());
            throw new RuntimeException(e);
        }
    }

}
