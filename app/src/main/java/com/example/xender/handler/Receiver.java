package com.example.xender.handler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.xender.Dialog.MyApplication;
import com.example.xender.db.FileSendDatabaseHandler;
import com.example.xender.model.FileSend;
import com.example.xender.service.SendFileService;
import com.example.xender.wifi.MyWifi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Receiver implements Serializable {
    private Intent intent;
    private DataInputStream dis;
    private BufferedInputStream bis;
    private InputStream inputStream;
    private OutputStream outputStream;

    boolean isDialogShow = false;
    private long fileLength;
    private String fileName;
    private FileSendDatabaseHandler fileSendDatabaseHandler;
    public Receiver(InputStream _inputStream, OutputStream _outputStream) throws IOException {
        inputStream= _inputStream;
        outputStream= _outputStream;
       bis = new BufferedInputStream(inputStream);
       dis = new DataInputStream(bis);

    }
    public  synchronized void ShowCheckDialog() throws IOException {

        synchronized (this) {
            while(isDialogShow)
            {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            isDialogShow = true;


             fileLength = dis.readLong();
            Log.d("WifiDirect", "length " + String.valueOf(fileLength));
            fileName = dis.readUTF();
            Log.d("WifiDirect", "name " + fileName);
            intent = new Intent(MyApplication.getActivity(), SendFileService.class);
            MyWifi.receiver =this;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder b = new AlertDialog.Builder(MyApplication.getActivity());
                    b.setTitle("Xác nhận");
                    b.setMessage("Are you accept " + fileName + "\n Size: " + fileLength);

                    b.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            MyApplication.getActivity().startForegroundService(intent);


                        }
                    });
                    b.setCancelable(true);
                    b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Skip();
                                        dialog.cancel();
                                    }
                                }).start();

                        }
                    });
                    AlertDialog al = b.create();
                    al.show();

                }
            });

        }

    }


    public synchronized void Skip(){
        try {
            dis.skipBytes(bis.available());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        isDialogShow = false;
        notify();

    }
    public synchronized void Receive() throws InterruptedException {

        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(dirPath + "/" + fileName);

        if (file.exists()) {
            file.renameTo(new File(dirPath + "/copy_of_" + fileName));
        }
        file.setWritable(true, false);
        Log.d("WifiDirect", "file access" + String.valueOf(file.setWritable(true, false)));
        Log.d("WifiDirect", "path= " + file.getAbsolutePath());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            Log.d("WifiDirect", "path= " + file.getAbsolutePath());
            int s ;
            Log.d("WifiDirect bis", String.valueOf(bis.available()));
            for (int i = 0 ; i<fileLength;i++){
                bos.write(dis.readByte());
            }
            Log.d("WifiDirect", "write file successfully");
            bos.close();
        } catch (IOException e) {
            Log.d("WifiDirect", e.toString());
            throw new RuntimeException(e);
        }
        isDialogShow = false;
        FileSend fileSend = new FileSend(
                0,
                file.getName(),
                file.getPath(),
                "",
                new Timestamp(new Date().getTime()),
                false
        );
        fileSendDatabaseHandler = new FileSendDatabaseHandler(MyApplication.getActivity());
        Log.d("FileSendDatabaseHandler", fileSend.getFilePath() + fileSend.getFileName());
        fileSendDatabaseHandler.add(fileSend);
        fileSendDatabaseHandler.getAll();
        for (FileSend f: fileSendDatabaseHandler.getAll()
             ) {
            Log.d("FileSendDatabaseHandler", f.getId() + f.getFileName());
        }
        Thread.sleep(5000);
        notify();
    }


}
