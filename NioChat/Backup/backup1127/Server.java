package backup1127;

//서버->접속할 수 있도록 무한 대기

import java.io.IOException;//입출력
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;//네트워크
import java.net.Socket;
import java.util.Vector;//클라이언트의 정보 저장

class Server extends Thread{
	Vector<ServerThread> clients ; //클라이언트의 정보를 저장
	ServerSocket ss ; //접속하기위해서 
	Socket socket ; //클라이언트와 통신

	public static void main(String[] args){
		new Server() ; // 1)생성자 호출
	}
	
	@Override
	public void run(){
		//client와의 접속 상태를 체크->처리
		clients = new Vector<ServerThread>();

		try{
			ss = new ServerSocket(5432);
			System.out.println("---채팅 서버 가동중(즐 채팅하세요^^)---");

			while(true){
				//accept() 메소드 : 블로킹 메소드(무엇인가를 대기하는 메소드)
				socket = ss.accept(); //클라이언트의 요청이 들어 오면 소켓이 만들어 진다.
				System.out.println("Accepted from " + socket);

				//쓰레드를 가동시키면서 접속한 client 정보
				ServerThread st = new ServerThread(this, socket);
				addThread( st ) ; //클라이언트에서 누군가 접속이 되면 벡터에 저장
				st.start() ; //쓰레드가 가동				
			}			
		}catch(IOException e){ 			
			e.printStackTrace();//관련된 에러 전부 출력
		} //catch
	}

	public void addThread(ServerThread  st){ //벡터에 추가
		clients.add(st);//클라이언트의 정보		
		printSize("추가");
	}
	private void printSize(String msg){ //벡터 요소의 크기를 출력
		System.out.println("벡터에 " + msg + "됩니다.");		
		System.out.println("벡터 요소 크기 : " + clients.size() );
		broadcast( "size|" + clients.size() );
	}
	public void removeThread(ServerThread st){ //삭제
		clients.remove(st);//탈퇴
		printSize("삭제") ;
	}	
	public void broadcast(String str){ //대화명, 각 client에게 전송 
		for(int i = 0; i < clients.size() ; i++){
			ServerThread st = (ServerThread)clients.get(i);
			st.send(str);//접속한 client에게
		}
	}
	
	public void send(Object obj){ 
		for(int i = 0; i < clients.size() ; i++){
			ServerThread st = (ServerThread)clients.get(i);
			st.send(obj);//접속한 client에게
		}
	}
	
	
	
}

//클라이언트와 통신->쓰레드 필요
class ServerThread extends Thread{
    Server server;//서버의 정보
	Socket socket ; //클라이언트
	String str;//전달할 문자
	String name;//대화명
	ObjectOutputStream oos;
	ObjectInputStream ois;

	public ServerThread(Server server, Socket socket){
		this.server = server ; //서버의 정보
		this.socket = socket ; //client

		try{
		    oos = new ObjectOutputStream(socket.getOutputStream());
		    ois = new ObjectInputStream(socket.getInputStream());
		}catch (IOException ioe){
			ioe.getMessage();
		}
	}//생성자

	//클라이언트에게 메세지를 전달해주는 메소드	
	public void send(Object obj) {
		try {
			oos.writeObject(obj);
			oos.flush();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
 
	//클라이언트로부터 메세지를 받아
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
			server.broadcast("[" + name + "]님이 나가셨습니다.");
			System.out.println(socket.getInetAddress() + "의 연결이 종료되었습니다.");
			server.removeThread( this );
		}
		
	}
}