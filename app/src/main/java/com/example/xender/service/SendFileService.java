package com.example.xender.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.xender.Dialog.MyApplication;
import com.example.xender.activity.SendActivity;
import com.example.xender.db.FileSendDatabaseHandler;
import com.example.xender.handler.SendReceiveHandler;
import com.example.xender.model.FileSend;
import com.example.xender.wifi.MyWifi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;

public class SendFileService extends Service {
    private FileSendDatabaseHandler fileSendDatabaseHandler;
    private String connectionAddress;
    public static File current;

    String TAG="Send File Service";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(current.getAbsolutePath()));
            //    Log.d("WifiDirect", "onItemClick: "+ MyWifi.socket.toString());
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    SendReceiveHandler handler =null;

                    if (MyWifi.socket != null)
                    {
                        handler = new SendReceiveHandler(MyWifi.socket);
                        connectionAddress = "Wifi";
                    }
                    else if (MyWifi.bluetoothSocket != null)
                    {
                        handler = new SendReceiveHandler(MyWifi.bluetoothSocket);
                        connectionAddress = "Bluetooth";
                    }


                    if (handler != null) {
                        try {
                            handler.writeUTF("File");
                            handler.writeLong(current.length());
                            handler.writeUTF(current.getName());
                            handler.write(bytes);
                            fileSendDatabaseHandler = new FileSendDatabaseHandler(MyApplication.getActivity());

                            FileSend fileSend = new FileSend(
                                    0,
                                    current.getName(),
                                    current.getPath(),
                                    connectionAddress,
                                    new Timestamp(new Date().getTime()),
                                    true
                            );
                            fileSendDatabaseHandler.add(fileSend);
                            for (FileSend f: fileSendDatabaseHandler.getAll()
                            ) {
                                Log.d(TAG, f.getId() + f.getFileName());
                            }
                        } catch (IOException e) {
                            Log.d("WifiDirect", "Exception " + e.toString());
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            thread.start();
            Log.d("WifiDirect", "onItemClick: ");
        } catch (Exception e) {
            Log.d("WifiDirect", "onItemClick: "+e.toString());
            throw new RuntimeException(e);
        }
        return super.onStartCommand(intent, flags, startId);
    }


}
