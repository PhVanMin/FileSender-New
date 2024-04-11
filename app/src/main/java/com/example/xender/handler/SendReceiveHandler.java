package com.example.xender.handler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.xender.Dialog.MyApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class SendReceiveHandler extends Thread{

    public static boolean isAccept = false;
    public static boolean isFinish = false;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private static String EOF_STRING = "EOFFFFFFF!";

    public SendReceiveHandler(Socket skt){
        socket = skt;
       try {
           inputStream= skt.getInputStream();
           outputStream= skt.getOutputStream();
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }


    //receive
    @Override
    public void run(){

        byte[] buffer = new byte[1024];
        int bytes;
        ArrayList<Byte> arrayList = new ArrayList<Byte>();
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        DataInputStream dis = new DataInputStream(bis);
        String dirPath= android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        boolean isInDialog = false;
        Thread current = this;
        synchronized (this){
            while (socket != null && !socket.isClosed()) {

                try {
                    Log.d("WifiDirect", "write file start");
                    isFinish =false;
                    long fileLength = dis.readLong();
                    Log.d("WifiDirect", "length " + String.valueOf(fileLength));
                    String fileName = dis.readUTF();
                    Log.d("WifiDirect", "name " + fileName);
                    Handler handler = new Handler(Looper.getMainLooper());

                    handler.post(new Runnable() {
                        @Override
                        public synchronized  void run() {
                            AlertDialog.Builder b = new AlertDialog.Builder(MyApplication.getActivity());
                            b.setTitle("Xác nhận");
                            b.setMessage("Are you accept " + fileName + "\n Size: " + fileLength);
                            b.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SendReceiveHandler.isAccept=true;
                                    SendReceiveHandler.isFinish=true;
                                }
                            });
                            b.setCancelable(true);
                            b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SendReceiveHandler.isAccept=false;
                                   // current.notifyAll();
                                    SendReceiveHandler.isFinish=true;
                                    dialog.cancel();

                                }
                            });
                            AlertDialog al = b.create();
                            al.show();

                        }
                    });

                    while(!isFinish);

                    if(isFinish){
                        if(isAccept) {
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
                        } else {
                            dis.skipBytes(bis.available());
                        }
                    }

                } catch (IOException e) {
                    Log.d("WifiDirect", e.toString());

                }

            }
        }

        Log.d("WifiDirect", "SendReceive stop");


    }


    public void write(byte[] bytes){
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void writeLong(long number){
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        try {
            dataOutputStream.writeLong(number);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void writeUTF(String str) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeUTF(str);
    }
}
