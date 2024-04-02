package com.example.xender.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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
    //private SendRecevie sendRecevie;
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
        context = this;
        findViewByIdea();
    }

    private void showListBluetooth() {
        btnListDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
               /* ServerSocket serverSocket = new ServerSocket();
                serverSocket.start();*/
            }
        });
        lstvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*ConnectThread clientClass = new ConnectThread(devices[position]);
                clientClass.start();
                status.setText("Connecting");*/
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = String.valueOf(messageBox.getText());
                //sendRecevie.wirte(s.getBytes());
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bluetooth_qr, container, false);
    }
}