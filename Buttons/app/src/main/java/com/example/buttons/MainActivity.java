package com.example.buttons;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //corresponds to what button 1 does when pressed
    public void typeMessage(View view) {
        Intent intent = new Intent(this, TypeMessageActivity.class);
        startActivity(intent);
    }

    //corresponds to what button 2 does when pressed
    public void seeStar(View view) {
        Intent intent = new Intent(this, SeeStarActivity.class);
        startActivity(intent);
    }
}
