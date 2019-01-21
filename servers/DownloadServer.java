import java.io.*;
import java.net.*;

public class DownloadServer extends Thread{

    
    private ServerSocket welcomeSocket;

    
    public DownloadServer(int port) {
	try {
	    welcomeSocket = new ServerSocket(port);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }
    

    public void run() {
	while (true) {
	    try {
		String file;
		
		Socket clientSocket = welcomeSocket.accept();

		BufferedReader textFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		file = textFromClient.readLine();

		sendFile(clientSocket, file);
		
		textFromClient.close();
	    }
	    catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
    
    
    private void sendFile(Socket clientSocket, String file) throws IOException {
	File requestedFile = new File(file);
	
	DataOutputStream toClient = new DataOutputStream(clientSocket.getOutputStream());
	FileInputStream fileForClient = new FileInputStream(requestedFile);
	byte[] buffer = new byte[4096];

	toClient.writeInt(Math.toIntExact(requestedFile.length()));
		
	while (fileForClient.read(buffer) > 0) {
	    toClient.write(buffer);
	}
		
        fileForClient.close();
	toClient.close();	
    }

    public static void main(String[] args) {
	DownloadServer ds = new DownloadServer(Integer.parseInt(args[0]));
	ds.start();
    }
}
