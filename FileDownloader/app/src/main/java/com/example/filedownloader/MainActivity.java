package com.example.filedownloader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    static final String ARGS = "com.example.filedownloader.ARGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void downloadFile(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        EditText editText2 = (EditText) findViewById(R.id.editText2);
        EditText editText3 = (EditText) findViewById(R.id.editText3);

        String hostname = editText.getText().toString();
        String portnumber = editText2.getText().toString();
        String filename = editText3.getText().toString();

        final String[] args = {hostname, portnumber, filename};

        Intent intent = new Intent(this, DownloadActivity.class);
        intent.putExtra(ARGS, args);
        startActivity(intent);
    }
}
