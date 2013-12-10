package backup11272;

//서버->접속할 수 있도록 무한 대기

import java.io.IOException;//입출력
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;//네트워크
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import javax.swing.ImageIcon;
//클라이언트의 정보 저장

class Server extends Thread{
	//Vector<ServerThread> clients ; //클라이언트의 정보를 저장
	ServerSocket ss ; //접속하기위해서 
	Socket socket ; //클라이언트와 통신
	Map<ServerThread, String> reverseClientMap = Collections.synchronizedMap(new HashMap<ServerThread, String>());
	Map<String, ServerThread> clientMap = Collections.synchronizedMap(new HashMap<String, ServerThread>());
	
	@Override
	public void run(){
		//client와의 접속 상태를 체크->처리
		try{
			ss = new ServerSocket(5432);
			System.out.println("---채팅 서버 가동중(즐 채팅하세요^^)---");

			while(true){
				//accept() 메소드 : 블로킹 메소드(무엇인가를 대기하는 메소드)
				socket = ss.accept(); //클라이언트의 요청이 들어 오면 소켓이 만들어 진다.
				System.out.println("Accepted from " + socket);

				//쓰레드를 가동시키면서 접속한 client 정보
				ServerThread st = new ServerThread(this, socket);
				st.start() ; //쓰레드가 가동				
			}			
		}catch(IOException e){ 			
			e.printStackTrace();//관련된 에러 전부 출력
		} //catch
	}

	public void removeThread(String key){ //쓰레드 및 리스트삭제	
		clientMap.remove(key);
		ServerUI.listModel.removeElement(key);
	}	
	
	public void broadcast(String str){  //전체 메세지 보내기
		Set<String> s = clientMap.keySet();
		String[] clist = new String[s.size()]; 
		s.toArray(clist);		
		for(int i=0;i<clist.length;i++) {
			clientMap.get(clist[i]).send(str);
		}
		ServerUI.txtarLog.append(str + "\n");
	}
	
	public void sendConnectorList(){  //접속자 리스트 보내기
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
	 * @param str[] 받을 클라이언트 목록
	 * @param obj  보낼 내용 (보탱의 경우 String 객체)
	 * @param from 메세지를 보낸 사람
	 */
	public void send(String[] str, Object obj, String from) {  //귓속말인 경우
		
		for(int i=0;i<str.length;i++){
			if(clientMap.containsKey(str[i])) {
				String msg = (String)obj;
				msg = "from " + from + ":" + msg;
				clientMap.get(str[i]).send(msg);
			}
		}
	}	
	
}

//주기적으로 접속자 리스트를 클라이언트에 뿌리기 위한 스레드
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

	//클라이언트에게 객체를 전달해주는 메소드	(텍스트의 경우에도 String 객체로 전송)
	public void send(Object obj) {
		try {
			oos.writeObject(obj);
			oos.flush();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void toSelected(Object obj) { //리스트에서 선택된 클라이언트에게만 메세지 전송
		ChatData chatdata = (ChatData)obj;		
		if(chatdata!=null && chatdata.to.length>0) {   //chatdata의 to 필드에 무언가가 있을 경우 => 클라이언트에서 메세지 전송시 리스트에서 보낼 사람을 선택 => 즉 귓속말인 경우
			for(int i=0;i<chatdata.to.length;i++) {
				if(chatdata.to[i].equals("To All")) {	//To ALL이 선택되어 있는 경우 전체 메세지 전송 후 빠져나감		
					server.broadcast(this.getName() + " : " + chatdata.data);
					return;
				}
			}
			server.send(chatdata.to, chatdata.data, this.getName());
		}
		else{  //전체 메세지
			System.out.println("sending broad");
			server.broadcast(this.getName() + " : " + chatdata.data);			
		}	
	}
 
	//클라이언트로부터 메세지를 받는 쓰레드
	@Override
	public void run(){
		try{
			Object obj=null;
			while((obj=ois.readObject())!=null) {
				if(obj instanceof String) { //첫 접속에만 String 객체 사용
					String str = (String)obj;
					if (str.startsWith("*****[")) {  //클라이언트에서 최초 접속시 보내는 헤더 *****[ID:REALID]***** 
						String ID = str.substring(9, str.lastIndexOf("]"));  //<= REALID 부분을 분리하여 ID로 저장
						ServerUI.listModel.addElement(ID);  //리스트에 ID추가
						this.setName(ID); //Thread의 이름을 ID로 설정 => ID중복 불가
						server.clientMap.put(ID, this);  
						server.reverseClientMap.put(this, ID);
						server.sendConnectorList(); //각 클라이언트에 현재 접속자 리스트 전송
						server.broadcast("["+ID+"]님이 접속하셨습니다.");
					}else {
						ServerUI.txtarLog.append(this.getName() +" : " + (String)obj + "\n");
					}
				}else if(obj instanceof ChatData) { //귓속말의 경우 ChatData 객체로 넘어옴
					toSelected(obj);
				}else if(obj instanceof ImageIcon) {
					System.out.println("Got ImageIcon");
					ImageIcon img = (ImageIcon)obj;
					ServerUI.btnBroadcast.setIcon(img); //이미지를 채팅창에 표시하기 위해서는 TextArea를 JEditorPane or JTextPane으로 변경 필요?
				}
			}				
		}catch (IOException | ClassNotFoundException e){			
			server.removeThread( this.getName() ); //소켓이 닫힌 경우(읽을 수 없는 경우) 쓰레드 및 리스트 제거
			server.broadcast("[" + this.getName() + "]님이 나가셨습니다.");
			System.out.println(socket.getInetAddress() + "의 연결이 종료되었습니다.");
		
		}
		
	}
}