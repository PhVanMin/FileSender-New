package com.example.xender.activity;

import static androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.xender.Dialog.TransferDialog;
import com.example.xender.R;
import com.example.xender.Utils.VirusScanUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> activityResultLauncher;
    NavController navController;
    Toolbar toolbar;
    public static boolean downloading = true;
    int PROGRESS_MAX = 100;
    int PROGRESS_CURRENT = 0;
    int PROGRESS_STEP = 5;
    Thread updateProgress;
    String resource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Uri returnUri = data.getData();

                        String name = getFileNameFromUri(this, returnUri);

                        File scanFile = new File(getFilesDir().getAbsolutePath(), "haha.txt");
                        Log.d("File exist", scanFile.exists() ? "True" : "False");
                        Log.d("File name", scanFile.getName());
                        Log.d("File path", scanFile.getPath());
                        VirusScanUtil.scanFile(scanFile);
//                        try (InputStream inputStream = getContentResolver().openInputStream(data.getData())) {
//                            File scanFile = new File(getFilesDir().getAbsolutePath(), name);
//                            Log.d("File name", scanFile.getName());
//                            Log.d("File path", scanFile.getPath());
//                            FileOutputStream outputStream = new FileOutputStream(scanFile);
//
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                                FileUtils.copy(inputStream, outputStream);
//                            }
//                            outputStream.close();
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
                    }
                });

        Button chooseFile = findViewById(R.id.ShowDialog);
        chooseFile.setOnClickListener(v -> {
            File scanFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "Use-case.txt");
            Log.d("File readable", scanFile.canRead() ? "True" : "False");
            Log.d("File exist", scanFile.exists() ? "True" : "False");
            Log.d("File name", scanFile.getName());
            Log.d("File path", scanFile.getPath());
            Log.d("File size", String.valueOf(scanFile.length()));
            Thread scanVirusThread = new Thread(() -> {
                resource = VirusScanUtil.scanFile(scanFile);
            });
            scanVirusThread.start();
//            Intent filechooser = new Intent(Intent.ACTION_GET_CONTENT);
//            filechooser.setType("*/*");
//            filechooser.addCategory(Intent.CATEGORY_OPENABLE);
//            activityResultLauncher.launch(filechooser);
        });

        Button getScanInfo = findViewById(R.id.GetScan);
        getScanInfo.setOnClickListener(v -> {
            Thread getScanReport = new Thread(() -> {
                VirusScanUtil.getFileScanReport(resource);
            });
            getScanReport.start();
        });
//        createNotificationChannel();
//        Button showDialog = findViewById(R.id.ShowDialog);
//        showDialog.setOnClickListener(v -> {
//            Intent snoozeIntent = new Intent(this, MyBroadcastReceiver.class);
//            snoozeIntent.setAction(MyBroadcastReceiver.ACTION_CANCEL);
//            PendingIntent snoozePendingIntent =
//                    PendingIntent.getBroadcast(this, 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Transfer")
//                    .setSmallIcon(R.drawable.send_svgrepo_com)
//                    .setContentTitle("Transferring files...")
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .addAction(R.drawable.send_svgrepo_com, "Cancel", snoozePendingIntent);
//
//            builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
//            NotificationManagerCompat notification = NotificationManagerCompat.from(this);
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                Log.d("Permission", "Failed");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
//                return;
//            }
//
//            updateProgress = new Thread(() -> {
//                notification.notify(1, builder.build());
//
//                while (downloading && PROGRESS_CURRENT < PROGRESS_MAX) {
//                    PROGRESS_CURRENT += PROGRESS_STEP;
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                    builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
//                    notification.notify(1, builder.build());
//
//                    if (PROGRESS_CURRENT > PROGRESS_MAX)
//                        PROGRESS_CURRENT = PROGRESS_MAX;
//                }
//
//                builder.setContentText("Download complete")
//                        .setProgress(0,0,false);
//                notification.notify(1, builder.build());
//            });
//
//            updateProgress.start();
//        });

        
//        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//
//        toolbar = findViewById(R.id.appbar_main);
//        setSupportActionBar(toolbar);
//
//        BottomNavigationView navigationView = findViewById(R.id.navigation_view);
//        NavigationUI.setupWithNavController(navigationView,navController);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Transfer";
            String description = "Transfer";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Transfer", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private String getFileNameFromUri(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        Log.d("Get name", "Ok");
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        String fileName = cursor.getString(index);
        cursor.close();
        return fileName;
    }
}