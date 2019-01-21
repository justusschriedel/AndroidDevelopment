import java.util.*;
import java.io.*;
import java.nio.*;

public class TaskTwo {
    public static void main(String[] args) {
	
	String filename = "test3.pcap";
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
		    //System.out.println(dataOffset); /////
		    int srcPort = TaskOne.get16BitVal(bytes, i+ihl);
		    int destPort = TaskOne.get16BitVal(bytes, i+ihl+2);
		    int seq = TaskOne.get32BitVal(bytes, i+ihl+4); //bytes[i+ihl+4];
		    int ack = TaskOne.get32BitVal(bytes, i+ihl+8);
		    //System.out.println(seq + "\n"); /////
		    //System.out.println(ack); /////
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

			//TODO: fix error in here somewhere
			//potentially error with storing data
			//there's an error with the number of bytes being sent over the connection;
			//  could be from storing incorrectly in hash table
			String tcpConn = srcIP + " " + srcPort + " " + destIP +  " " + destPort;
			if(seqNums.containsKey(seq)) {
			    if(seqNums.get(seq).getFirst().equals(tcpConn)) {
				byte[] oldData = seqNums.get(seq).getSecond();
				byte[] temp = new byte[data.length + oldData.length];

				for (int q = 0; q < temp.length; q++) {
				    if (q < oldData.length) {
					temp[q] = oldData[q];
				    }
				    else {
					int index = q - oldData.length;
					temp[index] = data[index];
					seqNums.put(seq, new Pair<String, byte[]>(tcpConn, temp));
				    }
				}
			    }
			    else {
				boolean inMap = false; int p = 1;

				while(!inMap) {
				    if(!seqNums.containsKey(seq+p)) {
					seqNums.put(seq+p, new Pair<String, byte[]>(tcpConn, data));
					inMap = true;
				    }
				    p++;
				}
			    }
			}
			else {
			    seqNums.put(seq, new Pair<String, byte[]>(tcpConn, data));
			}
		    } 
		}

		/*Object[] s = seqNums.keySet().toArray();
		for (Object x : s) {
		    System.out.println(x); /////
		    }*/
	

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
	    System.out.println(x); /////
	    
	    Pair<String, byte[]> pair = seqNums.get(x);
	    String ip = pair.getFirst();
	    byte[] datum = pair.getSecond();
	    //System.out.println(ip); /////

	    if (!ips.contains(ip)) {
		ips.add(ip);

		ArrayList<String> temp = sortIPs(ips);

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

	System.out.println(ips); /////

	//TODO: fix error
	//IPs aren't being matched correctly: some aren't being matched to
	//there's an issue with the number of bytes being sent over connection
	
	for (int y = 0; y < ips.size(); y++) {
	    String connection = "";
	    String curr = ips.get(y);
	    String[] s = curr.split(" ");
	    String newIP = s[2] + " " + s[3] + " " + s[0] + " " + s[1];

	    if (!curr.endsWith("80") && ips.contains(newIP)) {
		int index = ips.indexOf(newIP);
		int uplink = data.get(index).length;
		int downlink = data.get(y).length;
		byte[] d = new byte[uplink + downlink];

		for (int r = 0; r < d.length; r++) {
		    if (r < uplink) {
			d[r] = data.get(index)[r];
		    }
		    else {
			d[r] = data.get(y)[r-uplink];
		    }
		}

		connection = newIP + " " + uplink + " " + downlink + "\n";

		ips.remove(index);
		ips.add(index, connection);
		
		data.remove(index);
		data.add(index, d);
		
		ips.remove(y);
		data.remove(y);

	    }
	    else if (!curr.endsWith("80") && !curr.endsWith("\n")) {
		int downlink = data.get(y).length;
		ips.remove(y);
		ips.add(y, newIP);

		ArrayList<String> resorted = sortIPs(ips);

		ips.removeAll(ips);
		ips.addAll(resorted);

		connection = newIP + " 0 " + downlink + "\n";
		int index = ips.indexOf(newIP);
		byte[] d = data.get(y);

		ips.remove(index);
		ips.add(index, connection);
		
		data.remove(y);
		data.add(index, d);
	    }
	    else {
		if (!curr.endsWith("\n")) {
		    int uplink = data.get(y).length;

		    connection = curr + " " + uplink + " 0\n";

		    ips.remove(y);
		    ips.add(y, connection);
		}
	    }
	}

	System.out.println(ips);

	//makes connection string to be written to .out file
	String connections = "";
	for (int g = 0; g < ips.size(); g++) {
	    connections += ips.get(g);
	}
	
	//could be used to fix up leftover ips not matched to in
	// above loop, but probably should not be
	/*for (int g = 0; g < ips.size(); g++) {
	    String curr = ips.get(g);

	    String[] temp = curr.split(" ");
	    String newIP = temp[2] + " " + temp[3] + " " + temp[0] + " " + temp[1];

	    if (ips.contains(newIP)) {
		System.out.println(true);
	    }
	    
	    connections += ips.get(g) + "\n";
	    }*/
	
	System.out.println(connections); /////
	
	try {
	    outputFile = new FileOutputStream("task2_test1.out");
	    outputFile.write(connections.getBytes());
	    
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
    }

    //sorts elements of given arraylist in ascending order
    // TODO: fix error
    // could be error with sorting method causing ips to get out of order
    public static ArrayList<String> sortIPs(ArrayList<String> unsorted) {
	ArrayList<String> sorted = new ArrayList<String>();

	for (String s : unsorted) {
	    String[] newIP = s.split(" ");
	    if (!sorted.isEmpty()) {
		for (int i = 0; i < sorted.size(); i++) {
		    String[] sortedIP = sorted.get(i).split(" ");

		    //System.out.println(newIP[1] + " " + sortedIP[1]); /////
		    
		    if (Integer.valueOf(newIP[1]).compareTo(Integer.valueOf(sortedIP[1])) <= 0) {
			sorted.add(i, s);
			break;
		    }
		    /* else if (Integer.valueOf(newIP[1]).compareTo(Integer.valueOf(sortedIP[1])) == 0){
			if (Integer.valueOf(newIP[3]).compareTo(Integer.valueOf(sortedIP[3])) < 0) {
			    sorted.add(i, s);
			    break;
			}
			
			}*/
		    else if (i+1 == sorted.size()) {
			sorted.add(s);
			break;
		    }
		}
	    }
	    else sorted.add(s);
	}

	//System.out.println(sorted);
	
	return sorted;
    }
}
