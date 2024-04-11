package com.example.xender.Dialog;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context context;
    private static Activity activity = null;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }
    public  static void setActivity(Activity _activity){
        activity = _activity;
    }
    public static  Activity getActivity(){
        return activity;
    }
    public static Context getAppContext() {
        return MyApplication.context;
    }
}