import java.net.*;
import java.io.*;

public class EchoServer {
    public static void main(String[] args) throws IOException {
	int portNumber = Integer.parseInt(args[0]);
	ServerSocket serverSocket = new ServerSocket(portNumber);

	while (true) {
	    Socket clientSocket = serverSocket.accept();
	    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

	    String inputLine = in.readLine();
	    if (inputLine != null) {
	       out.println(inputLine.toUpperCase());
	    }
	}

	/*try (
	     ServerSocket serverSocket = new ServerSocket(portNumber);
	     Socket clientSocket = serverSocket.accept();

	     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	     )
	    {
		String inputLine;

		//loop to keep server from closing after 1 echo
		do {
		    inputLine = in.readLine();

		     if (inputLine != null) {
			out.println(inputLine.toUpperCase());
		     }
		}
		while (true);

		while ((inputLine = in.readLine()) != null) {
		    out.println(inputLine.toUpperCase());
		    
		    }
	    }
	catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
			       + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }*/
    }
}
