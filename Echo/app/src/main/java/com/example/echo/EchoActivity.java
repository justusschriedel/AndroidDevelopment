package com.example.echo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
    }

    public void echoButton(View view) {
        String[] args = {hostName, portNumber};
        EditText editText = (EditText) findViewById(R.id.editText3);
        TextView textView = (TextView) findViewById(R.id.textView);

        EchoClient.userInput = editText.getText().toString();

        try {
            EchoClient.main(args);
            textView.setText(output);
        }
        catch (Exception e) {
            System.exit(1);
            //e.printStackTrace();
        }
    }
}
