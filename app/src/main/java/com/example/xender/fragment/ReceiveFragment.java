package com.example.xender.fragment;

import static com.example.xender.Dialog.MyApplication.getActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.xender.R;
import com.example.xender.activity.HistoryActivity;
import com.example.xender.activity.SendActivity;
import com.example.xender.adapter.FileSendAdapter;
import com.example.xender.db.FileSendDatabaseHandler;
import com.example.xender.model.FileSend;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReceiveFragment extends Fragment {
    private static String TAG = "History activity debug";
    private FileSendDatabaseHandler fileSendDatabaseHandler;
    private List<FileSend> fileSendList;
    private List<FileSend> fileReceiveList;
    private FileSendAdapter fileSendAdapter;
    private FileSendAdapter fileReceiveAdapter;
    HistoryActivity historyActivity;
    ListView listView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("History activity debug", "ok 1");
        listView = (ListView) getActivity().findViewById(R.id.list_cloud_file);
        switchToReceiveFragment();
    }
    public void switchToReceiveFragment() {
        fileSendDatabaseHandler = new FileSendDatabaseHandler(getActivity());
        initData();
        Log.d("History activity debug", "Load Receive fragment: " + fileReceiveList.size());
        fileReceiveAdapter = new FileSendAdapter(getActivity(), R.layout.file, fileReceiveList);
        listView.setAdapter(fileReceiveAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getActivity(), SendActivity.class);
            FileSend fileSend = fileReceiveAdapter.getItem(position);
            File current = new File(fileSend.getFilePath());
            intent.putExtra("File", current.getAbsolutePath());
            startActivity(intent);
        });
    }
    private void initData(){
        Log.d(TAG, "Load data: ");
        fileSendList = new ArrayList<>();
        fileReceiveList = new ArrayList<>();
        for (FileSend file: fileSendDatabaseHandler.getAll()) {
            if(file.getIsSend() == true)
            {
                Log.d(TAG, "File send: " + file.getFileName());
                fileSendList.add(file);

            }
            else {
                Log.d(TAG, "File receive: " + file.getFileName());
                fileReceiveList.add(file);

            }
        }
    }
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        historyActivity= (HistoryActivity) getActivity();
        historyActivity.receiveFragment = this;
    }
}
