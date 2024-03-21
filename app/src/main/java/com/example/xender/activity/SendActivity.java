package com.example.xender.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.xender.Loader.ContactsLoader;
import com.example.xender.R;
import com.example.xender.adapter.ContactAdapter;
import com.example.xender.fragment.ContactFragment;
import com.example.xender.fragment.FileFragment;
import com.example.xender.fragment.PhotoFragment;
import com.example.xender.model.Contact;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;

public class SendActivity extends AppCompatActivity {
    Toolbar toolbar;
    NavController navController;
    public static final int READ_CONTACTS_PERMISSION=1;
    public static final int READ_IMAGES_PERMISSION=2;
    Adapter contactAdapter;


    public Adapter getContactAdapter() {
        return contactAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        toolbar = findViewById(R.id.appbar_send);
        toolbar.setTitle("Chọn tập tin");
        toolbar.isBackInvokedCallbackEnabled();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView navigationView = findViewById(R.id.navigation_view);
        NavigationUI.setupWithNavController(navigationView,navController);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ContactsLoader loader = new ContactsLoader(this);
        loader.getContactList();
        contactAdapter  = new ContactAdapter(this,R.layout.contact,loader.getContacts());


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
    }
    public PhotoFragment photoFragment;
    public ContactFragment contactFragment;
    public FileFragment fileFragment;
}