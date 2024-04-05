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
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BluetoothBroadcastReceiver extends Thread {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice serverDevice;
    private BluetoothSocket socket;
    public static String TAG = "BluetoothConnectionSevice";
    private Context context;
    public BluetoothBroadcastReceiver(Context context, BluetoothManager bluetoothManager,String address) {
        bluetoothAdapter = bluetoothManager.getAdapter();
        this.context = context;
        ConnectThread connectThread = new ConnectThread(findServerDevice(address));
        connectThread.start();
    }

    private BluetoothDevice findServerDevice(String address) {
        BluetoothDevice serverDevice = null;
        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {

            if(device.getName().equals(address))
            {
                serverDevice = device;
                Log.d(TAG, device.getName());
                break;
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
                tmp = device.createRfcommSocketToServiceRecord(MyBluetooth.MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed",   e);
            }
            mmSocket = tmp;
        }
        public void run() {
            try {
                mmSocket.connect();
                Log.d(TAG, "Connected successfully!");
                showToast("Connected successfully!");
            } catch (IOException connectException) {
                connectException.printStackTrace();


            }
        }
        private void showToast(String message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
