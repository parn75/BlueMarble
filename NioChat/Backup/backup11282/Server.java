package backup11282;

//����->������ �� �ֵ��� ���� ���

import java.io.IOException;//�����
import java.net.ServerSocket;//��Ʈ��ũ
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.text.BadLocationException;
//Ŭ���̾�Ʈ�� ���� ����

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
		clientMap.remove(key);
		serverUI.listModel.removeElement(key);
	}	
	
//	public void broadcast(String str){  //��ü �޼��� ������
	public void broadcast(Object obj){  //��ü �޼��� ������
		Set<String> s = clientMap.keySet();
		String[] clist = new String[s.size()]; 
		s.toArray(clist);		
		for(int i=0;i<clist.length;i++) {
			clientMap.get(clist[i]).send(obj);
		}

		try {
			serverUI.doc.insertString(serverUI.doc.getLength(), obj + "\n", serverUI.sc.getStyle("MainSytle"));
			serverUI.sp.getVerticalScrollBar().setValue(serverUI.sp.getVerticalScrollBar().getMaximum()); //��ũ���� ���� �Ʒ��� 	
		} catch (BadLocationException e) {			
			e.printStackTrace();
		}
		
	}

	/**	 
	 * @param str[] ���� Ŭ���̾�Ʈ ���
	 * @param obj  ���� ���� (������ ��� String ��ü)
	 * @param from �޼����� ���� ���
	 */
	public void send(String[] str, Object obj, String from) {  //���õ� ��ܿ� �޼���(��ü) ����
		
		for(int i=0;i<str.length;i++){
			if(clientMap.containsKey(str[i])) {
				String msg = (String)obj;
				msg = "from " + from + ":" + msg;
				clientMap.get(str[i]).send(msg);
			}
		}
	}	
	
	public void sendConnectorList(){  //������ ����Ʈ ������, Ŭ���̾�Ʈ ���� ���ӽ� �ѹ����� �����. ���Ŀ��� �ֱ������� ����Ʈ ����.
		String[] str= new String[serverUI.listModel.size()];
		serverUI.listModel.copyInto(str);		
		Set<String> s = clientMap.keySet();
		String[] clist = new String[s.size()]; 
		s.toArray(clist);		
		for(int i=0;i<clist.length;i++) {
			clientMap.get(clist[i]).send(str);
		}
	}
	
}


