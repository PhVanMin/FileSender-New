package com.example.xender.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

import com.example.xender.Dialog.MyApplication;
import com.example.xender.wifi.MyWifi;

public class ReceiveFileService extends Service {


    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                     MyWifi.receiver.Receive();
                    stopForeground(true);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();




        final String CHANNEL = "Foreground task";
        NotificationChannel notificationChannel= new NotificationChannel(CHANNEL,CHANNEL,
                NotificationManager.IMPORTANCE_LOW);
        getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);

        Notification.Builder notification = new Notification.Builder(this,CHANNEL)
                .setContentText("Foreground service is running")
                .setContentTitle("SendFile task");

        startForeground(1001,notification.build());

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
