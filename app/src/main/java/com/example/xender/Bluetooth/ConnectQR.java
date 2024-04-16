package com.example.xender.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class ConnectQR {

    private static final String TAG = "MyBluetooth";
    private static final String NAME = "MYAPP";
    private static final UUID MY_UUID = UUID.fromString("e7203025-4e62-4f0c-8f3b-87ae58178bb7");

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice device;

    public ConnectQR(Context context, String address) {
        Log.d(TAG, "SCAN " + address);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        device = findPairedDevice(address);

        if (device == null) {
            new FindServerDeviceTask(context, address).execute();
        } else {
            connectToDevice(device);
        }
    }

    private BluetoothDevice findPairedDevice(String name) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice pairedDevice : pairedDevices) {
            if (pairedDevice.getName().equals(name)) {
                return pairedDevice;
            }
        }
        return null;
    }

    private void connectToDevice(BluetoothDevice device) {
        new ConnectThread(device).start();
    }

    private class FindServerDeviceTask extends AsyncTask<Void, Void, BluetoothDevice> {

        private Context context;
        private String address;

        public FindServerDeviceTask(Context context, String address) {
            this.context = context;
            this.address = address;
        }

        @Override
        protected BluetoothDevice doInBackground(Void... voids) {
            // Perform Bluetooth device discovery and return the server device
            return findServerDevice(address, context);
        }

        @Override
        protected void onPostExecute(BluetoothDevice result) {
            if (result != null) {
                Log.d(TAG, "Found server device: " + result.getName() + " - " + result.getAddress());
                connectToDevice(result);
            } else {
                Log.e(TAG, "Server device not found");
            }
        }
    }

    private BluetoothDevice findServerDevice(String name, Context context) {
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Bluetooth is not supported on this device");
            return null;
        }
        Log.e(TAG, "Broadcast start");
        BluetoothBroadCast bluetoothBroadCast = new BluetoothBroadCast();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

        context.registerReceiver(bluetoothBroadCast, filter);
        bluetoothAdapter.startDiscovery();

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        context.unregisterReceiver(bluetoothBroadCast);

        List<BluetoothDevice> bluetoothDevices = bluetoothBroadCast.getBluetoothDevices();
        for (BluetoothDevice device : bluetoothDevices) {
            Log.d(TAG, "Found device: " + device.getName() + " - " + device.getAddress());
            if(device.getName() != null)
            {
                if (device.getName().equals(name)) {
                    Log.d(TAG, "Found target device: " + device.getName() + " - " + device.getAddress());
                    return device;
                }
            }

        }
        return null;
    }

    private class ConnectThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice d) {
            Log.d(TAG, "Connect thread " + d.getName());
            BluetoothSocket tmp = null;
            mmDevice = d;
            try {
                tmp = d.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            try {
                mmSocket.connect();
                Log.d(TAG, "Connected successfully!");
            } catch (IOException connectException) {
                connectException.printStackTrace();
            }
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

