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

	int i = 36;
	while (i < bytes.length) {
	    int size = getValue(bytes, i, 0, 8);
	    int sum = 0, total8 = 0, b = 1;
	    
	    if (size == 255) {
	        do {
		    total8 = getValue(bytes, i+b, 0, 8);
		    sum += total8;
		    b++;
		}
		while (total8 != 0);

		size += sum;
	    }

	    totalPackets++;
	    i += (size + 12);
	    //System.out.println(i);
	    System.out.println(totalPackets);
	}

	System.out.println(totalPackets);
	//TODO: use hashmap for TCP connections
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
    public static int getValue(byte[] array, int index, int offset, int limit) {
	int value = 0, power = 0;

	for (int i = offset; i < limit; i++) {
	    if (isOne(array, index, i)) {
		value += Math.pow(2.0, (double) power);
	    }
	    power++;
	}

	return value;
    }
}
