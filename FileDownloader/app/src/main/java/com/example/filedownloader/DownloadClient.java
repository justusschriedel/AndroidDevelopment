package com.example.filedownloader;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import java.net.*;
import java.io.*;

public class DownloadClient {

    private Socket clientSocket;


    public DownloadClient(String host, int port, String file) {
        try {
            clientSocket = new Socket(host, port);

            PrintWriter textToServer = new PrintWriter(clientSocket.getOutputStream(), true);
            textToServer.println(file);

            saveFile(file, DownloadApplication.getContext());

            textToServer.close();
        }
        catch (Exception e) {
            DownloadActivity.output = e.getMessage();
        }
    }


    private void saveFile(String file, Context context) throws IOException {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File requestedfile = new File(path, file);
        requestedfile.createNewFile();
        try {
            path.mkdirs();
        } catch (Exception e) {
            DownloadActivity.output = e.getMessage();
        }

        DataInputStream fromServer = new DataInputStream(clientSocket.getInputStream());
        FileOutputStream fileFromServer = new FileOutputStream("/storage/emulated/0/Download/" + file);
                                        //context.openFileOutput(file, Context.MODE_PRIVATE);


        byte[] buffer = new byte[4096];

        int filesize = fromServer.readInt();
        int read = 0;
        int totalRead = 0;
        int remaining = filesize;
        while((read = fromServer.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
            totalRead += read;
            remaining -= read;
            DownloadActivity.output = "read " + totalRead + " bytes.";
            fileFromServer.write(buffer, 0, read);
        }

        DownloadActivity.output= "read " + totalRead + " bytes\ndownload complete";

        fileFromServer.flush();
        fileFromServer.close();
        fromServer.close();
    }

    public static void main(String[] args) {
        DownloadClient dc = new DownloadClient(args[0], Integer.parseInt(args[1]), args[2]);
    }

}
