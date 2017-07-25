package com.example.filedownloader;

import android.content.ActivityNotFoundException;
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
    String[] args;
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

    public void openFile(View view) {
        TextView textView = (TextView) findViewById(R.id.textView);

        String filename = args[2];

        File file = new File(Environment.getExternalStorageDirectory(), "/Download/" + filename);
        Uri fileUri = Uri.fromFile(file);

        Intent openIntent = new Intent(Intent.ACTION_VIEW);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (filename.endsWith(".txt") || filename.endsWith(".doc")) {
            openIntent.setDataAndType(fileUri, "text/plain");
        }
        else if (filename.endsWith(".pdf")) {
            openIntent.setDataAndType(fileUri, "application/pdf");
        }
        else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png") ||
        filename.endsWith(".bmp") || filename.endsWith(".gif")) {
            openIntent.setDataAndType(fileUri, "image/" + filename.substring(filename.indexOf('.') + 1));
        }
        else if (filename.endsWith(".wav") || filename.endsWith(".mp4")) {
            openIntent.setDataAndType(fileUri, "video/" + filename.substring(filename.indexOf('.') + 1));
        }
        else {
            textView.setText("file cannot be displayed");
            return;
        }

        try {
            startActivity(openIntent);
        }
        catch (ActivityNotFoundException e) {
            textView.setText("file cannot be displayed");
        }
    }
}
