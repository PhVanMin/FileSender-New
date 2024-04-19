package com.example.xender.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.xender.Dialog.MyApplication;
import com.example.xender.R;
import com.example.xender.db.FileSendDatabaseHandler;
import com.example.xender.fragment.ReceiveFragment;
import com.example.xender.fragment.SentFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HistoryActivity extends AppCompatActivity {
    public SentFragment sentFragment;
    public ReceiveFragment receiveFragment;
    private FileSendDatabaseHandler fileSendDatabaseHandler;
    private static String TAG = "History activity debug";
    NavController navController;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileSendDatabaseHandler = new FileSendDatabaseHandler(this);
        Log.d(TAG, "Load file: ");
        setContentView(R.layout.activity_history);
        toolbar = findViewById(R.id.appbar_send);
        toolbar.setTitle("History");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView navigationView = findViewById(R.id.navigation_view);
        NavigationUI.setupWithNavController(navigationView, navController);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.setActivity(this);
    }
}

