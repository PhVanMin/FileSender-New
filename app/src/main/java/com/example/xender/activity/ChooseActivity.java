package com.example.xender.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.xender.Dialog.MyApplication;
import com.example.xender.Loader.ContactsLoader;
import com.example.xender.R;
import com.example.xender.adapter.ContactAdapter;
import com.example.xender.fragment.AudioFragment;
import com.example.xender.fragment.ContactFragment;
import com.example.xender.fragment.FileFragment;
import com.example.xender.fragment.PhotoFragment;
import com.example.xender.fragment.VideoFragment;
import com.example.xender.permission.PermissionChecker;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChooseActivity extends AppCompatActivity {
    Toolbar toolbar;
    NavController navController;
    public static final int READ_CONTACTS_PERMISSION=1;
    public static final int READ_IMAGES_PERMISSION=2;
    public static final int READ_FILES_PERMISSION=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        toolbar = findViewById(R.id.appbar_send);
        toolbar.setTitle("Chọn tập tin");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView navigationView = findViewById(R.id.navigation_view);
        NavigationUI.setupWithNavController(navigationView,navController);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_IMAGES_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Read external storage granted",Toast.LENGTH_SHORT).show();
                photoFragment.loadImages();
            } else {
                Toast.makeText(this,"Read external storage denied",Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == READ_CONTACTS_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Read external storage granted",Toast.LENGTH_SHORT).show();
                contactFragment.loadContacts();
            } else {
                Toast.makeText(this,"Read external storage denied",Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == READ_FILES_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Read external storage granted",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"Read external storage denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.setActivity(this);
    }

    public PhotoFragment photoFragment;
    public ContactFragment contactFragment;
    public FileFragment fileFragment;
    public VideoFragment videoFragment;
    public AudioFragment audioFragment;

}