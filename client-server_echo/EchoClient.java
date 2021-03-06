import java.io.*;
import java.net.*;
 
public class EchoClient {
    public static void main(String[] args) throws IOException {
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
            Socket echoServer = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(echoServer.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoServer.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));)
        {
            String userInput;

	    /* while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("echo: " + in.readLine());
		}*/
	    do {
		//userInput = stdIn.readLine();
		out.println(userInput);
		System.out.println("echo: " + in.readLine());
		userInput = stdIn.readLine();
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
