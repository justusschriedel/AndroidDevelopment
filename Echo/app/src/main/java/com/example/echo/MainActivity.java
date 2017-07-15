package com.example.echo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String HOSTNAME = "com.example.echo.HOSTNAME";
    public static final String PORTNUMBER = "com.example.echo.PORTNUMBER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goButton(View view) {
        Intent intent = new Intent(this, EchoActivity.class);

        EditText editText = (EditText) findViewById(R.id.editText);
        EditText editText2 = (EditText) findViewById(R.id.editText2);
        String hostName = editText.getText().toString();
        String portNumber = editText2.getText().toString();

        intent.putExtra(HOSTNAME, hostName);
        intent.putExtra(PORTNUMBER, portNumber);

        startActivity(intent);
    }
}
