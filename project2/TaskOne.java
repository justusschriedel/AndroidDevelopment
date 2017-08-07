import java.util.*;
import java.io.*;
import java.nio.*;

//method isOne found on stackoverflow
//https://stackoverflow.com/questions/18931283/checking-individual-bits-in-a-byte-array-in-java

public class TaskOne {
    
    
    public static void main(String[] args) {
	int fileSize, totalPackets, totalIP, totalTCP, totalUDP;//, totalConn;
	FileInputStream fromFile;
	Scanner scan = new Scanner(System.in);
	byte[] bytes;
	HashMap<TCPConnection, Integer> tcpConn = new HashMap<TCPConnection, Integer>();

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

	totalPackets = 0;
	totalIP = 0;
	totalTCP = 0;
	totalUDP = 0;
	//totalConn = 0;

	//TODO: use hashmap for TCP connections
	//TODO: store source, dest IP and source, dest port into string;
	//      store string in hashmap

	/*System.out.println(get16BitVal(bytes, 56));
	System.out.println(get16BitVal(bytes, 146));
	System.out.println(get16BitVal(bytes, 236));
	System.out.println(get16BitVal(bytes, 326));
	System.out.println(get16BitVal(bytes, 408));*/

	int i = 54;
	while (i < bytes.length) {
	    int version = getBitVal(bytes, i, 4, 8);
	    int size = get16BitVal(bytes, i+2);
	    int protocol = getBitVal(bytes, i+9, 0, 8);
	    System.out.println(version);
	    System.out.println(size);
	    System.out.println(protocol);

	    int sIP = get32BitVal();
	    int dIP = get32BitVal();
	    int sPort = ;
	    int dPort = ;
	    
	    if (version == 4) totalIP++;
	    
	    totalPackets++;
	    
	    if (protocol == 6) {totalTCP++;}
	    else if (protocol == 17) {totalUDP++;}
	    
	    i += (size + 30);;
	}

	System.out.println(totalPackets + " " + totalIP + " " + totalTCP + " " + totalUDP);

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


