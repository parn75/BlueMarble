package blue_Merged;

import java.awt.FileDialog;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;

//클라이언트에서 데이터를 받아서 처리하는 클래스
class ServerThread extends Thread{
	ServerUI serverUI;
	Server server;//서버의 정보
	Socket socket ; //클라이언트
	ObjectOutputStream oos;
	ObjectInputStream ois;
	int thisRoomNum = -1;
	RoomListPanel roomList = RoomListPanel.getInstance();

	public ServerThread(Server server, Socket socket, ServerUI serverUI){
		this.server = server ; //서버의 정보
		this.socket = socket ; //client
		this.serverUI = serverUI;

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
			System.out.println(this.getName() + "님이 접속을 종료하여 데이터가 전송되지 않았습니다.");
		}
	}	
	public boolean isContain(String[] str, String search) { 
		for(int i=0;i<str.length;i++) if(str[i].equals(search)) return true;
		return false;
	}
	public void toSelected(ChatData fromChat) { //리스트에서 선택된 클라이언트 혹은 천체 메세지 전송		
		fromChat.from = this.getName()+":";
		if(fromChat.type == ChatType.Whisper && fromChat!=null && fromChat.to.length>0 && isContain(fromChat.to, "To All")==true) {   //chatdata의 to 필드에 무언가가 있을 경우 => 클라이언트에서 메세지 전송시 리스트에서 보낼 사람을 선택 => 즉 귓속말인 경우
			fromChat.type = ChatType.Broadcast;
			server.broadcast(fromChat);
			return;			
		}else if (fromChat!=null && fromChat.to.length>0 && isContain(fromChat.to, "To All")==false) {			
			fromChat.from = "from " + this.getName() + ":";  //귓속말을 보내기 전에  누가 보냈는지 설정(ex - from com1:)
			server.send(fromChat);
		}
		else{  //전체 메세지
			System.out.println("sending broad");			
			server.broadcast(fromChat);				
		}
	}
	
	public void identifyID(ChatData cd) { //ID를 추려내어 처리하는 메소드
		String ID = (String) cd.data;  //<= REALID 부분을 분리하여 ID로 저장
		serverUI.listModel.addElement(ID);  //리스트에 ID추가
		this.setName(ID); //Thread의 이름을 ID로 설정 => ID중복 불가
		server.clientMap.put(ID, this);  
		server.reverseClientMap.put(this, ID);
		server.sendConnectorList(); //각 클라이언트에 현재 접속자 리스트 전송
		server.broadcast(new ChatData(ChatType.Broadcast,"Server:", "["+ID+"]님이 접속하셨습니다."));
	}
	
	public void getFile(String filename, String from) { //파일을 받는 메소드 //필요 없음
		int confirm = JOptionPane.showConfirmDialog(serverUI, from+"님이 파일을 보내셨습니다. 저장하시겠습니까?", "choose one", JOptionPane.YES_NO_OPTION);
		if(confirm == JOptionPane.OK_OPTION) {
			FileDialog fd = new FileDialog(serverUI, "Save as", FileDialog.SAVE);
		    fd.setVisible(true);
		    String fname = fd.getDirectory()+fd.getFile();
		    System.out.println(fname);
		    
		    try {
		    	if(fname!= null && fd.getDirectory()!= null && fd.getFile()!=null && fname!="") {
		    		FileTransferClient ftc;				
					ftc = new FileTransferClient(InetAddress.getLocalHost(), fname);
					ftc.start();				    	
		    	} else {
					serverUI.doc.insertString(serverUI.doc.getLength(), "전송이 취소되었습니다!", serverUI.sc.getStyle("MainSytle"));
				} 
		    }catch (UnknownHostException|BadLocationException e) {					
					e.printStackTrace();
			}		   	   
		}//end if confirm
	}
	
	public void showImage(ChatData cd){ //사용 안함
		System.out.println("Got ImageIcon");
		ImageIcon icon = (ImageIcon)cd.data; 
	    StyleConstants.setIcon(serverUI.chatStyle.imageStyle, icon);
	    
	    try {
	    	//StyledEditorKit sek; //이미지 크기를 조절하기위해서는 에디터킷을 이용해야 할듯	
	    	serverUI.doc.insertString(serverUI.doc.getLength(), "Image from " + cd.from  + "\n", serverUI.chatStyle.mainStyle);
	    	serverUI.doc.insertString(serverUI.doc.getLength(), "ignored text\n", serverUI.chatStyle.imageStyle);	    	   	
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}	    
	    serverUI.endScroll();
		
	}
	
	private void join(ChatData cd) { //대기실 참여
		WaitingRoomInfo wri = (WaitingRoomInfo)cd.data;
		if (roomList.joinRoom(wri.getRoomNum(), this.getName()) == true) {			
			ChatData rd = new ChatData(ChatType.Join, "Success to join in room (" + wri.getRoomNum() +")" );
			server.send(rd, this);
			sendRoomStatus();
			thisRoomNum = wri.getRoomNum();
			if(!wri.getRoomName().toString().equals("참가")) {
				roomList.setRoom(wri.getRoomNum(), wri.getRoomName().toString());
				roomList.roomList[wri.getRoomNum()].roomInfo.setRoomName(wri.getRoomName());
			}			
		}else {
			//String[] to = {this.getName()};
			ChatData rd = new ChatData(ChatType.Join, "방이 가득찼습니다.");
			server.send(rd, this);
		}
	}	

	private void waitingRoomChat(ChatData cd) { //대기실 안에서 채팅		
		ChatData rd = new ChatData(ChatType.WaitingRoomChat, cd.data);
		String[] to = new String[roomList.roomList[thisRoomNum].getPlayerNames().length];
		for(int i=0;i<roomList.roomList[thisRoomNum].getPlayerNames().length;i++)
							to[i] = roomList.roomList[thisRoomNum].getPlayerNames()[i].toString();	
		
		rd.setTo(to);
		rd.setFrom(this.getName() + ":");
		server.send(rd);
	}
	
	private void sendGameData(ChatData cd) { //대기실 안에 있는 사람들에게 게임 데이터 전송		
		ChatData rd = new ChatData();
		rd.setType(ChatType.GameData);
		rd.setData(cd.data);
		String[] to = new String[roomList.roomList[thisRoomNum].getPlayerNames().length];
		for(int i=0;i<roomList.roomList[thisRoomNum].getPlayerNames().length;i++)
							to[i] = roomList.roomList[thisRoomNum].getPlayerNames()[i].toString();	
		
		rd.setTo(to);
		rd.setFrom(this.getName() + ":");
		server.send(rd);
	}
	

	private void waitingRoomExit(ChatData cd) { //대기실 나감		
		roomList.deletePlayer(thisRoomNum, this.getName());
		sendRoomStatus();
	}
	
	public void sendRoomStatus() {		
		WaitingRoomInfo[] wrs = roomList.cloneWholeRoomInfo(); //roomList.getWholeRoomInfo()로는 안됨? why? -_-;; 미치미치미치
		ChatData cd = new ChatData(ChatType.RoomStatus, wrs);			
		if(server.clientMap.size()>0) {
			server.broadcast(cd);		
		}		
	}
	
	public void exitProgram() {
		server.removeThread( this.getName() ); //소켓이 닫힌 경우(읽을 수 없는 경우) 쓰레드 및 리스트 제거	
		server.broadcast(new ChatData(ChatType.Broadcast, "Server: ", "[" + this.getName() + "]님이 나가셨습니다."));
		server.sendConnectorList();
		System.out.println(socket.getInetAddress() + "의 연결이 종료되었습니다.");
	}
	
	public void gameStart(ChatData cd) {
		ChatData rd = new ChatData();
		rd.setType(ChatType.GameStart);		
		String[] to = new String[roomList.roomList[thisRoomNum].getPlayerNames().length];
		for(int i=0;i<roomList.roomList[thisRoomNum].getPlayerNames().length;i++)
							to[i] = roomList.roomList[thisRoomNum].getPlayerNames()[i].toString();	
		
		rd.setTo(to);
		rd.setFrom(this.getName() + ":");
		server.send(rd);
	}

	//클라이언트로부터 메세지를 받는 쓰레드
	@Override
	public void run(){
		try{
			Object obj=null;
			while((obj=ois.readObject())!=null) {
				ChatData cd = (ChatData)obj;
				
				switch(cd.type) {				
				case ConnectorList:	break;	//서버에서는 무시	
				case ID: identifyID(cd); break; //최초 로그인시 사용
				case File: 				
				case Image:
				case Whisper: 				
				case Broadcast: toSelected(cd);	break; //귓속말
				case Join: join(cd); break; //대기실 참가
				case WaitingRoomChat: waitingRoomChat(cd); break; //대기실 대화
				case WaitingRoomExit: waitingRoomExit(cd); break; //대기실 나감
				case Exit: exitProgram(); break;
				case GameData: sendGameData(cd); break;
				case GameStart: gameStart(cd); break;
				default:
					break;

				}
			}		
		}catch (IOException | ClassNotFoundException e){
				if(server.clientMap.containsKey(this.getName())) {
					server.removeThread( this.getName() ); //소켓이 닫힌 경우(읽을 수 없는 경우) 쓰레드 및 리스트 제거					
					server.broadcast(new ChatData(ChatType.Broadcast, "Server: ", "[" + this.getName() + "]님이 나가셨습니다."));
					System.out.println(socket.getInetAddress() + "의 연결이 종료되었습니다.");	
				}
		}	
	}


}
