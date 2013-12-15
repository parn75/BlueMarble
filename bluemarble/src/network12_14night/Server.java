package network12_14night;

//���� �� ������ ���� Ŭ����

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;



public class Server extends Thread{
	ServerUI serverUI;
	ServerSocket ss ; //�����ϱ����ؼ� 
	Socket socket ; //Ŭ���̾�Ʈ�� ���
	Map<ServerThread, String> reverseClientMap = Collections.synchronizedMap(new HashMap<ServerThread, String>());
	Map<String, ServerThread> clientMap = Collections.synchronizedMap(new HashMap<String, ServerThread>());
	
	
	public Server(ServerUI serverUI) {
		this.serverUI = serverUI;		
	}
	
	@Override
	public void run(){
		//client���� ���� ���¸� üũ->ó��
		try{
			ss = new ServerSocket(5432);
			System.out.println("---ä�� ���� ������(�� ä���ϼ���^^)---");

			while(true){
				//accept() �޼ҵ� : ���ŷ �޼ҵ�(�����ΰ��� ����ϴ� �޼ҵ�)
				socket = ss.accept(); //Ŭ���̾�Ʈ�� ��û�� ��� ���� ������ ����� ����.
				System.out.println("Accepted from " + socket);

				//�����带 ������Ű�鼭 ������ client ����
				ServerThread st = new ServerThread(this, socket, serverUI);
				st.start() ; //�����尡 ����				
			}			
		}catch(IOException e){ 			
			e.printStackTrace();//���õ� ���� ���� ���
		} //catch
	}

	public void removeThread(String key){ //������ �� ����Ʈ����	
		clientMap.get(key).interrupt();
		clientMap.remove(key);
		serverUI.listModel.removeElement(key);
	}
	
	public void showImage(ChatData cd){
		System.out.println("Got ImageIcon");
		ImageIcon icon = (ImageIcon)cd.data; 
	    StyleConstants.setIcon(serverUI.chatStyle.imageStyle, icon);
	    
	    try {
	    	//StyledEditorKit sek; //�̹��� ũ�⸦ �����ϱ����ؼ��� ������Ŷ�� �̿��ؾ� �ҵ�	
	    	serverUI.doc.insertString(serverUI.doc.getLength(), "Image from: " + cd.from  + "\n", serverUI.chatStyle.mainStyle);
	    	serverUI.doc.insertString(serverUI.doc.getLength(), "ignored text\n", serverUI.chatStyle.imageStyle);	    	   	
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}	    
	    serverUI.endScroll();		
	}
	
//	public void broadcast(String str){  //��ü �޼��� ������
	public void broadcast(ChatData cd){  //��ü �޼��� ������
		Set<String> s = clientMap.keySet();
		String[] clist = new String[s.size()]; 
		s.toArray(clist);
		for(int i=0;i<clist.length;i++) {
			synchronized(clientMap) {
				clientMap.get(clist[i]).send(cd);
			}
		}
		try {
			if (cd.type == ChatType.Image) showImage(cd);
			else if (cd.type == ChatType.RoomStatus) return;
			else if (cd.type == ChatType.Broadcast || cd.type == ChatType.Whisper) serverUI.doc.insertString(serverUI.doc.getLength(), cd.from + (String)cd.data + "\n", serverUI.sc.getStyle("MainSytle"));
			serverUI.endScroll();
		} catch (BadLocationException e) {			
			e.printStackTrace();
		}
		
	}

	/**	 
	 * @param str[] ���� Ŭ���̾�Ʈ ���
	 * @param cd  ���� ���� (������ ��� String ��ü)
	 * @param from �޼����� ���� ���
	 */
	synchronized public void send(ChatData cd) {  //���õ� ��ܿ� �޼���(��ü) ����, �������� ���۽ÿ��� ChatData�� to������ null�̸� �ȵȴ�.
		
		for(int i=0;i<cd.to.length;i++){			
			if(clientMap.containsKey(cd.to[i])) {
				if(!cd.to[i].equals(cd.from ))clientMap.get(cd.to[i]).send(cd);			
			}
		}
	}
	
	synchronized public void send(ChatData cd, ServerThread st) {  //�������� ���Ӱ� ��ġ�ϴ� Ŭ���̾�Ʈ�� ����
		String[] to = new String[1];
		to[0] = st.getName();
		cd.setTo(to);
		for(int i=0;i<cd.to.length;i++){			
			if(clientMap.containsKey(cd.to[i])) {
				if(!cd.to[i].equals(cd.from ))clientMap.get(cd.to[i]).send(cd);			
			}
		}
	}
	
	public void sendConnectorList(){  //������ ����Ʈ ������, Ŭ���̾�Ʈ ���� ���ӽ� �ѹ����� �����. ���Ŀ��� �ֱ������� ����Ʈ ����.		
		String[] str= new String[serverUI.listModel.size()];
		serverUI.listModel.copyInto(str);
		Set<String> s = clientMap.keySet();
		String[] clist = new String[s.size()]; 
		s.toArray(clist);
		ChatData cd = new ChatData(ChatType.ConnectorList, str);
		for(int i=0;i<clist.length;i++) {
			clientMap.get(clist[i]).send(cd);			
		}
	}
	
}


