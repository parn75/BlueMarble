package backup11282;

//서버->접속할 수 있도록 무한 대기

import java.io.IOException;//입출력
import java.net.ServerSocket;//네트워크
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.text.BadLocationException;
//클라이언트의 정보 저장

public class Server extends Thread{
	ServerUI serverUI;
	ServerSocket ss ; //접속하기위해서 
	Socket socket ; //클라이언트와 통신
	Map<ServerThread, String> reverseClientMap = Collections.synchronizedMap(new HashMap<ServerThread, String>());
	Map<String, ServerThread> clientMap = Collections.synchronizedMap(new HashMap<String, ServerThread>());
	
	public Server(ServerUI serverUI) {
		this.serverUI = serverUI;
	}
	
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
				ServerThread st = new ServerThread(this, socket, serverUI);
				st.start() ; //쓰레드가 가동				
			}			
		}catch(IOException e){ 			
			e.printStackTrace();//관련된 에러 전부 출력
		} //catch
	}

	public void removeThread(String key){ //쓰레드 및 리스트삭제	
		clientMap.remove(key);
		serverUI.listModel.removeElement(key);
	}	
	
//	public void broadcast(String str){  //전체 메세지 보내기
	public void broadcast(Object obj){  //전체 메세지 보내기
		Set<String> s = clientMap.keySet();
		String[] clist = new String[s.size()]; 
		s.toArray(clist);		
		for(int i=0;i<clist.length;i++) {
			clientMap.get(clist[i]).send(obj);
		}

		try {
			serverUI.doc.insertString(serverUI.doc.getLength(), obj + "\n", serverUI.sc.getStyle("MainSytle"));
			serverUI.sp.getVerticalScrollBar().setValue(serverUI.sp.getVerticalScrollBar().getMaximum()); //스크롤을 가장 아래로 	
		} catch (BadLocationException e) {			
			e.printStackTrace();
		}
		
	}

	/**	 
	 * @param str[] 받을 클라이언트 목록
	 * @param obj  보낼 내용 (보탱의 경우 String 객체)
	 * @param from 메세지를 보낸 사람
	 */
	public void send(String[] str, Object obj, String from) {  //선택된 명단에 메세지(객체) 전송
		
		for(int i=0;i<str.length;i++){
			if(clientMap.containsKey(str[i])) {
				String msg = (String)obj;
				msg = "from " + from + ":" + msg;
				clientMap.get(str[i]).send(msg);
			}
		}
	}	
	
	public void sendConnectorList(){  //접속자 리스트 보내기, 클라이언트 최초 접속시 한번씩만 실행됨. 이후에는 주기적으로 리스트 전송.
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


