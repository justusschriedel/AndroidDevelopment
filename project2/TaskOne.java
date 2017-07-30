import java.util.*;
import java.io.*;
import java.nio.*;

public class TaskOne {
    public static int fileSize, totalPackets, totalIP, totalTCP, totalUDP, totalConn;
    
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

	//System.out.println(fileBytes[36] + " " + fileBytes[37] + " "
	//		   + fileBytes[38] + " " + fileBytes[39]);

	ByteBuffer bytes = ByteBuffer.wrap(fileBytes);
	IntBuffer fileInfo = bytes.asIntBuffer();

	System.out.println(fileInfo.get(36));

	totalPackets = 0;
	totalIP = 0;
	totalTCP = 0;
	totalUDP = 0;
	totalConn = 0;

	/*int i = 36; int x = 0;
	while (i < fileBytes.length && x < 45) {
	    System.out.println(i);
	    totalPackets++;
	    int size = fileBytes[i];
	    i += size + 12;
	    x++;
	}

	System.out.println(totalPackets);*/
    }
	
}
