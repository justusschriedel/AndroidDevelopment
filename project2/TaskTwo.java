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
	HashMap<String, ArrayList<Integer>> connections = new HashMap<String, ArrayList<Integer>>(); 

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

		if (protocol == 6) {
		    int dataOffset = (TaskOne.getBitVal(bytes, i+ihl+12, 4, 8)*32)/8;
		    int srcPort = TaskOne.get16BitVal(bytes, i+ihl);
		    int destPort = TaskOne.get16BitVal(bytes, i+ihl+2);

		    if (destPort == 80) {
			String srcIP = "", destIP = "";

			for (int x = 0; x < 3; x++) {
			    srcIP += TaskOne.getBitVal(bytes, i+12+x, 0, 8) + ".";
			    destIP += TaskOne.getBitVal(bytes, i+16+x, 0, 8) + ".";
			}

			srcIP += TaskOne.getBitVal(bytes, i+12+3, 0, 8) + " ";
			destIP += TaskOne.getBitVal(bytes, i+16+3, 0, 8) + " ";

			String tcpConn = srcIP + srcPort + " " + destIP + destPort + " ";

			if (connections.containsKey(tcpConn)) {
			    int data = length - ihl - dataOffset;
			    int uplink = connections.get(tcpConn).get(0) + data;
			    int downlink = connections.get(tcpConn).get(1);
			    
			    ArrayList<Integer> dataSent = connections.get(tcpConn);
			    dataSent.remove(0);
			    dataSent.remove(0);
			    dataSent.add(0, uplink);
			    dataSent.add(1, downlink);
			    dataSent.add(i);

			    connections.put(tcpConn, dataSent);
			}
			else {
			    int data = length - ihl - dataOffset;
			    int uplink = data;
			    int downlink = 0;
			    
			    ArrayList<Integer> dataSent = new ArrayList<Integer>();
			    dataSent.add(uplink);
			    dataSent.add(downlink);
			    dataSent.add(i);

			    connections.put(tcpConn, dataSent);
			}
		    }
		    else if (srcPort == 80) {
			String srcIP = "", destIP = "";

			for (int x = 0; x < 3; x++) {
			    srcIP += TaskOne.getBitVal(bytes, i+12+x, 0, 8) + ".";
			    destIP += TaskOne.getBitVal(bytes, i+16+x, 0, 8) + ".";
			}

			srcIP += TaskOne.getBitVal(bytes, i+12+3, 0, 8) + " ";
			destIP += TaskOne.getBitVal(bytes, i+16+3, 0, 8) + " ";

			String tcpConn = destIP + destPort + " " + srcIP + srcPort + " ";

			if (connections.containsKey(tcpConn)) {
			    int data = length - ihl - dataOffset;
			    int uplink = connections.get(tcpConn).get(0);
			    int downlink = connections.get(tcpConn).get(1) + data;

			    ArrayList<Integer> dataSent = connections.get(tcpConn);
			    dataSent.remove(0);
			    dataSent.remove(0);
			    dataSent.add(0, uplink);
			    dataSent.add(1, downlink);
			    dataSent.add(i);

			    connections.put(tcpConn, dataSent);
			}
			else {
			    int data = length - ihl - dataOffset;
			    int uplink = 0;
			    int downlink = data;

			    ArrayList<Integer> dataSent = new ArrayList<Integer>();
			    dataSent.add(uplink);
			    dataSent.add(downlink);
			    dataSent.add(i);

			    connections.put(tcpConn, dataSent);
			}
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
	System.out.println(totalPackets + " " + connections.size() + "\n");
	
	Set<String> keySet = connections.keySet();
	for (String s : keySet) {
	    String output = s + "" + connections.get(s).get(0) + " " + connections.get(s).get(1) + " ";

	    for (int x = 2; x < connections.get(s).size(); x++) {
		output += connections.get(s).get(x) + " ";
	    }

	    System.out.println(output);
	    System.out.println();
	}
	//TODO: use sequence numbers and acknowledgment numbers to determine order of data in stream
	//      and output the data for each connection in the right order (in binary?)
	//remember: the side sending data makes seq number = TCP length + previous seq number and makes ack
	//          number = ?something?; side sending acknowledgment of receiving data (but not sending data)
	//          makes received seq number ack number and received ack number seq number (from what I could
	//          see on wireshark)
    }
}
