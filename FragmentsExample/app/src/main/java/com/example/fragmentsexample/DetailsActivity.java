package com.example.fragmentsexample;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class DetailsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //checks if orientation is landscape, and if it is, this activity isn't needed (so nothing
        // happens/is returned)
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        //plug in details fragment to activity
        if (savedInstanceState == null) {
            DetailsFragment details = new DetailsFragment();
            details.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }
    }
}
