package com.example.xender.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.xender.R;
import com.example.xender.activity.HistoryActivity;
import com.example.xender.activity.SendActivity;
import com.example.xender.adapter.FileSendAdapter;
import com.example.xender.db.FileSendDatabaseHandler;
import com.example.xender.model.FileSend;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecFragment extends Fragment {
    private static String TAG = "History activity debug";
    private FileSendDatabaseHandler fileSendDatabaseHandler;
    private List<FileSend> fileSendList;
    private List<FileSend> fileReceiveList;
    private FileSendAdapter fileSendAdapter;
    private FileSendAdapter fileReceiveAdapter;
    HistoryActivity historyActivity;
    ListView listView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecFragment newInstance(String param1, String param2) {
        RecFragment fragment = new RecFragment();
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
        return inflater.inflate(R.layout.fragment_rec, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("History activity debug", "ok 1");
        listView = (ListView) getActivity().findViewById(R.id.list_rec_file);
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
        historyActivity.recFragment = this;
    }
}