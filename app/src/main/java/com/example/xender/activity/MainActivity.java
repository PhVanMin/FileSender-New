package com.example.xender.activity;

import static androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.xender.Dialog.TransferDialog;
import com.example.xender.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    Toolbar toolbar;
    public static boolean downloading = true;
    int PROGRESS_MAX = 100;
    int PROGRESS_CURRENT = 0;
    int PROGRESS_STEP = 5;
    Thread updateProgress;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        createNotificationChannel();
        Button showDialog = findViewById(R.id.ShowDialog);
        showDialog.setOnClickListener(v -> {
            Intent snoozeIntent = new Intent(this, MyBroadcastReceiver.class);
            snoozeIntent.setAction(MyBroadcastReceiver.ACTION_CANCEL);
            PendingIntent snoozePendingIntent =
                    PendingIntent.getBroadcast(this, 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Transfer")
                    .setSmallIcon(R.drawable.send_svgrepo_com)
                    .setContentTitle("Transferring files...")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(R.drawable.send_svgrepo_com, "Cancel", snoozePendingIntent);

            builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
            NotificationManagerCompat notification = NotificationManagerCompat.from(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "Failed");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                return;
            }

            updateProgress = new Thread(() -> {
                notification.notify(1, builder.build());

                while (downloading && PROGRESS_CURRENT < PROGRESS_MAX) {
                    PROGRESS_CURRENT += PROGRESS_STEP;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
                    notification.notify(1, builder.build());

                    if (PROGRESS_CURRENT > PROGRESS_MAX)
                        PROGRESS_CURRENT = PROGRESS_MAX;
                }

                builder.setContentText("Download complete")
                        .setProgress(0,0,false);
                notification.notify(1, builder.build());
            });

            updateProgress.start();
        });
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
}