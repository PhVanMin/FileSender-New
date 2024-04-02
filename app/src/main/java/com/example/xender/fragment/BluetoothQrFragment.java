package com.example.xender.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xender.R;
import com.example.xender.activity.QRActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.util.Set;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BluetoothQrFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BluetoothQrFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ListView lstvw;
    private ArrayAdapter aAdapter;

    private BluetoothQrFragment context;

    private Button btnListen, btnListDevices, btnSend;

    private TextView status, messageShow;
    private EditText messageBox;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice[] devices;
    private SendRecevie sendRecevie;
    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;
    int REQUEST_ENABLE_BLUETOOTH = 1;

    private static final String TAG = "BluetoothConnectionSevice";
    private static final String NAME = "MYAPP";

    private final UUID MY_UUID =
            UUID.fromString("e7203025-4e62-4f0c-8f3b-87ae58178bb7");

    public BluetoothQrFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BluetoothQRFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BluetoothQrFragment newInstance(String param1, String param2) {
        BluetoothQrFragment fragment = new BluetoothQrFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = this;
        findViewByIdea();
        showListBluetooth();
        BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
            }
        }
    }

    private void showListBluetooth() {
        btnListDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                String[] strings = new String[pairedDevices.size()];
                devices = new BluetoothDevice[pairedDevices.size()];
                int index = 0;
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        devices[index] = device;
                        strings[index] = device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, strings);
                    lstvw.setAdapter(arrayAdapter);
                }

            }
        });
        btnListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BluetoothQrFragment.ServerSocket serverSocket = new BluetoothQrFragment.ServerSocket();
                serverSocket.start();
            }
        });
        lstvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConnectThread clientClass = new ConnectThread(devices[position]);
                clientClass.start();
                status.setText("Connecting");
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = String.valueOf(messageBox.getText());
                if (sendRecevie != null) {
                    sendRecevie.wirte(s.getBytes());
                } else {
                    // Xử lý khi sendRecevie là null, ví dụ:
                    Log.e(TAG, "sendRecevie is null");
                }
            }
        });
    }
    private void findViewByIdea(){
        lstvw = (ListView) getActivity().findViewById(R.id.listBluetooth);
        btnSend = (Button) getActivity().findViewById(R.id.send);
        btnListen = (Button) getActivity().findViewById(R.id.listen);
        btnListDevices = (Button) getActivity().findViewById(R.id.listDevices);
        messageBox = getActivity().findViewById(R.id.messageBox);
        //messageShow = getActivity().(R.id.showMessage);
        status = getActivity().findViewById(R.id.status);
    }



    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case STATE_LISTENING:
                    status.setText("Listening...");
                    break;
                case STATE_CONNECTING:
                    status.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection Failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff = (byte[]) msg.obj;
                    String tempMsg = new String(readBuff, 0, msg.arg1);
                    messageShow.setText(tempMsg);
                    break;

            }
            return true;
        }
    });


    private  class ServerSocket extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public ServerSocket() {
            BluetoothServerSocket tmp = null;
            try {

                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }
        public void run(){
            BluetoothSocket socket = null;
            while (true) {
                try {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTING;
                    handler.sendMessage(message);
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                    break;
                }

                if (socket != null) {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTED;
                    handler.sendMessage(message);
                    sendRecevie = new SendRecevie(socket);
                    sendRecevie.start();
                    break;
                }
            }
        }
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }


    private class SendRecevie extends Thread{
        private final BluetoothSocket bluetoothSocket;
        private InputStream inputStream = null;
        private OutputStream outputStream = null;

        public SendRecevie(BluetoothSocket socket) {
            this.bluetoothSocket = socket;
            InputStream tempIn = null;
            OutputStream tempOut =null;
            try {
                tempIn=bluetoothSocket.getInputStream();
                tempOut=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            inputStream=tempIn;
            outputStream=tempOut;
        }
        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;
            while(true){
                try {
                    bytes = inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public void wirte(byte[] bytes){
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private class ConnectThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }
        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handler.sendMessage(message);
                sendRecevie = new SendRecevie(mmSocket);
                sendRecevie.start();
            } catch (IOException connectException) {
                connectException.printStackTrace();
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(message);

            }

            //manageMyConnectedSocket(mmSocket);
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bluetooth_qr, container, false);
    }
}