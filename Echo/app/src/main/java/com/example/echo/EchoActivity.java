package com.example.echo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EchoActivity extends AppCompatActivity {

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
            //some error relating to failed server connection
        }

        // TODO: 7/15/17  figure out if an EditText is needed for PrintView object in EchoClient
        // TODO: 7/15/17  exception handler
    }
}
