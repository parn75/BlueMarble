package oldchat;

//����->������ �� �ֵ��� ���� ���

import java.net.*;//��Ʈ��ũ
import java.io.*;//�����
import java.util.Vector;//Ŭ���̾�Ʈ�� ���� ����

class Server {
	Vector<ServerThread> clients ; //Ŭ���̾�Ʈ�� ������ ����
	ServerSocket ss ; //�����ϱ����ؼ� 
	Socket socket ; //Ŭ���̾�Ʈ�� ���

	public static void main(String[] args){
		new Server() ; // 1)������ ȣ��
	}
	public Server(){
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
			//e.getMessage();
			e.printStackTrace();//���õ� ���� ���� ���
		} //catch
	}

	public void addThread(ServerThread  st){ //���Ϳ� �߰�
		clients.add(st);//Ŭ���̾�Ʈ�� ����		
		printSize("�߰�") ;
	}
	private void printSize(String msg){ //���� ����� ũ�⸦ ���
		System.out.println("���Ϳ� " + msg + "�˴ϴ�.");		
		System.out.println("���� ��� ũ�� : " + clients.size() );
		broadcast( "size|" + clients.size() ) ;
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
}

//Ŭ���̾�Ʈ�� ���->������ �ʿ�
class ServerThread extends Thread{
    Server server;//������ ����
	Socket socket ; //Ŭ���̾�Ʈ
	BufferedReader br;//��뷮���� �Է�
    PrintWriter pw;//��¿�
	String str;//������ ����
	String name;//��ȭ��

	public ServerThread(Server server, Socket socket){
		this.server = server ; //������ ����
		this.socket = socket ; //client

		try{
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream(), true);//autoFlush
		}catch (IOException ioe){
			ioe.getMessage();
		}
	}//������

	//Ŭ���̾�Ʈ���� �޼����� �������ִ� �޼ҵ�
	public void send(String str){
        pw.println(str);
	}
 
	//Ŭ���̾�Ʈ�κ��� �޼����� �޾�
	public void run(){
		try{
			pw.println("��ȭ���� �Է��ϼ���");
			name = br.readLine();//��ȭ��
			//�� client���� �������ִ� �޼ҵ�
			server.broadcast("[" + name + "]���� �����ϼ̽��ϴ�.");

			while((str=br.readLine())!=null){
				server.broadcast("["+name+"]"+str);
			}
		}catch (IOException ioe){
			server.broadcast("[" + name + "]���� �����̽��ϴ�.");
			System.out.println(socket.getInetAddress() + "�� ������ ����Ǿ����ϴ�.");
			server.removeThread( this ) ;
		}
	}
}