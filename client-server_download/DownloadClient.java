import java.io.*;
import java.net.*;

public class DownloadClient {

    private Socket clientSocket;

    
    public DownloadClient(String host, int port, String file) {
	try {
	    clientSocket = new Socket(host, port);

	    PrintWriter textToServer = new PrintWriter(clientSocket.getOutputStream(), true);
	    textToServer.println(file);
	    
	    saveFile(file);

	    textToServer.close();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }
    

    private void saveFile(String file) throws IOException {
	DataInputStream fromServer = new DataInputStream(clientSocket.getInputStream());
	FileOutputStream fileFromServer = new FileOutputStream(file);
	byte[] buffer = new byte[4096];
	
	int filesize = fromServer.readInt();
	int read = 0;
	int totalRead = 0;
	int remaining = filesize;
	while((read = fromServer.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
	    totalRead += read;
	    remaining -= read;
	    System.out.println("read " + totalRead + " bytes.");
	    fileFromServer.write(buffer, 0, read);
	}

        fileFromServer.close();
        fromServer.close();
    }

    public static void main(String[] args) {
        DownloadClient dc = new DownloadClient(args[0], Integer.parseInt(args[1]), args[2]);
    }  
}
