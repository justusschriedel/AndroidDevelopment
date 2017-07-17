package com.example.echo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoActivity extends AppCompatActivity {
    public static String output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echo);

        Intent intent = getIntent();

        String hostName = intent.getStringExtra(MainActivity.HOSTNAME);
        String portNumber = intent.getStringExtra(MainActivity.PORTNUMBER);

        String[] args = {hostName, portNumber};

        try {
            EchoClient.main(args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void echoButton(View view) {
        EditText editText = (EditText) findViewById(R.id.editText3);
        TextView textView = (TextView) findViewById(R.id.textView);

        EchoClient.userInput = editText.getText().toString();
        textView.setText(output);
    }
}
