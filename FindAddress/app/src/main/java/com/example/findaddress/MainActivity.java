package com.example.findaddress;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import static java.lang.reflect.Array.getLength;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openAddress(View view) {
        //get address entered by user
        EditText editText = (EditText) findViewById(R.id.editText);
        String[] address = editText.getText().toString().split(" ");

        //format address to searchable location
        String formattedAddress = address[0];
        for(int i = 1; i < getLength(address); i++) {
            formattedAddress += "+" + address[i];
        }

        //build the intent with formatted address
        Uri location = Uri.parse("geo:0,0?q=" + formattedAddress);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);

        //verify there's a map app to find address
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
        boolean isSafe = activities.size() > 0;

        //start activity if map app exists
        if (isSafe) {
            startActivity(mapIntent);
        }
    }
}
