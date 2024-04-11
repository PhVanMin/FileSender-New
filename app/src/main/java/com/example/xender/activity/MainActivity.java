package com.example.xender.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.bluetooth.BluetoothCsipSetCoordinator;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.xender.Dialog.MyApplication;
import com.example.xender.R;
import com.example.xender.fragment.HomeFragment;
import com.example.xender.handler.Client;


import com.example.xender.permission.PermissionChecker;
import com.example.xender.utils.StorageUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        toolbar = findViewById(R.id.appbar_main);
        setSupportActionBar(toolbar);

        BottomNavigationView navigationView = findViewById(R.id.navigation_view);
        NavigationUI.setupWithNavController(navigationView, navController);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ChooseActivity.READ_STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (homeFragment != null){
                    homeFragment.setProgressbar();
                }
                Toast.makeText(this, "Read external storage granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Read external storage denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public HomeFragment homeFragment = null;


}// class

