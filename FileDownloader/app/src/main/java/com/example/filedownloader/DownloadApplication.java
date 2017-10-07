package com.example.filedownloader;

import android.app.Application;
import android.content.Context;

public class DownloadApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        DownloadApplication.context = getApplicationContext();
    }

    public static Context getContext() {
        return DownloadApplication.context;
    }
}
