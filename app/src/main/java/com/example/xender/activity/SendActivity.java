package com.example.xender.activity;

import static com.example.xender.adapter.FileAdapter.IMAGE_FILE;
import static com.example.xender.adapter.FileAdapter.extension;
import static com.example.xender.adapter.FileAdapter.readableFileSize;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.DialogFragmentNavigatorDestinationBuilder;

import com.example.xender.Dialog.MyApplication;
import com.example.xender.R;

import com.example.xender.db.FileCloudDatabaseHandler;

import com.example.xender.db.FileSendDatabaseHandler;
import com.example.xender.handler.SendReceiveHandler;
import com.example.xender.model.FileCloud;
import com.example.xender.model.FileSend;
import com.example.xender.wifi.MyWifi;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;

public class SendActivity extends AppCompatActivity {

    private File file;
    private FileSendDatabaseHandler fileSendDatabaseHandler;
    private String connectionAddress;
    TextView fileName;
    TextView sizeDate;
    ImageView fileImage;
    Button backButton ;
    Toolbar toolbar;
    Button uploadButton ;
    Button sendButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        String path = getIntent().getStringExtra("File");
        file = new File(path);
        fileName = findViewById(R.id.fileName);
        fileImage = findViewById(R.id.fileImage);
        sizeDate = findViewById(R.id.sizeDate);
        sendButton = findViewById(R.id.SendButton);
        uploadButton = findViewById(R.id.upload_btn);


        int index = extension.get(FilenameUtils.getExtension(file.getAbsolutePath()));
        Log.d("adapter ", String.valueOf(index));
        fileImage.setImageResource(IMAGE_FILE[index]);
        fileName.setText(file.getName());
        sizeDate.setText(readableFileSize(file.length()));
        toolbar = findViewById(R.id.appbar_send);
        toolbar.setTitle("Chọn tập tin");
        toolbar.isBackInvokedCallbackEnabled();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFile(file);
            }
        });
       uploadButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               uploadFile(file);
           }
       });
        FirebaseApp.initializeApp(/*context=*/ this);
    }
    static String TAG= "Upload File";
    public  void uploadFile(File _file){
        //  StorageUtil.getAllDir(Environment.getExternalStorageDirectory(),StorageUtil.FILTER_BY_DOCUMENT);



        Uri file =Uri.fromFile(_file);

//        //FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
//        firebaseAppCheck.installAppCheckProviderFactory(
//                PlayIntegrityAppCheckProviderFactory.getInstance());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType(null)
                .build();
        String path = "files/" + file.getLastPathSegment();
        StorageReference filePath = storageRef.child(path);
        UploadTask uploadTask = (UploadTask) filePath
                        .putFile(file, metadata);





        // ...

        // Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.d(TAG, "Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "Upload is fail" + exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Upload is success");
            }
        });

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "then: " + task.getException().toString());
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.d(TAG, "then: " + downloadUri.toString());
                    FileCloud fileCloud = new FileCloud(_file.getName(),
                            downloadUri.toString(),
                            new Timestamp(new Date().getTime()));
                    FileCloudDatabaseHandler handler = new FileCloudDatabaseHandler(SendActivity.this);
                    handler.add(fileCloud);
                } else {
                    Log.d(TAG, "then: fail " + task.getException().toString());
                    // ...
                }
            }
        });

    }
    public void sendFile(File current){

                try {
                    byte[] bytes = Files.readAllBytes(Paths.get(current.getAbsolutePath()));
                //    Log.d("WifiDirect", "onItemClick: "+ MyWifi.socket.toString());
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SendReceiveHandler handler =null;

                            if (MyWifi.socket != null)
                            {
                                handler = new SendReceiveHandler(MyWifi.socket);
                                connectionAddress = "Wifi";
                            }
                           else if (MyWifi.bluetoothSocket != null)
                            {
                                handler = new SendReceiveHandler(MyWifi.bluetoothSocket);
                                connectionAddress = "Bluetooth";
                            }


                            if (handler != null) {
                                try {
                                    handler.writeLong(current.length());
                                    handler.writeUTF(current.getName());
                                    handler.write(bytes);
                                    fileSendDatabaseHandler = new FileSendDatabaseHandler(SendActivity.this);
                                    boolean isDatabaseExists = fileSendDatabaseHandler.isDatabaseExists(SendActivity.this);
                                    Log.d(TAG, "Is database exists: " + isDatabaseExists);

                                    SQLiteDatabase db = fileSendDatabaseHandler.getReadableDatabase();
                                    boolean isTableExists = fileSendDatabaseHandler.isTableExists(db, "file_sends");
                                    if(isTableExists != true)
                                    {
                                        fileSendDatabaseHandler.onCreate(db);
                                    }
                                    Log.d(TAG, "Is table exists: " + isTableExists);
                                    FileSend fileSend = new FileSend(
                                            0,
                                            current.getName(),
                                            current.getPath(),
                                            connectionAddress,
                                            new Timestamp(new Date().getTime()),
                                            true
                                    );
                                    fileSendDatabaseHandler.add(fileSend);
                                    for (FileSend f: fileSendDatabaseHandler.getAll()
                                    ) {
                                        Log.d(TAG, f.getId() + f.getFileName());
                                    }
                                } catch (IOException e) {
                                    Log.d("WifiDirect", "Exception " + e.toString());
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
                    thread.start();
                    Log.d("WifiDirect", "onItemClick: ");
                } catch (Exception e) {
                    Log.d("WifiDirect", "onItemClick: "+e.toString());
                    throw new RuntimeException(e);
                }
    }
    @Override
    protected void onResume() {

        super.onResume();
        MyApplication.setActivity(this);
    }
}
