import java.util.*;
import java.io.*;
import java.nio.*;

public class TaskTwo {
    public static void main(String[] args) {
	
	String filename = "testdata/task2.test1.pcap";
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
		    int seq = TaskOne.get32BitVal(bytes, i+ihl+4);//bytes[i+ihl+4];
		    int ack = TaskOne.get32BitVal(bytes, i+ihl+8);
		    //System.out.println(seq); /////
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

	//TODO: fix error
	//some IPs not ending with port 80 are still not being matched to, so they
	// are still showing up in the ips arraylist
	//there's an issue with the number of bytes being sent over connection; math
	// should be right in here, but double check
	String connections = "";
	for (int y = 0; y < ips.size(); y++) {
	    String curr = ips.get(y);
	    
	    if (!curr.endsWith(" 80")) {
		String[] temp = curr.split(" ");
		String newIP = temp[2] + " " + temp[3] + " " + temp[0] + " " + temp[1];
		//System.out.println(newIP + " newIP"); /////

		for (int z = 0; z < ips.size(); z++) {
		    String correctIP = ips.get(z);

		    if (newIP.equals(correctIP)) {
		        int uplink = data.get(z).length;
			int downlink = data.get(y).length;

			connections += correctIP + " " + uplink + " " + downlink + "\n";
			//correctIP += " " +  uplink + " " + downlink;

			byte[] d = new byte[data.get(z).length +  data.get(y).length];
			for (int q = 0; q < d.length; q++) {
			    if (q < data.get(z).length){
				d[q] = data.get(z)[q];
			    }
			    else {
				d[q] = data.get(y)[q-data.get(z).length];
			    }
			}

			//ips.remove(z);
			//ips.add(z, correctIP);
			
			data.remove(z);
			data.add(z, d);

			ips.remove(y);
			data.remove(y);

			break;
		    }
		}
	    }
	}

	//makes connection string to be written to .out file
	
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

	    /*old code from calculated uplink, downlink data and making connections string
if (!curr.endsWith(" 80")) {
		String[] temp = curr.split(" ");
		String newIP = temp[2] + " " + temp[3] + " " + temp[0] + " " + temp[1];
		System.out.println(newIP + " newIP"); /////

		for (int z = 0; z < ips.size(); z++) {
		    String correctIP = ips.get(z);

		    if (newIP.equals(correctIP)) {
		        int uplink = data.get(z).length;
			int downlink = data.get(y).length;

			//connections += correctIP + " " + uplink + " " + downlink + "\n";
			correctIP += " " +  uplink + " " + downlink;

			byte[] d = new byte[data.get(z).length +  data.get(y).length];
			for (int q = 0; q < d.length; q++) {
			    if (q < data.get(z).length){
				d[q] = data.get(z)[q];
			    }
			    else {
				d[q] = data.get(y)[q-data.get(z).length];
			    }
			}

			ips.remove(z);
			ips.add(z, correctIP);
			
			data.remove(z);
			data.add(z, d);

			ips.remove(y);
			data.remove(y);

			break;
			}
		    
		}
		}*/

/*
if (ips.contains(newIP)) {
		if (temp[1].equals("80")) {
		    int indexNewIP = ips.indexOf(newIP);
		    int uplink = data.get(indexNewIP).length;
		    int downlink = data.get(y).length;
		    
		    String correctIP = newIP +  " " +  uplink + " " + downlink;
		    
		    byte[] d = new byte[uplink + downlink];
		    for (int q = 0; q < d.length; q++) {
			if (q < data.get(indexNewIP).length){
			    d[q] = data.get(indexNewIP)[q];
			}
			else {
			    d[q] = data.get(y)[q-data.get(indexNewIP).length];
			}
		    }
		    
		    ips.remove(indexNewIP);
		    ips.add(indexNewIP, correctIP);
		    
		    data.remove(indexNewIP);
		    data.add(indexNewIP, d);
		    
		    ips.remove(y);
		    data.remove(y);
		}
		else {
		    int indexWrongIP = ips.indexOf(newIP);
		    int uplink = data.get(y).length;
		    int downlink = data.get(indexWrongIP).length;
		    
		    String correctIP = curr +  " " +  uplink + " " + downlink + " x";
		    
		    byte[] d = new byte[uplink + downlink];
		    for (int q = 0; q < d.length; q++) {
			if (q < data.get(y).length){
			    d[q] = data.get(y)[q];
			}
			else {
			    d[q] = data.get(indexWrongIP)[q-data.get(y).length];
			}
		    }
		    
		    ips.remove(y);
		    ips.add(y, correctIP);
		    
		    data.remove(y);
		    data.add(y, d);
		    
		    ips.remove(indexWrongIP);
		    data.remove(indexWrongIP);
		}
	    }
	    else {
		System.out.println("bad"); /////
	    }
 */
