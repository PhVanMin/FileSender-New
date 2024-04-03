package com.example.xender.handler;

import android.util.Log;
import android.widget.Toast;

import com.example.xender.wifi.MyWifi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client extends Thread{
    Socket socket;
    InetAddress inetAddress;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }



    private SendReceiveHandler handler;

    public SendReceiveHandler getHandler(){
        return handler;
    }
    public Client(InetAddress inetAddress){

        this.inetAddress = inetAddress;
        socket = new Socket();
    }


    @Override
    public void run(){
        try {
                socket.connect(new InetSocketAddress(inetAddress,8888));
                MyWifi.socket= socket;
                Log.d("WifiDirect","connect socket successfully");
                handler = new SendReceiveHandler(socket);
                handler.start();
        }
        catch(Exception e){
            Log.d("WifiDirect",e.toString());
        }
    }

    public void cleanup(){
        Log.d("WifiDirect", "cleanup: ");
        if(socket!=null){
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
