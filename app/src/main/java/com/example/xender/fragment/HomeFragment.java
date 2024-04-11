package com.example.xender.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.xender.activity.MainActivity;
import com.example.xender.activity.QRActivity;
import com.example.xender.activity.ChooseActivity;
import com.example.xender.permission.PermissionChecker;
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
    private TextView totalFilesTextView;
    private ImageButton sendBtn ;
    private ImageButton qrBtn;
    private ImageButton connectBtn;
    private ImageButton cloudBtn;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressBar progressBar;
    MainActivity activity;

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
        activity = (MainActivity) getActivity();
        sendBtn = getActivity().findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(v -> gotoSendActivity());

        qrBtn = getActivity().findViewById(R.id.qr_btn);
        qrBtn.setOnClickListener(v -> gotoQRActivity());

        connectBtn = getActivity().findViewById(R.id.connect_btn);
        connectBtn.setOnClickListener(v -> gotoConnectctivity());

        cloudBtn = getActivity().findViewById(R.id.cloud_btn);
        cloudBtn.setOnClickListener(v -> gotoCloudActivity());
        if (PermissionChecker.checkReadExternalStorage(getActivity()) ){
            setProgressbar();
        }

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.homeFragment = this;

    }

    public void setProgressbar(){

        long files = Environment.getExternalStorageDirectory().listFiles().length;
        long gbMemoryAvailable = StorageUtil.getByteAvailable() / (1073741824);
        long gbMemorySize = StorageUtil.getByteMemorySize() / (1073741824);

        storageInfoTextView = activity.findViewById(R.id.storageInfoTextView);
        totalFilesTextView = activity.findViewById(R.id.totalFiles);
        totalFilesTextView.setText(Long.toString(files) + " files");
        storageInfoTextView.setText(
                String.valueOf(gbMemorySize - gbMemoryAvailable) + " GB of " +
                        String.valueOf(gbMemorySize) + " GB");

        progressBar = getActivity().findViewById(R.id.homeProgressBar);
        progressBar.setMax((int) gbMemorySize);
        progressBar.setProgress((int) ((gbMemorySize - gbMemoryAvailable)));
    }
}