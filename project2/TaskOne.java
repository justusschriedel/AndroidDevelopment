import java.util.*;
import java.io.*;
import java.nio.*;

public class TaskOne {
    
    
    public static void main(String[] args) {
	int fileSize, totalPackets, totalIP, totalTCP, totalUDP, totalConn;
	FileInputStream fromFile;
	Scanner scan = new Scanner(System.in);
	byte[] bytes;
	//ByteBuffer bytes;

	System.out.println("Enter .pcap file name:");
	String fileName = scan.nextLine();

	File file = new File(fileName);
	fileSize = Math.toIntExact(file.length());

	bytes = new byte[fileSize];
	
	try {
	    fromFile = new FileInputStream(file);
	    fromFile.read(bytes, 0, fileSize);
	}
	catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	catch (IOException e) {
	    e.printStackTrace();
	}

	//bytes = ByteBuffer.wrap(fileBytes);

	//System.out.println(fileInfo.get(36));

	totalPackets = 0;
	totalIP = 0;
	totalTCP = 0;
	totalUDP = 0;
	totalConn = 0;
	
	//bytes.order(ByteOrder.BIG_ENDIAN);
	//bytes.get(array, 0, fileSize);

	/*for (int i = 0; i < 126; i++) {
	    //System.out.println(bytes.order(ByteOrder.BIG_ENDIAN).getInt(i));
	    System.out.print(array[i] + " ");
	}

	System.out.println();*/
	
	//System.out.println(bytes.order(ByteOrder.BIG_ENDIAN).getInt(36));
	
	/*int i = 42;
	while (i < fileBytes.length) {
	    //System.out.println(i);
	    totalPackets++;
	    int size = fileBytes[i] + fileBytes[i+1];
	    i += size + 18;
	}

	System.out.println(totalPackets);*/

	//TODO: use isOne method to analyze bits and find values that convey IPv4, TCP, UDP
	//TODO: use hashmap for TCP connections

    }

    //method idea found on stackoverflow
    //https://stackoverflow.com/questions/18931283/checking-individual-bits-in-a-byte-array-in-java
    public static boolean isOne(byte[] array, int bit) {
	int index = bit / 8;  //which byte contains this bit in the array
	int position = bit % 8;

	return (array[index] >> position & 1) == 1;

	//TODO: figure out if bits are being shifted in right direction (endianness)
	//TODO: figure out what & is doing here
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
