package com.example.kingismon.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class LUTemonApp extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public void onCreate() {
        super.onCreate();
        LUTemonApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return LUTemonApp.context;
    }
}