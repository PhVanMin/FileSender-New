package com.example.xender.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.xender.R;
import com.example.xender.activity.CloudActivity;
import com.example.xender.activity.HistoryActivity;

public class SentFragment extends Fragment {
    HistoryActivity historyActivity;
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
        Log.d("History Activity", "ok");
        historyActivity= (HistoryActivity) getActivity();
        historyActivity.switchToSendFragment();
    }
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
