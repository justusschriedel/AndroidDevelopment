import java.util.*;
import java.io.*;
import java.nio.*;

public class TaskOne {
    public int fileSize, totalPackets, totalIP, totalTCP, totalUDP, totalConn;
    
    public static void main(String[] args) {
	FileInputStream fromFile;
	Scanner scan = new Scanner(System.in);
	byte[] fileBytes;
	//ByteBuffer bytes;

	System.out.println("Enter .pcap file name:");
	String fileName = scan.nextLine();
	File file = new File(fileName);
	int fileSize = Math.toIntExact(file.length());

	fileBytes = new byte[fileSize];
	
	try {
	    fromFile = new FileInputStream(file);
	    fromFile.read(fileBytes, 0, fileSize);
	}
	catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	catch (IOException e) {
	    e.printStackTrace();
	}

	ByteBuffer bytes = ByteBuffer.wrap(fileBytes);

	System.out.println(bytes.getInt(36));
    }
	
}
