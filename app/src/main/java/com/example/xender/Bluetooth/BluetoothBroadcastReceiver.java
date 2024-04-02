package com.example.xender.Bluetooth;

import static android.net.VpnProfileState.STATE_CONNECTING;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.UUID;

public class BluetoothBroadcastReceiver {
    private static final String TAG = "BluetoothConnectionSevice";
    private static final String NAME = "MYAPP";

    private final UUID MY_UUID =
            UUID.fromString("e7203025-4e62-4f0c-8f3b-87ae58178bb7");
    private BluetoothAdapter bluetoothAdapter;

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {

                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }
        public void run(){
            BluetoothSocket socket = null;
            while (true) {
                try {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTING;

                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }

                if (socket != null) {
                    //manageMyConnectedSocket(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        }
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }
    private class ConnectThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }
        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                Log.d(TAG, "OKkkkkkk");
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            //manageMyConnectedSocket(mmSocket);
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }
    public void startServer(){
        AcceptThread acceptThread = new AcceptThread();
        acceptThread.start();
    }
    public void startClient(BluetoothDevice device) {
        ConnectThread connectThread = new ConnectThread(device);
        connectThread.start();
    }

}
