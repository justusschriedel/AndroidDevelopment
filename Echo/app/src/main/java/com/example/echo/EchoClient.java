package com.example.echo;

import android.annotation.TargetApi;
import android.os.Build;

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
        )
        {
            /*do {
                out.println(userInput);
                EchoActivity.output = "echo: " + in.readLine();
            }
            while (!(userInput.equals("break")));*/

            out.println(userInput);
            EchoActivity.output = "echo: " + in.readLine();
            echoServer.close();
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
