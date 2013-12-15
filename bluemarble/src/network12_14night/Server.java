package network12_14night;

//접속 및 데이터 전송 클래스

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
		clientMap.get(key).interrupt();
		clientMap.remove(key);
		serverUI.listModel.removeElement(key);
	}
	
	public void showImage(ChatData cd){
		System.out.println("Got ImageIcon");
		ImageIcon icon = (ImageIcon)cd.data; 
	    StyleConstants.setIcon(serverUI.chatStyle.imageStyle, icon);
	    
	    try {
	    	//StyledEditorKit sek; //이미지 크기를 조절하기위해서는 에디터킷을 이용해야 할듯	
	    	serverUI.doc.insertString(serverUI.doc.getLength(), "Image from: " + cd.from  + "\n", serverUI.chatStyle.mainStyle);
	    	serverUI.doc.insertString(serverUI.doc.getLength(), "ignored text\n", serverUI.chatStyle.imageStyle);	    	   	
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}	    
	    serverUI.endScroll();		
	}
	
//	public void broadcast(String str){  //전체 메세지 보내기
	public void broadcast(ChatData cd){  //전체 메세지 보내기
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
	 * @param str[] 받을 클라이언트 목록
	 * @param cd  보낼 내용 (보탱의 경우 String 객체)
	 * @param from 메세지를 보낸 사람
	 */
	synchronized public void send(ChatData cd) {  //선택된 명단에 메세지(객체) 전송, 서버에서 전송시에는 ChatData의 to영역이 null이면 안된다.
		
		for(int i=0;i<cd.to.length;i++){			
			if(clientMap.containsKey(cd.to[i])) {
				if(!cd.to[i].equals(cd.from ))clientMap.get(cd.to[i]).send(cd);			
			}
		}
	}
	
	synchronized public void send(ChatData cd, ServerThread st) {  //스레드의 네임과 일치하는 클라이언트에 전송
		String[] to = new String[1];
		to[0] = st.getName();
		cd.setTo(to);
		for(int i=0;i<cd.to.length;i++){			
			if(clientMap.containsKey(cd.to[i])) {
				if(!cd.to[i].equals(cd.from ))clientMap.get(cd.to[i]).send(cd);			
			}
		}
	}
	
	public void sendConnectorList(){  //접속자 리스트 보내기, 클라이언트 최초 접속시 한번씩만 실행됨. 이후에는 주기적으로 리스트 전송.		
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


