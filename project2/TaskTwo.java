import java.util.*;
import java.io.*;
import java.nio.*;

public class TaskTwo {
    public static void main(String[] args) {
	
	String filename = "test2.pcap";
	File file = new File(filename);
	int filesize = Math.toIntExact(file.length());
	FileInputStream fromFile;
	FileOutputStream outputFile;
	byte[] bytes = new byte[filesize];
	HashMap<Integer, Pair<String, byte[]>> seqNums = new HashMap<Integer, Pair<String, byte[]>>(); //key = sequence number, value = tcp connection, total number of bytes sent over link

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
	int i = 54;
	while (i < bytes.length) {
	    int version = TaskOne.getBitVal(bytes, i, 4, 8);

	    if (version == 4) {
		int ihl = (TaskOne.getBitVal(bytes, i, 0, 4)*32)/8;
		int length = TaskOne.get16BitVal(bytes, i+2);
		int protocol = TaskOne.getBitVal(bytes, i+9, 0, 8);

		if (protocol == 6) {
		    String srcIP = "", destIP = "";
		    int dataOffset = (TaskOne.getBitVal(bytes, i+ihl+12, 4, 8)*32)/8;
		    int srcPort = TaskOne.get16BitVal(bytes, i+ihl);
		    int destPort = TaskOne.get16BitVal(bytes, i+ihl+2);
		    int seq = TaskOne.get32BitVal(bytes, i+ihl+4);
		    System.out.println(seq);
		    byte[] data = new byte[length-ihl-dataOffset];
		    
		    if (srcPort == 80 || destPort == 80) {
			for (int x = 0; x < 3; x++) {
			    srcIP += TaskOne.getBitVal(bytes, i+12+x, 0, 8) + ".";
			    destIP += TaskOne.getBitVal(bytes, i+16+x, 0, 8) + ".";
			}
			srcIP += TaskOne.getBitVal(bytes, i+12+3, 0, 8);
			destIP += TaskOne.getBitVal(bytes, i+16+3, 0, 8);

			int z = 0;
			for (int x = i+ihl+dataOffset; x < i+length; x++) {
			    data[z] = bytes[x];
			    z++;
			}
			
			String tcpConn = srcIP + " " + srcPort + " " + destIP +  " " + destPort;
			seqNums.put(seq, new Pair<String, byte[]>(tcpConn, data));
		    } 
		}

		//dealing with padding
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
	}

	//looking over table info; preparing to send to output file
	Object[] sortedSeqs = seqNums.keySet().toArray();
	Arrays.sort(sortedSeqs); //sorts numbers in ascending order

	ArrayList<String> ips = new ArrayList<String>();
	ArrayList<byte[]> data = new ArrayList<byte[]>();

	for (Object x : sortedSeqs) {
	    Pair<String, byte[]> pair = seqNums.get(x);
	    String ip = pair.getFirst();
	    byte[] datum = pair.getSecond();
	    System.out.println(ip);

	    if (!ips.contains(ip)) {
		ips.add(ip);

		ArrayList<String> temp = sort(ips);

		ips.removeAll(ips);
		ips.addAll(temp);
		
		if (ips.indexOf(ip) < data.size()) {
		    data.add(ips.indexOf(ip), datum);
		}
		else data.add(datum);
	    }
	    else {
		int index = ips.indexOf(ip);
		byte[] temp = new byte[data.get(index).length + datum.length];

		for (int q = 0; q < temp.length; q++) {
		    if (q < data.get(index).length){
			temp[q] = data.get(index)[q];
		    }
		    else {
			temp[q] = datum[q-data.get(index).length];
		    }
		}

		data.remove(index);
		data.add(index, temp);
	    }
	}
	//System.out.println(ips); /////
	//a lot of ips are being sent to else clause
	//has to be due to mismatching strings, but still don't know what is different between them
	//could have to do with adding spaces at the end (maybe take that out for all strings?)
	String connections = "";
	for (int y = 0; y < ips.size(); y++) {
	    String curr = ips.get(y);
	    System.out.println(ips.get(y)); /////
	    if (!curr.endsWith("80")) {
		String[] temp = curr.split(" ");
		String newIP = temp[2] + " " + temp[3] + " " + temp[0] + " " + temp[1];
		System.out.println(newIP + " here"); /////

		for (int z = 0; z < ips.size(); z++) {
		    String correctIP = ips.get(z);

		    if (newIP.equals(correctIP)) {
		        int uplink = data.get(z).length;
			int downlink = data.get(y).length;

			connections += correctIP + " " + uplink + " " + downlink + "\n";

			byte[] d = new byte[data.get(z).length +  data.get(y).length];
			for (int q = 0; q < d.length; q++) {
			    if (q < data.get(z).length){
				d[q] = data.get(z)[q];
			    }
			    else {
				d[q] = data.get(y)[q-data.get(z).length];
			    }
			}

			data.remove(z);
			data.add(z, d);

			ips.remove(y);
			data.remove(y);

			break;
		    }
		}
	    }
	}
	System.out.println(ips); /////
	try {
	    outputFile = new FileOutputStream("task2_test1.out");
	    outputFile.write(connections.getBytes());

	    System.out.println(data);
	    for (int z = 0; z < data.size(); z++) {
		outputFile.write(data.get(z));
	    }
	}
	catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	catch (IOException e) {
	    e.printStackTrace();
	}
	//TODO: debug/optimize code
	    
    }

    //sorts elements of given arraylist in ascending order
    public static ArrayList<String> sort(ArrayList<String> unsorted) {
	ArrayList<String> sorted = new ArrayList<String>();

	for (String s : unsorted) {
	    if (!sorted.isEmpty()) {
		for (int i = 0; i < sorted.size(); i++) {
		    if (s.compareTo(sorted.get(i)) <= 0) {
			sorted.add(i, s);
			break;
		    }
		    else if (i+1 == sorted.size()) {
			sorted.add(s);
			break;
		    }
		}
	    }
	    else sorted.add(s);
	}

	return sorted;
    }
}
