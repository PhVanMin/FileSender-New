package com.example.xender.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.xender.R;
import com.example.xender.activity.CloudActivity;
import com.example.xender.activity.ConnectActivity;
import com.example.xender.activity.QRActivity;
import com.example.xender.activity.ChooseActivity;
import com.example.xender.utils.StorageUtil;



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
    private Button sendBtn ;
    private Button qrBtn;
    private Button connectBtn;
    private Button cloudBtn;
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
        sendBtn.setOnClickListener(v -> gotoSendActivity());

        qrBtn = getActivity().findViewById(R.id.qr_btn);
        qrBtn.setOnClickListener(v -> gotoQRActivity());

        connectBtn = getActivity().findViewById(R.id.connect_btn);
        connectBtn.setOnClickListener(v -> gotoConnectctivity());

        cloudBtn = getActivity().findViewById(R.id.cloud_btn);
        cloudBtn.setOnClickListener(v -> gotoCloudActivity());

        storageInfoTextView = getActivity().findViewById(R.id.storageInfoTextView);

        long gbMemoryAvailable = StorageUtil.getByteAvailable() / (1073741824);
        long gbMemorySize = StorageUtil.getByteMemorySize() / (1073741824);

        storageInfoTextView.setText(
                String.valueOf(gbMemorySize - gbMemoryAvailable) + " GB of " +
                        String.valueOf(gbMemorySize) + " GB");

        progressBar = getActivity().findViewById(R.id.progressBar);
        progressBar.setMax((int) gbMemorySize);
        progressBar.setProgress((int) ((gbMemorySize - gbMemoryAvailable)));
    }
    public void gotoSendActivity(){
        Intent intent = new Intent(getActivity(), ChooseActivity.class);
        startActivity(intent);
    }
    public void gotoQRActivity(){
        Log.d("QRActivity", "gotoQRActivity: ");
        Intent intent = new Intent(getActivity(), QRActivity.class);
        startActivity(intent);
    }
    public void gotoConnectctivity(){
        Intent intent = new Intent(getActivity(), ConnectActivity.class);
        startActivity(intent);
    }
    public void gotoCloudActivity(){
        Intent intent = new Intent(getActivity(), CloudActivity.class);
        startActivity(intent);
    }
}