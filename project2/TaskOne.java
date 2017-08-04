import java.util.*;
import java.io.*;
import java.nio.*;

//method isOne found on stackoverflow
//https://stackoverflow.com/questions/18931283/checking-individual-bits-in-a-byte-array-in-java

public class TaskOne {
    
    
    public static void main(String[] args) {
	int fileSize, totalPackets, totalIP, totalTCP, totalUDP, totalConn;
	FileInputStream fromFile;
	Scanner scan = new Scanner(System.in);
	byte[] bytes;

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
	totalConn = 0;

	//TODO: use hashmap for TCP connections
	//TODO: get totalPackets, totalIP, totalTCP, totalUDP, totalConn

	/*System.out.println(get16BitVal(bytes, 56));
	System.out.println(get16BitVal(bytes, 146));
	System.out.println(get16BitVal(bytes, 236));
	System.out.println(get16BitVal(bytes, 326));
	System.out.println(get16BitVal(bytes, 408));*/

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
    public static int getByteVal(byte[] array, int index, int offset, int limit) {
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

    //TODO: rewrite this method like get16BitVal
    //TODO: add description
    public static int get32BitVal(byte[] array, int start) {
	int total = 0;

	for (int i = start; i < start+4; i++) {
	    total += getByteVal(array, i, 0, 8);
	}

	return total;
    }
}
