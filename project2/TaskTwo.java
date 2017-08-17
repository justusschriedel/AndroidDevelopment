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
	HashMap<String, ArrayList<Pair<Integer, Integer>>> connections = new HashMap<String, ArrayList<Pair<Integer, Integer>>>(); 

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
			    int uplink = connections.get(tcpConn).get(0).getFirst() + data;
			    int downlink = connections.get(tcpConn).get(0).getSecond();
			    int seqNumber = TaskOne.get32BitVal(bytes, i+ihl+4);
			    
			    ArrayList<Pair<Integer, Integer>> dataSent = connections.get(tcpConn);
			    dataSent.remove(0);
			    dataSent.add(0, new Pair(uplink, downlink));
			    dataSent.add(new Pair(i, seqNumber));

			    connections.put(tcpConn, dataSent);
			}
			else {
			    int data = length - ihl - dataOffset;
			    int uplink = data;
			    int downlink = 0;
			    int seqNumber = TaskOne.get32BitVal(bytes, i+ihl+4);
			    
			    ArrayList<Pair<Integer, Integer>> dataSent = new ArrayList<Pair<Integer, Integer>>();
			    dataSent.add(new Pair(uplink, downlink));
			    dataSent.add(new Pair(i, seqNumber));

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
			    int uplink = connections.get(tcpConn).get(0).getFirst();
			    int downlink = connections.get(tcpConn).get(0).getSecond() + data;
			    int seqNumber = TaskOne.get32BitVal(bytes, i+ihl+4);

			    ArrayList<Pair<Integer, Integer>> dataSent = connections.get(tcpConn);
			    dataSent.remove(0);
			    dataSent.add(0, new Pair(uplink, downlink));
			    dataSent.add(new Pair(i, seqNumber));

			    connections.put(tcpConn, dataSent);
			}
			else {
			    int data = length - ihl - dataOffset;
			    int uplink = 0;
			    int downlink = data;
			    int seqNumber = TaskOne.get32BitVal(bytes, i+ihl+4);

			    ArrayList<Pair<Integer, Integer>> dataSent = new ArrayList<Pair<Integer, Integer>>();
			    dataSent.add(new Pair(uplink, downlink));
			    dataSent.add(new Pair(i, seqNumber));

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
	
	String uplink = "", downlink = "";
	Set<String> keySet = connections.keySet();
	for (String s : keySet) {
	    ArrayList<Pair<Integer, Integer>> list = connections.get(s);
	    ArrayList<Pair<Integer, Integer>> tempUp = new ArrayList<Pair<Integer, Integer>>();
	    ArrayList<Pair<Integer, Integer>> tempDown = new ArrayList<Pair<Integer, Integer>>();
	    String connOutput = s + "" + list.get(0).getFirst() + " " + list.get(0).getSecond() + " ";

	    if ((list.get(0).getFirst() != 0) || (list.get(0).getSecond() != 0)) {
		for (int x = 1; x < list.size(); x++) {
		    int index = list.get(x).getFirst();

		    int length = TaskOne.get16BitVal(bytes, index+2); 
		    int ihl = (TaskOne.getBitVal(bytes, index, 0, 4)*32)/8;
		    int dataOffset = (TaskOne.getBitVal(bytes, index+ihl+12, 4, 8)*32)/8;

		    if ((length - ihl - dataOffset) != 0) {
			int destPort = TaskOne.get16BitVal(bytes, index+ihl+2);

			if (destPort == 80) {
			    //uplink
			    tempUp.add(list.get(x));
			}
			else {
			    //downlink
			    tempDown.add(list.get(x));
			}
		    }
		}
	    }
	    
	    System.out.println(connOutput);
	    
	    ArrayList<Pair<Integer, Integer>> uplinkData = sort(tempUp);
	    ArrayList<Pair<Integer, Integer>> downlinkData = sort(tempDown);

	    int z = 0;
	    while (z < uplinkData.size() || z < downlinkData.size()) {
		if (z < uplinkData.size()) {
		    int index = uplinkData.get(z).getFirst();
		    
		    int length = TaskOne.get16BitVal(bytes, index+2); 
		    int ihl = (TaskOne.getBitVal(bytes, index, 0, 4)*32)/8;
		    int dataOffset = (TaskOne.getBitVal(bytes, index+ihl+12, 4, 8)*32)/8;

		    for (int q = index+ihl+dataOffset; q < index+length; q++) {
			uplink += bytes[q] + " ";
		    }
		}

		if (z < downlinkData.size()) {
		    int index = downlinkData.get(z).getFirst();
		    
		    int length = TaskOne.get16BitVal(bytes, index+2); 
		    int ihl = (TaskOne.getBitVal(bytes, index, 0, 4)*32)/8;
		    int dataOffset = (TaskOne.getBitVal(bytes, index+ihl+12, 4, 8)*32)/8;

		    for (int q = index+ihl+dataOffset; q < index+length; q++) {
			downlink += bytes[q] + " ";
		    }
		}

		z++;
	    }
	}

	System.out.println(uplink + downlink);
	//TODO: use sequence numbers and acknowledgment numbers to determine order of data in stream
	//      and output the data for each connection in the right order (in binary?)
	//remember: the side sending data makes seq number = TCP length + previous seq number and makes ack
	//          number = ?something?; side sending acknowledgment of receiving data (but not sending data)
	//          makes received seq number ack number and received ack number seq number (from what I could
	//          see on wireshark)
    }

    public static ArrayList<Pair<Integer, Integer>> sort(ArrayList<Pair<Integer, Integer>> list) {
	ArrayList<Pair<Integer, Integer>> sorted = new ArrayList<Pair<Integer, Integer>>();
	
	for (int i = 0; i < list.size(); i++) {
	    int curr = list.get(i).getSecond();

	    if (sorted.isEmpty()) {
		sorted.add(list.get(i));
	    }
	    else {
		int x = 0;
		while (true) {
		    int seq = sorted.get(x).getSecond();

		    if (!(x-1 < 0) && ((curr < seq) && (curr > sorted.get(x-1).getSecond()))) {
			sorted.add(x, list.get(i));
			break;
		    }
		    else if (!(x+1 >= sorted.size()) && ((curr > seq) && (curr < sorted.get(x+1).getSecond()))) {
			sorted.add(x, list.get(i));
			break;
		    }
		    else if (curr < seq) {
			if (x > 0) x--;
			else {
			    sorted.add(x, list.get(i));
			    break;
			}
		    }
		    else if (curr > seq) {
			if (x < sorted.size()-1) x++;
			else {
			    sorted.add(list.get(i));
			    break;
			}
		    }
		    else {
			sorted.add(x, list.get(i));
			break;
		    }
			
		    
		}
	    }
	}

	return sorted;
    }
}
