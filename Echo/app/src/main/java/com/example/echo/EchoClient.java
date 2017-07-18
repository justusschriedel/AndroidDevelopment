package com.example.echo;

import android.annotation.TargetApi;
import android.os.Build;

import java.io.*;
import java.net.*;

import static com.example.echo.R.string.textView;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class EchoClient {
    public static String userInput;

    public static void main(String[] args) throws IOException {
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
            Socket echoServer = new Socket(hostName, portNumber);
            //EchoActivity.output = "hello";
            PrintWriter out = new PrintWriter(echoServer.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoServer.getInputStream()));
        )
        {
            /*do {
                out.println(userInput);
                EchoActivity.output = "echo: " + in.readLine();
                //textView.setText(EchoActivity.output);
                //echoServer.close();
            }
            while (userInput != null);*/

            out.println(userInput);
            EchoActivity.output = "echo: " + in.readLine();

            //echoServer.close();
        }
        catch (UnknownHostException e) {
            EchoActivity.output = "Don't know about host " + hostName;
        }
        catch (IOException e) {
            EchoActivity.output = "Couldn't get I/O for the connection to " + hostName;
        }
        catch (SecurityException e) {
            EchoActivity.output = "The checkConnect method doesn't allow the operation";
        }
        catch (IllegalArgumentException e) {
            EchoActivity.output = "The port parameter is outside the specified range of valid port values";
        }
    }
}
