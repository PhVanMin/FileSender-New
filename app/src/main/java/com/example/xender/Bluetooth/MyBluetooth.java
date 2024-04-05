package com.example.xender.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class MyBluetooth {
    public static String TAG = "BluetoothConnectionSevice";
    public static String NAME = "MYAPP";

    public static UUID MY_UUID =
            UUID.fromString("e7203025-4e62-4f0c-8f3b-87ae58178bb7");
    private BluetoothAdapter bluetoothAdapter;


    public String addressMyBluetooth(){
        return bluetoothAdapter.getName();
    }
    public MyBluetooth(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        AcceptThread acceptThread = new AcceptThread();
        acceptThread.start();
    }
    private class AcceptThread extends Thread {
        private BluetoothServerSocket serverSocket;
        private AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME,MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            serverSocket = tmp;
        }

        @Override
        public void run() {
            BluetoothSocket socket = null;
            while (socket != null){
                Log.d(TAG, "Socket's create() method failed");
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*if (socket != null) {
                    Log.d(TAG, "Send recevice");
                }*/
            }

        }

        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
