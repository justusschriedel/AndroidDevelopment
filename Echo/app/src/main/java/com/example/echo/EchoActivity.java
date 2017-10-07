package com.example.echo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.UnknownHostException;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class EchoActivity extends AppCompatActivity {
    public static String hostName, portNumber, output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echo);

        Intent intent = getIntent();
        hostName = intent.getStringExtra(MainActivity.HOSTNAME);
        portNumber = intent.getStringExtra(MainActivity.PORTNUMBER);

        TextView textView2 = (TextView) findViewById(R.id.textView2);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        textView2.setText("Host Name: " + hostName);
        textView3.setText("Port Number: " + portNumber);
    }

    public void echoButton(View view) {
        final String[] args = {hostName, portNumber};
        EditText editText = (EditText) findViewById(R.id.editText3);
        final TextView textView = (TextView) findViewById(R.id.textView);

        EchoClient.userInput = editText.getText().toString();

        //new EchoTask().execute(args);
        //textView.setText(output);


        new Thread(new Runnable() {
            public void run() {
                try {
                    EchoClient.main(args);
                }
                catch (UnknownHostException e) {
                    System.exit(1);
                }
                catch (IOException e) {
                    System.exit(1);
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
