package com.example.xender.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Objects;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public static String ACTION_CANCEL = "actionCancelAlarm";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), ACTION_CANCEL)) {
            MainActivity.downloading = false;
        }
    }
}
