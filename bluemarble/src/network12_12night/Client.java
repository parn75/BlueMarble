package network12_12night;

import java.awt.FileDialog;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
//GUI
//event
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;

class Client extends Thread {
	Socket socket ; //서버와 통신
	ClientUI clientUI;
	String hostname;
	WaitingRoom waitingRoom;
	boolean inWaitingRoom = false;
	boolean startWaitingRoom = false;
	boolean inGame = false;

	ObjectOutputStream oos;
	ObjectInputStream ois;

	public Client(ClientUI clientUI, String hostname){		//객체 생성 및 배치
		this.clientUI = clientUI;
		this.hostname = hostname;
		try{ //서버와 연결하는 구문			
			socket = new Socket(hostname, 5432);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		}catch (Exception e){
			e.getMessage();
		}
	}
	
	public void showImage(ChatData cd){  //필요 없음
		System.out.println("Got ImageIcon");
		//ChatData cd = (ChatData)obj;
		ImageIcon icon = (ImageIcon)cd.data;	    
	    StyleConstants.setIcon(clientUI.chatStyle.imageStyle, icon);
	    
	    try {
	    	clientUI.doc.insertString(clientUI.doc.getLength(), "Image from " + cd.from + "\n", clientUI.chatStyle.mainStyle);
	    	clientUI.doc.insertString(clientUI.doc.getLength(), "ignored text\n", clientUI.chatStyle.imageStyle);	    	   	
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}	    
	    //clientUI.endScroll();		
	}
	
	public void updateConnectorList(ChatData cd) { 
		if(cd.data!=null) {
			String[] str = (String[]) cd.data;   
			List<String> l = clientUI.lstConnector.getSelectedValuesList(); //선택되어 있던 명단을 기억
			
			clientUI.listModel.clear();  //현재 리스트 목록 삭제
			int[] indices = new int[l.size()];
			for(int i=0;i<indices.length;i++) indices[i] = -1;  
			int index=0;
			for(int i=0;i<str.length;i++) {
				clientUI.listModel.addElement(str[i]);
				for(String s:l) if(s.equals(str[i])) indices[index++] = i; //connector						
			}
			clientUI.lstConnector.setSelectedIndices(indices); //선택되어 있던 명단을 복원하여 다시 선택 상태로 전환
		}
	}
	
	public void getFile(ChatData cd) { //파일을 받는 메소드 //필요 없음
		Object[] inData = new Object[2]; //파일 데이터의 경우 Object[0]는 보낸 사람의 InetAddress, Object[1]은 파일 이름(String)임. 
		inData = (Object[]) cd.data;
		int confirm = JOptionPane.showConfirmDialog(clientUI, cd.from+" " + inData[1]+ " 파일을 보내셨습니다. 저장하시겠습니까?", "choose one", JOptionPane.YES_NO_OPTION);
		if(confirm == JOptionPane.OK_OPTION) {
			FileDialog fd = new FileDialog(clientUI, "Save as", FileDialog.SAVE);
		    fd.setVisible(true);
		    String fname = fd.getDirectory()+fd.getFile();
		    System.out.println(fname);		    
		    
		    if(fname!= null && fd.getDirectory()!= null && fd.getFile()!=null && fname!="") {
		    	FileTransferClient ftc = new FileTransferClient((InetAddress)inData[0], fname);
		    	ftc.start();
		    } else { 			    
				try {
					clientUI.doc.insertString(clientUI.doc.getLength(), "전송이 취소되었습니다!", clientUI.sc.getStyle("MainSytle"));
				} catch (BadLocationException e) {					
					e.printStackTrace();
				}
		    }		   
		}
	}

	@Override
	public void run(){
		try{ 

			Object obj=null;
			
			while((obj=ois.readObject())!=null) {	
				
				if(obj instanceof ChatData) {
					ChatData cd = (ChatData)obj;					
					try {
						switch(cd.type) {							
							case ConnectorList:	updateConnectorList(cd); //접속자 리스트 업데이트
								break;
							case File:getFile(cd); //파일 받기
								break;
							case ID:
								break;
							case Image: showImage(cd); //이미지 보여주기
								break;
							case Broadcast: clientUI.doc.insertString(clientUI.doc.getLength(), cd.from + (String)cd.data + "\n", clientUI.sc.getStyle("MainSytle"));
								break;
							case Whisper: clientUI.doc.insertString(clientUI.doc.getLength(), cd.from + (String)cd.data + "\n", clientUI.sc.getStyle("WhisperStyle"));
								break;
							case Join: //대기실 참가
									String msg = (String)cd.data;
									if(msg.equals("Success")) {
										System.out.println("방 접속 성공"); //방 접속 작업
										inWaitingRoom = true;
										waitingRoom = new WaitingRoom(this);
									}else JOptionPane.showMessageDialog(clientUI, msg);
								break;
							case RoomStatus: updateRoomStatus(cd); //전체 대기실 상태 데이터
								break;
							case WaitingRoomStatus: if(inWaitingRoom == true) updateWaitingRoomStatus(cd); //내가 접속해있는 대기실 상태 데이터
								break;
							case GameData: break; //게임 데이터를 위해 예약
							case WaitingRoomChat:  //대기실 내부 채팅								
								waitingRoom.doc.insertString(waitingRoom.doc.getLength(), cd.from + (String)cd.data + "\n", waitingRoom.sc.getStyle("MainSytle"));
								break;
							default:
								break;					
						}
					} catch (BadLocationException e) {						
						e.printStackTrace();
					}
				}
				clientUI.endScroll(); //스크롤을 가장 아래로 이동
			}
			
		}catch (IOException | ClassNotFoundException e){
			e.getMessage();
		}
	}

	synchronized private void updateWaitingRoomStatus(ChatData cd) { //내가 들어가 있는 대기실 상태 업데이트
		WaitingRoomStatus[] wrsAry = (WaitingRoomStatus[])cd.data;
		WaitingRoomStatus wrs = null;
		
		ForPlayers: 
		for(int i=0;i<12;i++) {		
			for(String s: wrsAry[i].playerNames) {			
				if (s!=null && s.equals(clientUI.clientAction.ID)) {
					wrs = wrsAry[i];
					if(inWaitingRoom == true && waitingRoom!= null) 
						if(waitingRoom.isVisible()) waitingRoom.updateWaitingRoom(wrs);
					break ForPlayers;
				}
			}			
		}
		
		if(startWaitingRoom == false && wrs!=null) {
			startWaitingRoom = true;
			clientUI.setVisible(false);
		}
	}

	synchronized private void updateRoomStatus(ChatData cd) { //전체 대기실 상태 업데이트
		RoomStatus rs = (RoomStatus)(cd.data);
		for(int i=0;i<12;i++) {
			clientUI.roomList.roomList[i].setTitle(rs.roomTitle[i]);
			clientUI.roomList.roomList[i].setPlayerNum(rs.playerNum[i]);
			if(rs.playerNum[i]>0) clientUI.roomList.roomList[i].joinB.setText("참가하기");
			else if (rs.playerNum[i]==4) clientUI.roomList.roomList[i].joinB.setEnabled(false);
			clientUI.roomList.roomList[i].setStarted(rs.isStarted[i]);
		}
	}

	public void send(Object obj) { //데이터 전송
		try {
			oos.writeObject(obj);			
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}	
}