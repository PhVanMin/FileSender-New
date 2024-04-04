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
    private BluetoothServerSocket serverSocket;

    public MyBluetooth(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME,MY_UUID);
            new AcceptThread().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private class AcceptThread extends Thread {
        private BluetoothSocket socket;

        @Override
        public void run() {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
