package com.example.xender.handler;

import android.app.AlertDialog;
import android.bluetooth.BluetoothSocket;
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
    private BluetoothSocket bluetoothSocket;
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

    public SendReceiveHandler(BluetoothSocket skt){
            bluetoothSocket=skt;
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
        Log.d("WifiDirect", "write file start");

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            AlertDialog.Builder b = new AlertDialog.Builder(MyApplication.getActivity());
            b.setTitle("Kết nối");
            b.setMessage("Thành công!");
            AlertDialog al = b.create();
            al.show();
        });


        try {

            Receiver  receiver = new Receiver(inputStream,outputStream);

            while( (socket!= null && !socket.isClosed()) || (bluetoothSocket!=null && bluetoothSocket.isConnected())){
                receiver.ShowCheckDialog();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


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
