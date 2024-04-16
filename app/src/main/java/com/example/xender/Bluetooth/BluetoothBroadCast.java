package com.example.xender.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BluetoothBroadCast extends BroadcastReceiver {
    private List<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    public BluetoothBroadCast(){
        Log.d("BluetoothBroadCast", "F1: ");
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BluetoothBroadCast", "Found device: ");
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            bluetoothDevices.add(device);
            Log.d("BluetoothBroadCast", "Found device: " + device.getName() + " - " + device.getAddress());
        }
    }

    public List<BluetoothDevice> getBluetoothDevices() {
        return bluetoothDevices;
    }
}
