package backup1126_beforesendingObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

public class NChatClient {

	SocketChannel client;
	final String ID = "Test Client";
	Selector selector;
	SelectionKey clientKey;
	Set keys;
	SocketChannel channel;
	Iterator i;

	private void connect() throws IOException{ 
		// Create client SocketChannel
		client = SocketChannel.open();

		// nonblocking I/O
		client.configureBlocking(false);

		// Connection to host port 8000
		client.connect(new java.net.InetSocketAddress("localhost",8000));

		// Create selector
		selector = Selector.open();

		// Record to selector (OP_CONNECT type)
		clientKey = client.register(selector, SelectionKey.OP_CONNECT|SelectionKey.OP_READ);

		// Waiting for the connection
		while (selector.select(500)> 0) {

			// Get keys
			keys = selector.selectedKeys();
			i = keys.iterator();

			// For each key...
			while (i.hasNext()) {
				SelectionKey key = (SelectionKey)i.next();

				// Remove the current key
				i.remove();

				// Get the socket channel held by the key
				channel = (SocketChannel)key.channel();

				// Attempt a connection
				if (key.isConnectable()) {

					// Connection OK
					System.out.println("Server Found");

					// Close pendent connections
					if (channel.isConnectionPending()) {
						if (channel.finishConnect()) {
							System.out.println("Successfully connected on server");
							send("*****["+ClientUI.fldID.getText()+"]*****");
						}
					}
				}
			}
		}

	}

	public void send(String str) throws IOException {

		// Write continuously on the buffer
		ByteBuffer buffer = null;
		buffer = ByteBuffer.wrap(str.getBytes());
		channel.write(buffer);
		buffer.clear();

	}

	public NChatClient(){
		try {
			connect();
			//send("test message\n");			
			//send("test message3\n");
			
			ClientThread ct = new ClientThread(selector,keys ,clientKey, i);
			ct.start();
			
		} catch (IOException e) {		
			e.printStackTrace();
		} 
	}

	public static void main(String[] args) {
		NChatClient nc = new NChatClient();
	}
}

class ClientThread extends Thread {
	
	Selector selector;	
	Set keys;
	SelectionKey key;
	Iterator i;
	//SocketChannel channel;
	//SocketChannel client;	
	

	public ClientThread(Selector selector,Set keys , SelectionKey key, Iterator i) {
		this.selector = selector;
		this.keys = keys;
		this.key = key;
		this.i = i;
	}

	@Override
	public void run() {
		for(;;) {
			// Waiting for events
			try {
				selector.select();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// if isReadable = true
			// then the server is ready to read 
			if (key.isReadable()) {
				//System.out.println("key is readable.");
				SocketChannel client = (SocketChannel) key.channel();					

				// Read byte coming from the client
				int BUFFER_SIZE = 32;
				ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
				try {
					client.read(buffer);
				}
				catch (Exception e) {
					// client is no longer active
					//e.printStackTrace();
					continue;
				}

				// Show bytes on the console
				buffer.flip();				
				Charset charset=Charset.defaultCharset();
				CharsetDecoder decoder = charset.newDecoder();
				CharBuffer charBuffer=null;
				try {
					charBuffer = decoder.decode(buffer);
				} catch (CharacterCodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.print(charBuffer.toString());
				try {
					ClientUI.txtChat.append(client.getLocalAddress()+charBuffer.toString()+"\n");
				} catch (IOException e) {						
					e.printStackTrace();
				}
				continue;
			}	

		}
	}
}
