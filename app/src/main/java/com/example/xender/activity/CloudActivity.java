package com.example.xender.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.DialogFragmentNavigatorDestinationBuilder;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xender.Dialog.MyApplication;
import com.example.xender.R;
import com.example.xender.adapter.FileCloudAdapter;

import com.example.xender.db.FileCloudDatabaseHandler;
import com.example.xender.db.LocalDatabaseHandler;
import com.example.xender.fragment.DownloadFragment;
import com.example.xender.fragment.UploadFragment;
import com.example.xender.model.FileCloud;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CloudActivity extends AppCompatActivity {
    private static String TAG = "Cloud activity debug";
    private Toolbar toolbar;
    private List<FileCloud> fileCloudList;
    private FileCloudDatabaseHandler databaseHandler;
    private FileCloudAdapter fileCloudAdapter;
    private ListView listView;
    private Button downloadBtn;
    private TextView fileCloudUri;
    private NavController navController;
    public UploadFragment uploadFragment;
    public DownloadFragment downloadFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHandler = new FileCloudDatabaseHandler(this);
        setContentView(R.layout.activity_cloud);
        toolbar = findViewById(R.id.appbar_send);
        toolbar.setTitle("Cloud");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView navigationView = findViewById(R.id.navigation_view);
        NavigationUI.setupWithNavController(navigationView,navController);
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    public void setViewUploadFragment(){

        listView = findViewById(R.id.list_cloud_file);
        initData();
        fileCloudAdapter = new FileCloudAdapter(this,R.layout.file,fileCloudList);
        listView.setAdapter(fileCloudAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            FileCloud current = fileCloudList.get(position);
            Intent qrCloud = new Intent();
            qrCloud.setClass(CloudActivity.this,QRCloudActivity.class);
            qrCloud.putExtra("QRCODE",current.getUri());
            startActivity(qrCloud);
            // fileCloudQrFragment.generateQRCode(current.getUri());
        });
    }
    private void initData(){
        fileCloudList = databaseHandler.getAll();
        fileCloudList.forEach(fileCloud -> {
            Log.d(TAG, "initData: "  + fileCloud.getName());
        });
    }

    private void downloadFileCloud(String uri){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(uri);
      //  Log.d(TAG, "downloadFileCloud: " + httpsReference.getName());
        httpsReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            try {
                String dirPath= android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                String fileName  = httpsReference.getName();
                File file = new File(dirPath+"/"+fileName);
                if(file.exists()){
                    file.renameTo(new File(dirPath+"/copy_of_"+fileName));
                }
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bos.write(bytes);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).addOnFailureListener(e -> Log.d(TAG, e.toString()));
    };
    @Override
    protected void onResume() {

        super.onResume();
        MyApplication.setActivity(this);
    }

}