package com.example.filedownloader;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.os.Environment.getExternalStorageDirectory;

public class DownloadActivity extends AppCompatActivity {
    public static String output;
    String[] args = {};
    static final String TEXT = "com.example.fileDownload.TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        Intent intent = getIntent();
        args = intent.getStringArrayExtra(MainActivity.ARGS);

        final TextView textView = (TextView) findViewById(R.id.textView);

        new Thread(new Runnable() {
            public void run() {
                try {
                    DownloadClient.main(args);
                }
                catch (Exception e) {
                    output = e.getMessage();
                }

                textView.post(new Runnable() {
                    public void run() {
                        textView.setText(output);
                    }
                });
            }
        }).start();


    }
}
