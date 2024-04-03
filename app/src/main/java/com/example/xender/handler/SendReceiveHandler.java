package com.example.xender.handler;

import android.util.Log;

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
        while(socket !=null && !socket.isClosed()){
            String dirPath= android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            try {
                Log.d("WifiDirect", "write file start");
                long fileLength = dis.readLong();
                Log.d("WifiDirect", "length " +String.valueOf(fileLength));
                String fileName = dis.readUTF();
                Log.d("WifiDirect", "name " +fileName);
                File file = new File(dirPath+"/"+fileName);
                if(file.exists()){
                    file.renameTo(new File(file.getName()+"exist"));
                }
                file.setWritable(true,false);
                Log.d("WifiDirect", "file access"+  String.valueOf(file.setWritable(true,false)));
                Log.d("WifiDirect", "path" +file.getAbsolutePath());
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                for(int j = 0; j < fileLength; j++) {
                    bos.write(bis.read());
                }
                Log.d("WifiDirect", "write file successfully");
               bos.close();

            } catch (IOException e) {
                Log.d("WifiDirect", e.toString());

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
