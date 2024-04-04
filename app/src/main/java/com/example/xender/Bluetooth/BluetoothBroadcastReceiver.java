package com.example.xender.Bluetooth;

import static android.content.ContentValues.TAG;
import static android.net.VpnProfileState.STATE_CONNECTING;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.UUID;

public class BluetoothBroadcastReceiver extends Thread {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice serverDevice;
    private BluetoothSocket socket;
    private UUID uuid;

    public BluetoothBroadcastReceiver(String uuid) {
        this.uuid = UUID.fromString(uuid);
        ConnectThread connectThread = new ConnectThread(findServerDevice(UUID.fromString(uuid)));
        connectThread.start();
    }

    private BluetoothDevice findServerDevice(UUID uuid) {
        BluetoothDevice serverDevice = null;
        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {

            for (ParcelUuid deviceUUID : device.getUuids()) {
                if (deviceUUID.equals(uuid)) {
                    serverDevice = device;
                    break;
                }
            }
            if (serverDevice != null) {
                break;
            }
        }
        return serverDevice;
    }
    private class ConnectThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed",   e);
            }
            mmSocket = tmp;
        }
        public void run() {
            bluetoothAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                connectException.printStackTrace();


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
}
