package com.example.xender.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.xender.R;
import com.example.xender.adapter.FileCloudAdapter;
import com.example.xender.handler.DatabaseHandler;
import com.example.xender.model.FileCloud;

import java.util.List;

public class CloudActivity extends AppCompatActivity {
    private static String TAG = "Cloud activity";
    private Toolbar toolbar;
    private List<FileCloud> fileCloudList;
    private DatabaseHandler databaseHandler;
    private FileCloudAdapter fileCloudAdapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);
        toolbar = findViewById(R.id.appbar_send);
        toolbar.setTitle("Cloud");
        toolbar.isBackInvokedCallbackEnabled();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        databaseHandler = new DatabaseHandler(this);
        listView = findViewById(R.id.list_cloud_file);
        initData();
        fileCloudAdapter = new FileCloudAdapter(this,R.layout.file,fileCloudList);
        listView.setAdapter(fileCloudAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileCloud current = fileCloudList.get(position);
                Intent qrCloud = new Intent();
                qrCloud.setClass(CloudActivity.this,QRCloudActivity.class);
                qrCloud.putExtra("QRCODE",current.getUri());
                startActivity(qrCloud);
               // fileCloudQrFragment.generateQRCode(current.getUri());
            }
        });
    }

    private void initData(){
        fileCloudList = databaseHandler.getAllFileClouds();
        fileCloudList.forEach(fileCloud -> {
            Log.d(TAG, "initData: "  + fileCloud.getName());
        });

    }

}