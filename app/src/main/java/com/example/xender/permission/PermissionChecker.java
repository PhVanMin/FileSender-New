package com.example.xender.permission;

import static com.example.xender.activity.QRActivity.ACCESS_WIFI_STATE;
import static com.example.xender.activity.QRActivity.NEARBY_WIFI_DEVICE;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.xender.activity.ChooseActivity;
import com.example.xender.activity.QRActivity;

public class PermissionChecker {
    static String TAG="PermissionChecker";
    public static boolean checkReadExternalStorage(Activity activity){
        if( (android.os.Build.VERSION.SDK_INT) <= 32) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(activity, new String[]
                        {Manifest.permission.READ_EXTERNAL_STORAGE}, ChooseActivity.READ_IMAGES_PERMISSION);
                Log.d(TAG, "checkReadExternalStorage: FALSE");
                return false;
            }
        }
        Log.d(TAG, "checkReadExternalStorage: TRUE");
        return  true;
    };

    public static boolean checkAccessNearbyDevice(Activity activity){
        if (ContextCompat.checkSelfPermission(
                activity, Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.NEARBY_WIFI_DEVICES},
                    NEARBY_WIFI_DEVICE);
            Log.d(TAG, "checkAccessNearbyDevice: FALSE");
            return false;
        }
        Log.d(TAG, "checkAccessNearbyDevice: TRUE");
        return true;
    }
    public static boolean checkAccessWifiState(Activity activity){
        if (ContextCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_WIFI_STATE},
                    ACCESS_WIFI_STATE);
            Log.d(TAG, "checkAccessWifiState: FALSE");
            return false;
        }

        Log.d(TAG, "checkAccessWifiState: TRUE");
        return true;
    }

    public static boolean checkWriteExternalStorage(Activity activity){
        if( (android.os.Build.VERSION.SDK_INT) <= 32) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(activity, new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE}, QRActivity.WRITE_EXTERNAL_STORAGE);
                Log.d(TAG, "checkWriteExternalStorage: FALSE");
                return false;
            }
        }
        Log.d(TAG, "checkWriteExternalStorage: TRUE");
        return  true;
    };

    public static boolean CheckFineLocation(Activity activity){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(activity, new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION}, QRActivity.ACCESS_FINE_LOCATION);

            }
            Log.d(TAG, "checkAccessFileLocation: FALSE");
            return false;
        }
        Log.d(TAG, "checkAccessFileLocation: TRUE");
        return true;
    }
}
