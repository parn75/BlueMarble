package backup11272;

//����->������ �� �ֵ��� ���� ���

import java.io.IOException;//�����
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;//��Ʈ��ũ
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import javax.swing.ImageIcon;
//Ŭ���̾�Ʈ�� ���� ����

class Server extends Thread{
	//Vector<ServerThread> clients ; //Ŭ���̾�Ʈ�� ������ ����
	ServerSocket ss ; //�����ϱ����ؼ� 
	Socket socket ; //Ŭ���̾�Ʈ�� ���
	Map<ServerThread, String> reverseClientMap = Collections.synchronizedMap(new HashMap<ServerThread, String>());
	Map<String, ServerThread> clientMap = Collections.synchronizedMap(new HashMap<String, ServerThread>());
	
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
				ServerThread st = new ServerThread(this, socket);
				st.start() ; //�����尡 ����				
			}			
		}catch(IOException e){ 			
			e.printStackTrace();//���õ� ���� ���� ���
		} //catch
	}

	public void removeThread(String key){ //������ �� ����Ʈ����	
		clientMap.remove(key);
		ServerUI.listModel.removeElement(key);
	}	
	
	public void broadcast(String str){  //��ü �޼��� ������
		Set<String> s = clientMap.keySet();
		String[] clist = new String[s.size()]; 
		s.toArray(clist);		
		for(int i=0;i<clist.length;i++) {
			clientMap.get(clist[i]).send(str);
		}
		ServerUI.txtarLog.append(str + "\n");
	}
	
	public void sendConnectorList(){  //������ ����Ʈ ������
		String[] str= new String[ServerUI.listModel.size()];
		ServerUI.listModel.copyInto(str);		
		Set<String> s = clientMap.keySet();
		String[] clist = new String[s.size()]; 
		s.toArray(clist);		
		for(int i=0;i<clist.length;i++) {
			clientMap.get(clist[i]).send(str);
		}
	}
	
	/**	 
	 * @param str[] ���� Ŭ���̾�Ʈ ���
	 * @param obj  ���� ���� (������ ��� String ��ü)
	 * @param from �޼����� ���� ���
	 */
	public void send(String[] str, Object obj, String from) {  //�ӼӸ��� ���
		
		for(int i=0;i<str.length;i++){
			if(clientMap.containsKey(str[i])) {
				String msg = (String)obj;
				msg = "from " + from + ":" + msg;
				clientMap.get(str[i]).send(msg);
			}
		}
	}	
	
}

//�ֱ������� ������ ����Ʈ�� Ŭ���̾�Ʈ�� �Ѹ��� ���� ������
class SendConnectorList extends TimerTask {
	Server server;
	public SendConnectorList(Server server) {
		this.server = server;
	}
	
	@Override
	public void run() {
		String[] str= new String[ServerUI.listModel.size()];
		ServerUI.listModel.copyInto(str);
		Set<String> s = server.clientMap.keySet();
		String[] clist = new String[s.size()]; 
		s.toArray(clist);
		for(int i=0;i<clist.length;i++) {
			server.clientMap.get(clist[i]).send(str);			
		}
	}
}

//Ŭ���̾�Ʈ�� ���->������ �ʿ�
class ServerThread extends Thread{
    Server server;//������ ����
	Socket socket ; //Ŭ���̾�Ʈ
	String str;//������ ����
	String name;//��ȭ��
	ObjectOutputStream oos;
	ObjectInputStream ois;

	public ServerThread(Server server, Socket socket){
		this.server = server ; //������ ����
		this.socket = socket ; //client

		try{
		    oos = new ObjectOutputStream(socket.getOutputStream());
		    ois = new ObjectInputStream(socket.getInputStream());
		}catch (IOException ioe){
			ioe.getMessage();
		}
	}//������

	//Ŭ���̾�Ʈ���� ��ü�� �������ִ� �޼ҵ�	(�ؽ�Ʈ�� ��쿡�� String ��ü�� ����)
	public void send(Object obj) {
		try {
			oos.writeObject(obj);
			oos.flush();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void toSelected(Object obj) { //����Ʈ���� ���õ� Ŭ���̾�Ʈ���Ը� �޼��� ����
		ChatData chatdata = (ChatData)obj;		
		if(chatdata!=null && chatdata.to.length>0) {   //chatdata�� to �ʵ忡 ���𰡰� ���� ��� => Ŭ���̾�Ʈ���� �޼��� ���۽� ����Ʈ���� ���� ����� ���� => �� �ӼӸ��� ���
			for(int i=0;i<chatdata.to.length;i++) {
				if(chatdata.to[i].equals("To All")) {	//To ALL�� ���õǾ� �ִ� ��� ��ü �޼��� ���� �� ��������		
					server.broadcast(this.getName() + " : " + chatdata.data);
					return;
				}
			}
			server.send(chatdata.to, chatdata.data, this.getName());
		}
		else{  //��ü �޼���
			System.out.println("sending broad");
			server.broadcast(this.getName() + " : " + chatdata.data);			
		}	
	}
 
	//Ŭ���̾�Ʈ�κ��� �޼����� �޴� ������
	@Override
	public void run(){
		try{
			Object obj=null;
			while((obj=ois.readObject())!=null) {
				if(obj instanceof String) { //ù ���ӿ��� String ��ü ���
					String str = (String)obj;
					if (str.startsWith("*****[")) {  //Ŭ���̾�Ʈ���� ���� ���ӽ� ������ ��� *****[ID:REALID]***** 
						String ID = str.substring(9, str.lastIndexOf("]"));  //<= REALID �κ��� �и��Ͽ� ID�� ����
						ServerUI.listModel.addElement(ID);  //����Ʈ�� ID�߰�
						this.setName(ID); //Thread�� �̸��� ID�� ���� => ID�ߺ� �Ұ�
						server.clientMap.put(ID, this);  
						server.reverseClientMap.put(this, ID);
						server.sendConnectorList(); //�� Ŭ���̾�Ʈ�� ���� ������ ����Ʈ ����
						server.broadcast("["+ID+"]���� �����ϼ̽��ϴ�.");
					}else {
						ServerUI.txtarLog.append(this.getName() +" : " + (String)obj + "\n");
					}
				}else if(obj instanceof ChatData) { //�ӼӸ��� ��� ChatData ��ü�� �Ѿ��
					toSelected(obj);
				}else if(obj instanceof ImageIcon) {
					System.out.println("Got ImageIcon");
					ImageIcon img = (ImageIcon)obj;
					ServerUI.btnBroadcast.setIcon(img); //�̹����� ä��â�� ǥ���ϱ� ���ؼ��� TextArea�� JEditorPane or JTextPane���� ���� �ʿ�?
				}
			}				
		}catch (IOException | ClassNotFoundException e){			
			server.removeThread( this.getName() ); //������ ���� ���(���� �� ���� ���) ������ �� ����Ʈ ����
			server.broadcast("[" + this.getName() + "]���� �����̽��ϴ�.");
			System.out.println(socket.getInetAddress() + "�� ������ ����Ǿ����ϴ�.");
		
		}
		
	}
}