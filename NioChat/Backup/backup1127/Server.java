package backup1127;

//����->������ �� �ֵ��� ���� ���

import java.io.IOException;//�����
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;//��Ʈ��ũ
import java.net.Socket;
import java.util.Vector;//Ŭ���̾�Ʈ�� ���� ����

class Server extends Thread{
	Vector<ServerThread> clients ; //Ŭ���̾�Ʈ�� ������ ����
	ServerSocket ss ; //�����ϱ����ؼ� 
	Socket socket ; //Ŭ���̾�Ʈ�� ���

	public static void main(String[] args){
		new Server() ; // 1)������ ȣ��
	}
	
	@Override
	public void run(){
		//client���� ���� ���¸� üũ->ó��
		clients = new Vector<ServerThread>();

		try{
			ss = new ServerSocket(5432);
			System.out.println("---ä�� ���� ������(�� ä���ϼ���^^)---");

			while(true){
				//accept() �޼ҵ� : ���ŷ �޼ҵ�(�����ΰ��� ����ϴ� �޼ҵ�)
				socket = ss.accept(); //Ŭ���̾�Ʈ�� ��û�� ��� ���� ������ ����� ����.
				System.out.println("Accepted from " + socket);

				//�����带 ������Ű�鼭 ������ client ����
				ServerThread st = new ServerThread(this, socket);
				addThread( st ) ; //Ŭ���̾�Ʈ���� ������ ������ �Ǹ� ���Ϳ� ����
				st.start() ; //�����尡 ����				
			}			
		}catch(IOException e){ 			
			e.printStackTrace();//���õ� ���� ���� ���
		} //catch
	}

	public void addThread(ServerThread  st){ //���Ϳ� �߰�
		clients.add(st);//Ŭ���̾�Ʈ�� ����		
		printSize("�߰�");
	}
	private void printSize(String msg){ //���� ����� ũ�⸦ ���
		System.out.println("���Ϳ� " + msg + "�˴ϴ�.");		
		System.out.println("���� ��� ũ�� : " + clients.size() );
		broadcast( "size|" + clients.size() );
	}
	public void removeThread(ServerThread st){ //����
		clients.remove(st);//Ż��
		printSize("����") ;
	}	
	public void broadcast(String str){ //��ȭ��, �� client���� ���� 
		for(int i = 0; i < clients.size() ; i++){
			ServerThread st = (ServerThread)clients.get(i);
			st.send(str);//������ client����
		}
	}
	
	public void send(Object obj){ 
		for(int i = 0; i < clients.size() ; i++){
			ServerThread st = (ServerThread)clients.get(i);
			st.send(obj);//������ client����
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

	//Ŭ���̾�Ʈ���� �޼����� �������ִ� �޼ҵ�	
	public void send(Object obj) {
		try {
			oos.writeObject(obj);
			oos.flush();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
 
	//Ŭ���̾�Ʈ�κ��� �޼����� �޾�
	@Override
	public void run(){
		try{
			Object obj=null;
			while((obj=ois.readObject())!=null) {
				if(obj instanceof String) {
					String str = (String)obj;
					if (str.startsWith("*****[")) {
						String ID = str.substring(9, str.lastIndexOf("]"));
						ServerUI.listModel.addElement(ID);
						this.setName(ID);
					}else {
						ServerUI.txtarLog.append((String)obj);
					}
				}
				System.out.println("Server Run is running");
			}				
		}catch (IOException | ClassNotFoundException e){
			server.broadcast("[" + name + "]���� �����̽��ϴ�.");
			System.out.println(socket.getInetAddress() + "�� ������ ����Ǿ����ϴ�.");
			server.removeThread( this );
		}
		
	}
}