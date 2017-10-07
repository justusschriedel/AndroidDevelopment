import java.util.*;
import java.io.*;
import java.nio.*;

//method isOne found on stackoverflow
//https://stackoverflow.com/questions/18931283/checking-individual-bits-in-a-byte-array-in-java

public class TaskOne {
    
    
    public static void main(String[] args) {
	int fileSize, totalPackets, totalIP, totalTCP, totalUDP;
	FileInputStream fromFile;
	FileOutputStream outputFile;
	byte[] bytes;
	HashMap<String, Integer> tcpConn = new HashMap<String, Integer>();

	String fileName = "testdata/task1.test2.pcap";

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

	totalPackets = 0;
	totalIP = 0;
	totalTCP = 0;
	totalUDP = 0;
	
	int i = 54;
	while (i < bytes.length) {
	    int version = getBitVal(bytes, i, 4, 8);
	    int ethertype = get16BitVal(bytes, i-2);

	    if (version == 4 && ethertype >= 1536) {
		totalIP++;

		int ihl = (getBitVal(bytes, i, 0, 4)*32)/8;
		int size = get16BitVal(bytes, i+2);
		int protocol = getBitVal(bytes, i+9, 0, 8);
		
		if (protocol == 6) {
		    totalTCP++;
		    
		    int sIP = get32BitVal(bytes, i+12);
		    int dIP = get32BitVal(bytes, i+16);
		    int sPort = get16BitVal(bytes, i+ihl);
		    int dPort = get16BitVal(bytes, i+ihl+2);
		    
		    String connA = "" + sIP + sPort + dIP + dPort;
		    String connB = "" + dIP + dPort + sIP + sPort;
		    
		    if (tcpConn.containsKey(connA)) tcpConn.put(connA, tcpConn.get(connA)+1);
		    else if (tcpConn.containsKey(connB)) tcpConn.put(connB, tcpConn.get(connB)+1);
		    else tcpConn.put(connA, 1);
 
		}
		else if (protocol == 17) {
		    totalUDP++;
		}

		if ((size+14) < 60) {
		    int padding = 60 - (size + 14);

		    if (i+size+padding < bytes.length) {
			int count = 0;
			for (int x = (i+size); x < (i+size+padding); x++) {
			    int val = getBitVal(bytes, x, 0, 8);
			    
			    if (val == 0) count++;
			}
			
			if (count == padding) i += (size + 30 + padding);
			else i += (size + 30);
		    }
		    else i += (size + 30);
		}
		else  i += (size + 30);
	    }
	    else {
		int size = bytes[i-18];

		i += (size + 16);
	    }
	    
	    totalPackets++;
	}

	String temp = totalPackets + " " + totalIP + " " + totalTCP + " " + totalUDP + " " + tcpConn.size() + "\n";
	
	byte[] output = temp.getBytes();

	try {
	    outputFile = new FileOutputStream("task1_test2.out");
	    outputFile.write(output);
	}
	catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	catch (IOException e) {
	    e.printStackTrace();
	}

    }

    //checks if bit in given byte at given position is 1
    //  array: byte array containing wanted bit
    //  index: byte in array where wanted bit is
    //  position: position of bit in byte
    public static boolean isOne(byte[] array, int index, int position) {
	return ((array[index] >> position) & 1) == 1;  //the part with & will return 1 if both
	                                               //sides of & are 1
    }

    //gets value of bits at given index between offset and limit
    //  array: byte array containing bits to investigate
    //  index: byte where wanted bits are
    //  offset: where getValue should start summing bits
    //  limit: where getValue should stop summing bits
    public static int getBitVal(byte[] array, int index, int offset, int limit) {
	int value = 0, power = 0;

	for (int i = offset; i < limit; i++) {
	    if (isOne(array, index, i)) {
		value += Math.pow(2.0, (double) power);
	    }
	    power++;
	}

	return value;
    }

    //TODO: add description
    public static int get16BitVal(byte[] array, int index) {
	int value = 0;

	for (int i = 0; i < 16; i++) {
	    if (i >= 8) {
		if (isOne(array, index, i-8)) {
		    value += Math.pow(2.0, (double) i);
		}	    }
	    else {
		if (isOne(array, index+1, i)) {
		    value += Math.pow(2.0, (double) i);
		}
	    }
	}

	return value;
    }

    //TODO: add description
    public static int get32BitVal(byte[] array, int index) {
	int value = 0;

	for (int i = 0; i < 32; i++) {
	    if (i >= 24) {
		if (isOne(array, index, i-24)) {
		    value += Math.pow(2.0, (double) i);
		}
	    }
	    else if (i >= 16) {
		if (isOne(array, index+1, i-16)) {
		    value += Math.pow(2.0, (double) i);
		}
	    }
	    else if (i >= 8) {
		if (isOne(array, index+2, i-8)) {
		    value += Math.pow(2.0, (double) i);
		}
	    }
	    else {
		if (isOne(array, index+3, i)) {
		    value += Math.pow(2.0, (double) i);
		}
	    }
	}

	return value;
    }
    
}


