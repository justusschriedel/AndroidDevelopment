import java.util.*;
import java.io.*;
import java.nio.*;

public class TaskTwo {
    public static void main(String[] args) {
	String filename = args[0];
	File file = new File(filename);
	int filesize = Math.toIntExact(file.length());
	FileInputStream fromFile;
	byte[] bytes = new byte[filesize];
	HashMap<String, Integer> connections = new HashMap<String, Integer>(); 

	try {
	    fromFile = new FileInputStream(file);
	    fromFile.read(bytes, 0, filesize);
	}
	catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	catch (IOException e) {
	    e.printStackTrace();
	}

	int totalPackets = 0;
	int i = 54;
	while (i < bytes.length) {
	    int version = TaskOne.getBitVal(bytes, i, 4, 8);

	    if (version == 4) {
		int ihl = (TaskOne.getBitVal(bytes, i, 0, 4)*32)/8;
		int length = TaskOne.get16BitVal(bytes, i+2);
		int protocol = TaskOne.getBitVal(bytes, i+9, 0, 8);
		//System.out.println(protocol);

		if (protocol == 6) {
		    int srcPort = TaskOne.get16BitVal(bytes, i+ihl);
		    int destPort = TaskOne.get16BitVal(bytes, i+ihl+2);

		    if (srcPort == 80 || destPort == 80) {
			String srcIP = "", destIP = "";

			for (int x = 0; x < 3; x++) {
			    srcIP += TaskOne.getBitVal(bytes, i+12+x, 0, 8) + ".";
			    destIP += TaskOne.getBitVal(bytes, i+16+x, 0, 8) + ".";
			}

			srcIP += TaskOne.getBitVal(bytes, i+12+3, 0, 8) + " ";
			destIP += TaskOne.getBitVal(bytes, i+16+3, 0, 8) + " ";

			System.out.println(srcIP + srcPort + " " + destIP + destPort);
		    }
		}

		if ((length+14) < 60) {
		    int padding = 60 - (length + 14);

		    if ((i+length+padding) < bytes.length) {
			int count = 0;
			for (int x = i+length; x < i+length+padding; x++) {
			    int value = TaskOne.getBitVal(bytes, x, 0, 8);
			    
			    if (value == 0) count++;
			}
			if (count == padding) {
			    i += length + padding + 30;
			}
			else i += length + 30;
		    }
		    else i += length + 30;
		}
		else i += length + 30;
	    }
	    else {
		int length = bytes[i-18];

		i += length + 16;
	    }

	    totalPackets++;
	}
	System.out.println(totalPackets);
    }
}
