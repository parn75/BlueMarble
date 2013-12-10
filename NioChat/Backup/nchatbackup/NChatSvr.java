package nchatbackup;

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
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class NChatSvr extends Thread{
	ServerSocketChannel server;
	Selector selector;
	Vector<SocketChannel> chVector = new Vector<SocketChannel>();

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
						chVector.add(client);
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
					//Charset charset=Charset.forName("±¼¸²Ã¼");
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
						ServerUI.txtarLog.append(client.getLocalAddress()+charBuffer.toString());
					} catch (IOException e) {						
						e.printStackTrace();
					}
					continue;
				}
			}
		}
	}
	
	public void broadcast(String str) {
		
	}
	
	public void send(String str) throws IOException {
		// Write continuously on the buffer
		ByteBuffer buffer = null;
		buffer = ByteBuffer.wrap(str.getBytes());
		for(int i=0;i<chVector.size();i++) {
			SocketChannel ch = chVector.get(i);
			System.out.println("IsConnected: " + ch.isConnected());
			System.out.println(ch.getLocalAddress());
			//channel.write(buffer);
			int written = chVector.get(i).write(buffer);
			System.out.println("Written: " + written);
			buffer.rewind();
		}
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
