package backup1126_beforesendingObject;

import java.nio.channels.SocketChannel;

public class Client_Info {
	SocketChannel client;
	String ID;
	int clientNo;
	
	public Client_Info(SocketChannel client, String ID, int clientNo ) {
		this.client = client;
		this.ID = ID;
		this.clientNo = clientNo;
	}
	
}
