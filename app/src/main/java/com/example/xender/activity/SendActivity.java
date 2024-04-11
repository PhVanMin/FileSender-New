package com.example.xender.activity;

import static com.example.xender.adapter.FileAdapter.IMAGE_FILE;
import static com.example.xender.adapter.FileAdapter.extension;
import static com.example.xender.adapter.FileAdapter.readableFileSize;

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

import com.example.xender.R;

import com.example.xender.db.FileCloudDatabaseHandler;

import com.example.xender.model.FileCloud;
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
import java.sql.Timestamp;
import java.util.Date;

public class SendActivity extends AppCompatActivity {

    private File file;
    TextView fileName;
    TextView sizeDate;
    ImageView fileImage;
    Button backButton ;
    Toolbar toolbar;
    Button uploadButton ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        String path = getIntent().getStringExtra("File");
        file = new File(path);
        fileName = findViewById(R.id.fileName);
        fileImage = findViewById(R.id.fileImage);
        sizeDate = findViewById(R.id.sizeDate);
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

}
