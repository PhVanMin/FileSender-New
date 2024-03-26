package com.example.xender.handler;

import android.util.Log;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client extends Thread{
    Socket socket;
    InetAddress inetAddress;




    public Client(InetAddress inetAddress){

        this.inetAddress = inetAddress;
        socket = new Socket();
    }


    @Override
    public void run(){

        try {


                socket.connect(new InetSocketAddress(inetAddress,8888));
                Log.d("WifiDirect","connect socket successfully");



        }
        catch(Exception e){
            Log.d("WifiDirect",e.toString());
        }
    }
}
