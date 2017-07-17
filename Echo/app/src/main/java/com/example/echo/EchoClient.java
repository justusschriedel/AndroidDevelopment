package com.example.echo;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.EditText;

import java.io.*;
import java.net.*;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class EchoClient {
    public static String userInput;

    public static void main(String[] args) throws IOException {
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
            Socket echoServer = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(echoServer.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoServer.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));)
        {

            /*while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("echo: " + in.readLine());
            }*/
            do {
                userInput = stdIn.readLine();
                out.println(userInput);
                //System.out.println("echo: " + in.readLine());
                EchoActivity.output = "echo: " + in.readLine();
            }
            while (!(userInput.equals("break")));
        }
        catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        }
        catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}
