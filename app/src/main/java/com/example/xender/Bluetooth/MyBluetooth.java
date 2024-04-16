package com.example.xender.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.xender.handler.SendReceiveHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class MyBluetooth {
    private static final String TAG = "MyBluetooth";
    private static final String NAME = "MYAPP";
    private static final UUID MY_UUID = UUID.fromString("e7203025-4e62-4f0c-8f3b-87ae58178bb7");

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothServerSocket serverSocket;

    public MyBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public String getAddress() {
        return bluetoothAdapter.getName();
    }

    public void startServer() {
        AcceptThread acceptThread = new AcceptThread();
        acceptThread.start();
    }

    private class AcceptThread extends Thread {
        private AcceptThread() {
            try {
                serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
        }

        @Override
        public void run() {
            BluetoothSocket socket = null;
            while (true) {
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }
                if (socket != null) {
                    try {
                        Log.d(TAG, "Socket's accept() Success");
                        /*SendReceiveHandler sendReceiveHandler = new SendReceiveHandler(new Socket());
                        sendReceiveHandler.start();*/
                        socket.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Could not close the connected socket", e);
                    }
                }
            }
        }

        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the server socket", e);
            }
        }
    }
}
