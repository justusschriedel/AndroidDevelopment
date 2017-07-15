package com.example.echo;

import android.annotation.TargetApi;
import android.os.Build;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by jschried on 7/15/17.
 */
// TODO: 7/15/17 complete EchoServer 
public class EchoClient {
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void main(String[] args) throws Exception {
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
            Socket echoServer = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(echoServer.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoServer.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));)
        {
            String userInput;

            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("echo: " + in.readLine());
            }
        }
    }
}
