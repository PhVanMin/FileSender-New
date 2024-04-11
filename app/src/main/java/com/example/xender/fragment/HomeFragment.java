package com.example.xender.fragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.xender.R;
import com.example.xender.activity.CloudActivity;
import com.example.xender.activity.ConnectActivity;
import com.example.xender.activity.HistoryActivity;
import com.example.xender.activity.QRActivity;
import com.example.xender.activity.ChooseActivity;
import com.example.xender.activity.SendActivity;
import com.example.xender.utils.StorageUtil;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView storageInfoTextView;
    private TextView totalFilesTextView;
    private ImageButton sendBtn ;
    private ImageButton qrBtn;
    private ImageButton connectBtn;
    private ImageButton cloudBtn;
    private ImageButton historyBtn;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressBar progressBar;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sendBtn = getActivity().findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(v -> goToActivity(ChooseActivity.class));

        qrBtn = getActivity().findViewById(R.id.qr_btn);
        qrBtn.setOnClickListener(v -> goToActivity(QRActivity.class));

        connectBtn = getActivity().findViewById(R.id.connect_btn);
        connectBtn.setOnClickListener(v -> goToActivity(ConnectActivity.class));

        cloudBtn = getActivity().findViewById(R.id.cloud_btn);
        cloudBtn.setOnClickListener(v -> goToActivity(CloudActivity.class));

        historyBtn = getActivity().findViewById(R.id.history_btn);
        historyBtn.setOnClickListener(v -> goToActivity(HistoryActivity.class));

        storageInfoTextView = getActivity().findViewById(R.id.storageInfoTextView);
        totalFilesTextView = getActivity().findViewById(R.id.totalFiles);

        long files = Environment.getExternalStorageDirectory().listFiles().length;
        long gbMemoryAvailable = StorageUtil.getByteAvailable() / (1073741824);
        long gbMemorySize = StorageUtil.getByteMemorySize() / (1073741824);

        totalFilesTextView.setText(String.format(Locale.ENGLISH,"%d files", files));
        storageInfoTextView.setText(
                String.format(Locale.ENGLISH, "%s GB of %s GB", gbMemorySize - gbMemoryAvailable, gbMemorySize));

        progressBar = getActivity().findViewById(R.id.homeProgressBar);
        progressBar.setMax((int) gbMemorySize);
        progressBar.setProgress((int) ((gbMemorySize - gbMemoryAvailable)));
    }

    public void goToActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }
}