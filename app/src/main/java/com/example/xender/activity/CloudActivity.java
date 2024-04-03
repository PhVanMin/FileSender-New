package com.example.xender.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.xender.R;
import com.example.xender.handler.DatabaseHandler;
import com.example.xender.model.FileCloud;

import java.util.List;

public class CloudActivity extends AppCompatActivity {
    private static String TAG = "Cloud activity";
    private Toolbar toolbar;
    private List<FileCloud> fileCloudList;
    private DatabaseHandler databaseHandler;
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

        initData();

    }
    private void initData(){
        fileCloudList = databaseHandler.getAllFileClouds();
        fileCloudList.forEach(fileCloud -> {
            Log.d(TAG, "initData: "  + fileCloud.getName());
        });
    }
}