package com.example.xender.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xender.R;
import com.example.xender.adapter.FileCloudAdapter;
import com.example.xender.handler.DatabaseHandler;
import com.example.xender.model.FileCloud;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private DatabaseHandler databaseHandler;
    private FileCloudAdapter fileCloudAdapter;
    private ListView listView;
    private Button downloadBtn;
    private TextView fileCloudUri;
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

//        downloadBtn = findViewById(R.id.download_btn);
//        fileCloudUri = findViewById(R.id.inputURI);
//
//        downloadBtn.setOnClickListener(view -> {downloadFileCloud(fileCloudUri.getText().toString());});
//
//        databaseHandler = new DatabaseHandler(this);
//        listView = findViewById(R.id.list_cloud_file);
//        initData();
//        fileCloudAdapter = new FileCloudAdapter(this,R.layout.file,fileCloudList);
//        listView.setAdapter(fileCloudAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @SuppressLint("ResourceType")
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                FileCloud current = fileCloudList.get(position);
//                Intent qrCloud = new Intent();
//                qrCloud.setClass(CloudActivity.this,QRCloudActivity.class);
//                qrCloud.putExtra("QRCODE",current.getUri());
//                startActivity(qrCloud);
//               // fileCloudQrFragment.generateQRCode(current.getUri());
//            }
//        });
    }

    private void initData(){
        fileCloudList = databaseHandler.getAllFileClouds();
        fileCloudList.forEach(fileCloud -> {
            Log.d(TAG, "initData: "  + fileCloud.getName());
        });

    }

    private void downloadFileCloud(String uri){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(uri);
      //  Log.d(TAG, "downloadFileCloud: " + httpsReference.getName());
        httpsReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
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
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.toString());
            }
        });
    };


}