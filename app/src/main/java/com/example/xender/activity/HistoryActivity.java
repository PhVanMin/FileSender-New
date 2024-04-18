package com.example.xender.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.xender.R;
import com.example.xender.adapter.FileAdapter;
import com.example.xender.db.FileSendDatabaseHandler;
import com.example.xender.model.FileCloud;
import com.example.xender.model.FileSend;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private static String TAG = "History activity debug";
    private ArrayList<File> fileSendList;
    private FileSendDatabaseHandler fileSendDatabaseHandler;
    private FileAdapter fileAdapter;
    NavController navController;
    Toolbar toolbar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Load file: ");
        setContentView(R.layout.activity_history);

        // Find ListView by ID
        listView = findViewById(R.id.link_list);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        toolbar = findViewById(R.id.appbar_send);
        toolbar.setTitle("History");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> finish());

        BottomNavigationView navigationView = findViewById(R.id.navigation_view);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public void switchToSendFragment() {
        Log.d(TAG, "Load fragment: ");
        navController.navigate(R.id.sentFragment);
        fileSendList = new ArrayList<>();
        fileSendDatabaseHandler = new FileSendDatabaseHandler(this);
        // Remove listView initialization from here
        initData();
        fileAdapter =  new FileAdapter(this,  R.layout.file, fileSendList);
        Log.d(TAG, "Load Adapter: ");
        listView.setAdapter(fileAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            File current = fileSendList.get(position);
            Intent intent = new Intent(HistoryActivity.this,SendActivity.class);
            intent.putExtra("File",current.getAbsolutePath());
            startActivity(intent);
        });

    }

    private void initData(){
        Log.d(TAG, "Load data: ");
        for (FileSend file: fileSendDatabaseHandler.getAll()) {
            if(file.getIsSend() == true)
            {
                File f = new File(file.getFilePath());
                fileSendList.add(f);
            }
        }
        for (File f : fileSendList) {
            Log.d(TAG, "File: " + f.getName());
        }
    }

    public void switchToReceiveFragment() {
        navController.navigate(R.id.receiveFragment);
    }
}

