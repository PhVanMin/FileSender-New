package com.example.xender.model;

import java.sql.Timestamp;

public class ConnectedBluetoothDevice {
    private String name;
    private String address;
    private Timestamp time;

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public ConnectedBluetoothDevice(String name, String address, Timestamp time) {
        this.name = name;
        this.address = address;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
