package com.example.xender.handler;

import android.util.Log;

import com.example.xender.wifi.MyWifi;

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
    private SendReceiveHandler handler;

    public SendReceiveHandler getHandler(){
        return handler;
    }
    @Override
    public void run(){
        try {

                Log.d("WifiDirect", "Sever start");
                serverSocket = new ServerSocket(8888);
                while(true) {
                    socket = serverSocket.accept();
                    Log.d("WifiDirect", "Accepted client: " + socket.getLocalAddress().toString());
                    MyWifi.socket = socket;
                    handler = new SendReceiveHandler(socket);
                    handler.start();
                }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
