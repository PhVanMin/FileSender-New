package com.example.xender.activity;

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

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        createNotificationChannel();
        Button showDialog = findViewById(R.id.ShowDialog);
        showDialog.setOnClickListener(v -> {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Transfer")
                    .setSmallIcon(R.drawable.send_svgrepo_com)
                    .setContentTitle("Title")
                    .setContentText("Context text")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.d("Permission", "Failed");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                return;
            }
            Log.d("Permission", "Success");
            NotificationManagerCompat notification = NotificationManagerCompat.from(this);
            notification.notify(1, builder.build());
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