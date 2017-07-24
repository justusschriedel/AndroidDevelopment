package com.example.filedownloader;

import android.content.ContentProvider;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

        /*File file = new File(getExternalStorageDirectory(), args[2]);
        Uri path = Uri.fromFile(file);
        Intent openFileIntent = new Intent(Intent.ACTION_VIEW, path);
        openFileIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openFileIntent.setDataAndType(path, "application/");
        startActivity(openFileIntent);*/

    }

    public void readFile(View view) {
        TextView textView = (TextView) findViewById(R.id.textView);
        String text = "";

        try {
            File file = new File(getExternalStorageDirectory(), args[2]);
            FileInputStream is = new FileInputStream(file);

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            while (true) {
                text = br.readLine();
                if (text == null) break;
            }
            isr.close();
            is.close();
            br.close();


            Intent intent = new Intent(this, ReadFileActivity.class);
            intent.putExtra(TEXT, text);
            startActivity(intent);
        }
        catch (FileNotFoundException e) {
            textView.setText(e.getMessage());
        }
        catch (IOException e) {
            textView.setText(e.getMessage());
        }

    }

}
