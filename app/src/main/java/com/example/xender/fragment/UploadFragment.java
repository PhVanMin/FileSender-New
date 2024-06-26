package com.example.xender.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.xender.R;
import com.example.xender.activity.CloudActivity;

public class UploadFragment extends Fragment {
    CloudActivity cloudActivity;
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
        cloudActivity= (CloudActivity) getActivity();
        cloudActivity.setViewUploadFragment();
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
