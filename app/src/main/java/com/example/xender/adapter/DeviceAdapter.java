package com.example.xender.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.xender.R;
import com.example.xender.model.ConnectedBluetoothDevice;

import java.util.List;
import java.util.Random;

public class DeviceAdapter extends ArrayAdapter<ConnectedBluetoothDevice> {
    List<ConnectedBluetoothDevice> devices;

    public DeviceAdapter(@NonNull Context context, int resource, @NonNull List<ConnectedBluetoothDevice> objects) {
        super(context, resource, objects);
        devices= objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View row ;

        row = inflater.inflate(R.layout.device, null);

        TextView name = (TextView) row.findViewById(R.id.deviceName_textview);
        TextView address = (TextView) row.findViewById(R.id.deviceAddress_textView);
        TextView date = (TextView) row.findViewById(R.id.deviceDate_textview);


        name.setText(devices.get(position).getName());
        address.setText(devices.get(position).getAddress());
        date.setText(devices.get(position).getTime().toString());

        return row;
    }
}
