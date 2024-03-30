package com.example.xender.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.xender.R;
import com.example.xender.activity.SendActivity;
import com.example.xender.adapter.ContactAdapter;
import com.example.xender.adapter.FileAdapter;
import com.example.xender.handler.SendReceiveHandler;
import com.example.xender.utils.StorageUtil;
import com.example.xender.wifi.MyWifi;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listView;
    FileAdapter fileAdapter;
    public FileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FolderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FileFragment newInstance(String param1, String param2) {
        FileFragment fragment = new FileFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_folder, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle saveInstanceState) {
        Log.d("ActivitySend","FileFragment ");
        loadFiles();
        super.onActivityCreated(saveInstanceState);


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        SendActivity sendActivity = (SendActivity) getActivity();
        sendActivity.fileFragment= this;
    }

    public void loadFiles(){
        listView = getActivity().findViewById(R.id.list_files);
        SendActivity parent = (SendActivity) getActivity();
        if (StorageUtil.files.size() == 0) {
                StorageUtil.getAllDir(Environment.getExternalStorageDirectory(), StorageUtil.FILTER_BY_DOCUMENT);
        }
        Log.d("File test",String.valueOf(StorageUtil.files.size()));
        fileAdapter = new FileAdapter(getActivity(),R.layout.file,StorageUtil.files);

        listView.setAdapter(fileAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File current = fileAdapter.files.get(position);
                try {
                    byte[] bytes = Files.readAllBytes(Paths.get(current.getAbsolutePath()));
                    Log.d("WifiDirect", "onItemClick: "+ MyWifi.socket.toString());

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SendReceiveHandler handler= new SendReceiveHandler(MyWifi.socket);
                            try {
                                handler.writeLong(current.length());
                                handler.writeUTF(current.getName());
                                handler.write(bytes);
                            } catch (IOException e) {
                                Log.d("WifiDirect", "Exception "+ e.toString());
                                throw new RuntimeException(e);
                            }



                        }
                    });
                    thread.start();
                    Log.d("WifiDirect", "onItemClick: ");

//

                } catch (Exception e) {
                    Log.d("WifiDirect", "onItemClick: "+e.toString());
                    throw new RuntimeException(e);

                }

            }
        });
    }
}