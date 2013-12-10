package backup1126_beforesendingObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NChatSvr extends Thread{
	ServerSocketChannel server;
	Selector selector;
	//Vector<SocketChannel> chVector = new Vector<SocketChannel>();	
	static Map<SocketChannel, String> clientMap = Collections.synchronizedMap(new HashMap<SocketChannel, String>());
	static Map<String, SocketChannel> reverseClientMap = Collections.synchronizedMap(new HashMap<String, SocketChannel>());
	
	private void prepareSvr() throws ClosedChannelException, IOException {
		// Create the server socket channel
		server = ServerSocketChannel.open();
		// nonblocking I/O
		server.configureBlocking(false);
		// host-port 8000
		server.socket().bind(new java.net.InetSocketAddress("localhost",8000));
		System.out.println("Server has been initiated");
		// Create the selector
		selector = Selector.open();
		// Recording server to selector (type OP_ACCEPT)
		server.register(selector,SelectionKey.OP_ACCEPT);
	}

	@Override
	public void run() {
		// Infinite server loop
		for(;;) {
			// Waiting for events
			try {
				selector.select();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// Get keys
			Set keys = selector.selectedKeys();
			Iterator i = keys.iterator();

			// For each keys...
			while(i.hasNext()) {
				SelectionKey key = (SelectionKey) i.next();

				// Remove the current key
				i.remove();

				// if isAccetable = true
				// then a client required a connection
				if (key.isAcceptable()) {
					// get client socket channel
					SocketChannel client;
					try {
						client = server.accept();
						// Non Blocking I/O
						client.configureBlocking(false);
						// recording to the selector (reading)
						client.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
						//chVector.add(client);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					continue;
				}

				// if isReadable = true
				// then the server is ready to read 
				if (key.isReadable()) {

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
					//Charset charset=Charset.forName("±º∏≤√º");
					Charset charset=Charset.defaultCharset();
					CharsetDecoder decoder = charset.newDecoder();
					CharBuffer charBuffer=null;
					try {
						charBuffer = decoder.decode(buffer);
					} catch (CharacterCodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String msg = charBuffer.toString();				
					if (msg.startsWith("*****[")) {
						String ID = null;
						ID = msg.substring(6, msg.lastIndexOf("]"));
						if(ID!=null) ServerUI.txtarLog.append(ID+"¥‘¿Ã ¡¢º”«œºÃΩ¿¥œ¥Ÿ.\n");
						ServerUI.listModel.addElement(ID);
						//cVector.add(new Client_Info(client, ID, clientIndex++));
						clientMap.put(client, ID);
						reverseClientMap.put(ID, client);
						/*
						ObjectByteEx obe = new ObjectByteEx();
						byte[] byteMap = obe.toByteArray(clientMap);
						try {
							sendObject(client,byteMap);
						} catch (IOException e) {							
							e.printStackTrace();
						}
						*/
						
					}else {
						ServerUI.txtarLog.append(clientMap.get(client) + ":" +charBuffer.toString());
						
					}
					continue;
				}
			}
		}
	}
	
	public void broadcast(String str) throws IOException{
		Set<SocketChannel> set = clientMap.keySet();
		Iterator<SocketChannel> i = set.iterator();
		ByteBuffer buffer = null;
		buffer = ByteBuffer.wrap(str.getBytes());
		
		while(i.hasNext()) {
			SocketChannel ch = i.next();
			ch.write(buffer);
			buffer.rewind();			
		}
		
		byte[] bt = buffer.array();
		String msg = new String(bt);
		ServerUI.txtarLog.append(msg + "\n");
		buffer.clear();	
	}
	
	public void send(List<String> selected, String str) throws IOException {
		ByteBuffer buffer = null;
		buffer = ByteBuffer.wrap(str.getBytes());
		for(int i=0;i<selected.size();i++) {
			if(clientMap.containsValue(selected.get(i))) reverseClientMap.get(selected.get(i)).write(buffer);			
			buffer.rewind();			
		}	
		buffer.clear();
	}
	
	public void sendObject(SocketChannel client, byte[] byteObject) throws IOException {
		ByteBuffer buffer = null;
		buffer = ByteBuffer.wrap(byteObject);		
		client.write(buffer);			
		buffer.rewind();
		buffer.clear();
	}
	
	public NChatSvr() {
		try {
			prepareSvr();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public static void main(String[] args) {
		NChatSvr ns = new NChatSvr();

	}

}
