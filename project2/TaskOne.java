import java.util.*;
import java.io.*;
import java.nio.*;

public class TaskOne {
    
    
    public static void main(String[] args) {
	int fileSize, totalPackets, totalIP, totalTCP, totalUDP, totalConn;
	FileInputStream fromFile;
	Scanner scan = new Scanner(System.in);
	byte[] fileBytes;
	ByteBuffer bytes;

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
	IntBuffer fileInfo = bytes.asIntBuffer();

	System.out.println(fileInfo.get(36));

	totalPackets = 0;
	totalIP = 0;
	totalTCP = 0;
	totalUDP = 0;
	totalConn = 0;

	System.out.println(bytes.order(ByteOrder.LITTLE_ENDIAN).getInt(402));
	
	int i = 36; int x = 0;
	while (i < fileBytes.length && x < 45) {
	    System.out.println(i);
	    totalPackets++;
	    int size = fileBytes[i];
	    i += size + 12;
	    x++;
	}

	System.out.println(totalPackets)

    }
}
	/*	
        Runtime runtime = Runtime.getRuntime();
	BufferedReader infoReader = null;

	String[] ips = null;
	String[] tcps = null;
	String[] udps = null;
	
	try {
	    Process process = runtime.exec("tshark -r " + fileName + " -z ptype,tree");
	    infoReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

	    totalPackets = 0;
	    
	    String line = infoReader.readLine();
	    while (!(line.contains("======="))) {
	        totalPackets++;
		line = infoReader.readLine();
	    }

	    while (!(line.startsWith("IP Protocol Types "))) {
		line = infoReader.readLine();
	    }

	    ips = line.split(" ");
	    tcps = infoReader.readLine().split(" ");
	    udps = infoReader.readLine().split(" ");
	}
	catch (IOException e) {
	    e.printStackTrace();
	}

	try {
	    Process process = runtime.exec("tshark -r " + fileName + " -z conv,tcp");
	    infoReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

	    totalConn = 0;
	    
	    String line = infoReader.readLine();
	    while (!(line.contains("======="))) {
		line = infoReader.readLine();
	    }

	    int i = 0;
	    while (i < 5) {
		line = infoReader.readLine();
		i++;
	    }
	    
	    while (!(line.contains("======="))) {
		totalConn++;
		line = infoReader.readLine();
	    }
	}
	catch (IOException e) {
	    e.printStackTrace();
	}

	System.out.println((--totalPackets) + " " + removeSpaces(ips).get(3) + " " +
			   removeSpaces(tcps).get(1) + " " + removeSpaces(udps).get(1) +
			   " " + totalConn + "\n");


    public static ArrayList<String> removeSpaces(String[] array) {
	ArrayList<String> newArray = new ArrayList<String>();

	for (int i = 0; i < array.length; i++) {
	    if (!(array[i].equals(""))) {
		newArray.add(array[i]);
	    }
	}

	return newArray;
    }*/
