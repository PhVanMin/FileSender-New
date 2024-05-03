package com.example.xender.activity;

import static com.example.xender.adapter.FileAdapter.IMAGE_FILE;
import static com.example.xender.adapter.FileAdapter.extension;
import static com.example.xender.adapter.FileAdapter.readableFileSize;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.xender.Dialog.MyApplication;
import com.example.xender.R;

import com.example.xender.db.FileCloudDatabaseHandler;

import com.example.xender.db.FileSendDatabaseHandler;
import com.example.xender.model.FileCloud;
import com.example.xender.service.SendFileService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
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

        Log.d("adapter 11", file.getAbsolutePath());
        Log.d("adapter 11", file.getName());
        int index = extension.getOrDefault(FilenameUtils.getExtension(file.getName()), 0);
        Log.d("adapter 11", String.valueOf(index));
        fileImage.setImageResource(IMAGE_FILE[index]);
        fileName.setText(file.getName());
        sizeDate.setText(readableFileSize(file.length()));
        toolbar = findViewById(R.id.appbar_send);
        toolbar.setTitle("Gửi tập tin");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        sendButton.setOnClickListener(v -> sendFile(file));
        uploadButton.setOnClickListener(v -> uploadFile(file));
        FirebaseApp.initializeApp(/*context=*/ this);
    }
    static String TAG= "Upload File";
    public  void uploadFile(File _file){
        AlertDialog dialog = getUploadDialog();
        dialog.show();
        Uri file =Uri.fromFile(_file);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType(null)
                .build();
        String path = "files/" + file.getLastPathSegment();
        StorageReference filePath = storageRef.child(path);
        UploadTask uploadTask = filePath
                        .putFile(file, metadata);
        uploadTask.addOnProgressListener(taskSnapshot -> {
            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
            Log.d(TAG, "Upload is " + progress + "% done");
        }).addOnPausedListener(taskSnapshot -> Log.d(TAG, "Upload is paused"))
            .addOnFailureListener(exception -> Log.d(TAG, "Upload is fail" + exception))
            .addOnSuccessListener(taskSnapshot -> {
                Log.d(TAG, "Upload is success");
                dialog.cancel();
                Toast.makeText(this, "Upload successful!", Toast.LENGTH_SHORT).show();
            });

        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                Log.d(TAG, "then: " + task.getException().toString());
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return filePath.getDownloadUrl();
        }).addOnCompleteListener(task -> {
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
        });

    }
    public void sendFile(File current){
        SendFileService.current=current;
        Intent intent = new Intent(this, SendFileService.class);
        startService(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.setActivity(this);
    }

    private AlertDialog getUploadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

// 2. Chain together various setter methods to set the dialog characteristics.
        builder.setMessage("Uploading...")
                .setTitle("File upload")
                .setCancelable(false);

// 3. Get the AlertDialog.
        return builder.create();
    }
}
