import java.util.*;
import java.io.*;
import java.nio.*;

public class TaskTwo {
    public static void main(String[] args) {
	String filename = "test.pcap";
	File file = new File(filename);
	int filesize = Math.toIntExact(file.length());
	FileInputStream fromFile;
	FileOutputStream outputFile;
	byte[] bytes = new byte[filesize];
	//HashMap<String, ArrayList<Pair<Integer, Integer>>> connections = new HashMap<String, ArrayList<Pair<Integer, Integer>>>();
	HashMap<Integer, Pair<String, String>> seqNums = new HashMap<Integer, Pair<String, String>>(); //key = sequence number, value = tcp connection, total number of bytes sent over link

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

	//storing bytes from byte array into hashmap
	//int totalPackets = 0;
	int i = 54;
	while (i < bytes.length) {
	    System.out.println("1"); /////
	    int version = TaskOne.getBitVal(bytes, i, 4, 8);
	    System.out.println(version);

	    if (version == 4) {
		System.out.println("ip"); /////
		int ihl = (TaskOne.getBitVal(bytes, i, 0, 4)*32)/8;
		int length = TaskOne.get16BitVal(bytes, i+2);
		int protocol = TaskOne.getBitVal(bytes, i+9, 0, 8);

		if (protocol == 6) {
		    String srcIP = "", destIP = "";
		    int dataOffset = (TaskOne.getBitVal(bytes, i+ihl+12, 4, 8)*32)/8;
		    int srcPort = TaskOne.get16BitVal(bytes, i+ihl);
		    int destPort = TaskOne.get16BitVal(bytes, i+ihl+2);
		    int seq = TaskOne.get32BitVal(bytes, i+ihl+4);
		    String data = "";
		    
		    for (int x = 0; x < 3; x++) {
			System.out.println("2"); /////
			srcIP += TaskOne.getBitVal(bytes, i+12+x, 0, 8) + ".";
			destIP += TaskOne.getBitVal(bytes, i+16+x, 0, 8) + ".";
		    }
		    srcIP += TaskOne.getBitVal(bytes, i+12+3, 0, 8) + " ";
		    destIP += TaskOne.getBitVal(bytes, i+16+3, 0, 8) + " ";

		    for (int x = i+ihl+dataOffset; x < i+length; x++) {
			System.out.println("3"); /////
			data += bytes[x];
		    }

		    String tcpConn = srcIP + srcPort + " " + destIP + destPort;
		    System.out.println(tcpConn);

		    seqNums.put(seq, new Pair<String, String>(tcpConn, data));  
		}

		//dealing with padding
		if ((length+14) < 60) {
		    System.out.println("p"); /////
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

	    //totalPackets++;
	}
	//System.out.println(totalPackets + " " + connections.size() + "\n");

	//looking over table info; preparing to send to output file
	Object[] sortedSeqs = seqNums.keySet().toArray();
	Arrays.sort(sortedSeqs); //sorts numbers in ascending order

	ArrayList<String> ips = new ArrayList<String>();
	ArrayList<String> data = new ArrayList<String>();

	for (Object x : sortedSeqs) {
	    System.out.println("4"); /////
	    Pair<String, String> pair = seqNums.get(x);
	    String ip = pair.getFirst();
	    String datum = pair.getSecond();

	    //error: ips and data are becoming different sizes for some reason
	    //error in else clause
	    if (!ips.contains(ip)) {
		ips.add(ip);

		ArrayList<String> temp = sort(ips);

		ips.removeAll(ips);
		ips.addAll(temp);
		System.out.println(ips.indexOf(ip));
		if (ips.indexOf(ip) < data.size()) {
		    data.add(ips.indexOf(ip), datum);
		}
		else data.add(datum);

		System.out.println(ips.size() + data.size());
	    }
	    else {
		int index = ips.indexOf(ip);
		String temp = data.get(index) + "" + datum;
		System.out.println(temp);

		data.remove(index);
		data.add(index, temp);
	    }
	}

	String connections = "";
	for (int y = 0; y < ips.size(); y++) {
	    System.out.println("5"); /////
	    String curr = ips.get(y);
	    
	    if (!curr.endsWith("80")) {
		String[] temp = curr.split(" ");
		String newIP = temp[2] + " " + temp[3] + " " + temp[0] + " " + temp[1];

		for (int z = 0; z < ips.size(); z++) {
		    System.out.println("6"); /////
		    String correctIP = ips.get(z);

		    if (newIP.equals(correctIP)) {
		        int uplink = data.get(z).getBytes().length;
			int downlink = data.get(y).getBytes().length;

			connections += correctIP + " " + uplink + " " + downlink + "\n";

			String d = data.get(z) + "" +  data.get(y);
			System.out.println(connections + d);

			data.remove(z);
			data.add(z, d);

			ips.remove(y);
			//ips.remove(z);
			data.remove(y);

			break;
		    }
		}
	    }
	}

	/*try {
	    outputFile = new FileOutputStream("task2_test1.out");
	    outputFile.write(connections.getBytes());

	    for (int z = 0; z < data.size(); z++) {
		outputFile.write(data.get(z).getBytes());
	    }
	}
	catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	catch (IOException e) {
	    e.printStackTrace();
	    }*/
	//TODO: debug/optimize code
	    
    }

    //sorts elements of given arraylist in ascending order
    public static ArrayList<String> sort(ArrayList<String> unsorted) {
	ArrayList<String> sorted = new ArrayList<String>();

	for (String s : unsorted) {
	    System.out.println("7"); /////
	    if (!sorted.isEmpty()) {
		for (int i = 0; i < sorted.size(); i++) {
		    System.out.println("8"); /////
		    if (s.compareTo(sorted.get(i)) <= 0) {
			sorted.add(i, s);
			break;
		    }
		    else if (i+1 == sorted.size()) {
			sorted.add(s);
		    }
		}
	    }
	    else sorted.add(s);
	}

	return sorted;
    }
}
/*if (destPort == 80) {
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
		    }*/

	/*String uplink = "", downlink = "";
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

	System.out.println(uplink + downlink);*/
  /*public static ArrayList<Pair<Integer, Integer>> sort(ArrayList<Pair<Integer, Integer>> list) {
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
	}*/
