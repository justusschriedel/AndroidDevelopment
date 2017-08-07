
public class TCPConnection { //<SourceIP, SourcePort, DestIP, DestPort> {
    int sourceIP, sourcePort, destIP, destPort;
    
    public TCPConnection(int sourceIP, int sourcePort, int destIP, int destPort) {
	this.sourceIP = sourceIP;
	this.sourcePort = sourcePort;
	this.destIP = destIP;
	this.destPort = destPort;
    }

    public int getSourceIP() {
	return this.sourceIP;
    }

    public int getSourcePort() {
	return this.sourcePort;
    }

    public int getDestIP() {
	return this.destIP;
    }

    public int getDestPort() {
	return this.destPort;
    }
}
