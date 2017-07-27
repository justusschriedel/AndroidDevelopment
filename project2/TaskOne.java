import java.util.*;
import java.io.*;

public class TaskOne {
    public int fileSize, totalPackets, totalIP, totalTCP, totalUDP, totalConn;
    
    public static void main(String[] args) {
	FileInputStream fromFile;
	Scanner scan = new Scanner(System.in);
	byte[] fileBytes;

	System.out.println("Enter .pcap file name:");
	String fileName = scan.nextLine();
	File file = new File(fileName);
	int fileSize = Math.toIntExact(file.length());

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

	fileBytes = new byte[fileSize];
	
	/*
	  for (int i = 1622; i < 1704; i++) {
	    System.out.print(fileBytes[i] + " ");
	    }
	*/

	System.out.println();
	System.out.println((int) fileBytes[0]);
    }
}
