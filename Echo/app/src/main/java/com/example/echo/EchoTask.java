package com.example.echo;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.UnknownHostException;

import static com.example.echo.R.id.textView;

public class EchoTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String ... args) {
        try {
            EchoClient.main(args);
        }
        catch (UnknownHostException e) {
            System.exit(1);
        }
        catch (IOException e) {
            System.exit(1);
        }

        return null;
    }

    /*protected void onPostExcecute(String message) {
        EchoActivity.textView.setText(message);
    }*/
}
