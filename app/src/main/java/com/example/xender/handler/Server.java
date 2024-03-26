package com.example.xender.handler;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{

    public static Server server;
    public static Server getServer(){
        if (server == null)
            server = new Server();

        return server;
    };

    private Socket socket;
    private ServerSocket serverSocket;

    public Socket getSocket() {
        return socket;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    public void run(){
        try {
            Log.d("WifiDirect","Sever start");
            serverSocket = new ServerSocket(8888);
            socket = serverSocket.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
