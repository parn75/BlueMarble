package bluemarble_Merged;

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

//Ŭ���̾�Ʈ���� �����͸� �޾Ƽ� ó���ϴ� Ŭ����
class ServerThread extends Thread{
	ServerUI serverUI;
	Server server;//������ ����
	Socket socket ; //Ŭ���̾�Ʈ
	ObjectOutputStream oos;
	ObjectInputStream ois;
	int thisRoomNum = -1;
	RoomListPanel roomList = RoomListPanel.getInstance();

	public ServerThread(Server server, Socket socket, ServerUI serverUI){
		this.server = server ; //������ ����
		this.socket = socket ; //client
		this.serverUI = serverUI;

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
			System.out.println(this.getName() + "���� ������ �����Ͽ� �����Ͱ� ���۵��� �ʾҽ��ϴ�.");
			server.removeThread(this.getName());
		}
	}	
	public boolean isContain(String[] str, String search) { 
		for(int i=0;i<str.length;i++) if(str[i].equals(search)) return true;
		return false;
	}
	public void toSelected(ChatData fromChat) { //����Ʈ���� ���õ� Ŭ���̾�Ʈ Ȥ�� õü �޼��� ����		
		fromChat.from = this.getName()+":";
		if(fromChat.type == ChatType.Whisper && fromChat!=null && fromChat.to.length>0 && isContain(fromChat.to, "To All")==true) {   //chatdata�� to �ʵ忡 ���𰡰� ���� ��� => Ŭ���̾�Ʈ���� �޼��� ���۽� ����Ʈ���� ���� ����� ���� => �� �ӼӸ��� ���
			fromChat.type = ChatType.Broadcast;
			server.broadcast(fromChat);
			return;			
		}else if (fromChat!=null && fromChat.to.length>0 && isContain(fromChat.to, "To All")==false) {			
			fromChat.from = "from " + this.getName() + ":";  //�ӼӸ��� ������ ����  ���� ���´��� ����(ex - from com1:)
			server.send(fromChat);
		}
		else{  //��ü �޼���
			System.out.println("sending broad");			
			server.broadcast(fromChat);				
		}
	}
	
	public void identifyID(ChatData cd) { //ID�� �߷����� ó���ϴ� �޼ҵ�
		String ID = (String) cd.data;  //<= REALID �κ��� �и��Ͽ� ID�� ����
		serverUI.listModel.addElement(ID);  //����Ʈ�� ID�߰�
		this.setName(ID); //Thread�� �̸��� ID�� ���� => ID�ߺ� �Ұ�
		server.clientMap.put(ID, this);  
		server.reverseClientMap.put(this, ID);
		server.sendConnectorList(); //�� Ŭ���̾�Ʈ�� ���� ������ ����Ʈ ����
		server.broadcast(new ChatData(ChatType.Broadcast,"Server:", "["+ID+"]���� �����ϼ̽��ϴ�."));
	}
	
	public void getFile(String filename, String from) { //������ �޴� �޼ҵ� //�ʿ� ����
		int confirm = JOptionPane.showConfirmDialog(serverUI, from+"���� ������ �����̽��ϴ�. �����Ͻðڽ��ϱ�?", "choose one", JOptionPane.YES_NO_OPTION);
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
					serverUI.doc.insertString(serverUI.doc.getLength(), "������ ��ҵǾ����ϴ�!", serverUI.sc.getStyle("MainSytle"));
				} 
		    }catch (UnknownHostException|BadLocationException e) {					
					e.printStackTrace();
			}		   	   
		}//end if confirm
	}
	
	public void showImage(ChatData cd){ //��� ����
		System.out.println("Got ImageIcon");
		ImageIcon icon = (ImageIcon)cd.data; 
	    StyleConstants.setIcon(serverUI.chatStyle.imageStyle, icon);
	    
	    try {
	    	//StyledEditorKit sek; //�̹��� ũ�⸦ �����ϱ����ؼ��� ������Ŷ�� �̿��ؾ� �ҵ�	
	    	serverUI.doc.insertString(serverUI.doc.getLength(), "Image from " + cd.from  + "\n", serverUI.chatStyle.mainStyle);
	    	serverUI.doc.insertString(serverUI.doc.getLength(), "ignored text\n", serverUI.chatStyle.imageStyle);	    	   	
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}	    
	    serverUI.endScroll();
		
	}
	
	private void join(ChatData cd) { //���� ����
		WaitingRoomInfo wri = (WaitingRoomInfo)cd.data;
		if (roomList.joinRoom(wri.getRoomNum(), this.getName()) == true) {			
			ChatData rd = new ChatData(ChatType.Join, "Success to join in room (" + wri.getRoomNum() +")" );
			server.send(rd, this);
			sendRoomStatus();
			thisRoomNum = wri.getRoomNum();
			if(!wri.getRoomName().toString().equals("����")) {
				roomList.setRoom(wri.getRoomNum(), wri.getRoomName().toString());
				roomList.roomList[wri.getRoomNum()].roomInfo.setRoomName(wri.getRoomName());
			}			
		}else {
			//String[] to = {this.getName()};
			ChatData rd = new ChatData(ChatType.Join, "���� ����á���ϴ�.");
			server.send(rd, this);
		}
	}	

	private void waitingRoomChat(ChatData cd) { //���� �ȿ��� ä��		
		ChatData rd = new ChatData(ChatType.WaitingRoomChat, cd.data);
		String[] to = new String[roomList.roomList[thisRoomNum].getPlayerNames().length];
		for(int i=0;i<roomList.roomList[thisRoomNum].getPlayerNames().length;i++)
							to[i] = roomList.roomList[thisRoomNum].getPlayerNames()[i].toString();	
		
		rd.setTo(to);
		rd.setFrom(this.getName() + ":");
		server.send(rd);
	}
	
	private void sendGameData(ChatData cd) { //���� �ȿ� �ִ� ����鿡�� ���� ������ ����		
		ChatData rd = new ChatData();
		rd.setType(ChatType.GameData);
		rd.setData(cd.data);
		String[] to = new String[roomList.roomList[thisRoomNum].getPlayerNames().length];
		for(int i=0;i<roomList.roomList[thisRoomNum].getPlayerNames().length;i++)
							to[i] = roomList.roomList[thisRoomNum].getPlayerNames()[i].toString();	
		
		rd.setTo(to);
		rd.setFrom(this.getName());
		server.send(rd);
	}
	

	private void waitingRoomExit(ChatData cd) { //���� ����		
		roomList.deletePlayer(thisRoomNum, this.getName());
		sendRoomStatus();
	}
	
	public void sendRoomStatus() {		
		WaitingRoomInfo[] wrs = roomList.cloneWholeRoomInfo(); //roomList.getWholeRoomInfo()�δ� �ȵ�? why? -_-;; ��ġ��ġ��ġ
		ChatData cd = new ChatData(ChatType.RoomStatus, wrs);			
		if(server.clientMap.size()>0) {
			server.broadcast(cd);		
		}		
	}
	
	public void exitProgram() {
		server.removeThread( this.getName() ); //������ ���� ���(���� �� ���� ���) ������ �� ����Ʈ ����	
		server.broadcast(new ChatData(ChatType.Broadcast, "Server: ", "[" + this.getName() + "]���� �����̽��ϴ�."));
		server.sendConnectorList();
		System.out.println(socket.getInetAddress() + "�� ������ ����Ǿ����ϴ�.");
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

	//Ŭ���̾�Ʈ�κ��� �޼����� �޴� ������
	@Override
	public void run(){
		try{
			Object obj=null;
			while((obj=ois.readObject())!=null) {
				ChatData cd = (ChatData)obj;
				
				switch(cd.type) {				
				case ConnectorList:	break;	//���������� ����	
				case ID: identifyID(cd); break; //���� �α��ν� ���
				case File: 				
				case Image:
				case Whisper: 				
				case Broadcast: toSelected(cd);	break; //�ӼӸ�
				case Join: join(cd); break; //���� ����
				case WaitingRoomChat: waitingRoomChat(cd); break; //���� ��ȭ
				case WaitingRoomExit: waitingRoomExit(cd); break; //���� ����
				case Exit: exitProgram(); break;
				case GameData: sendGameData(cd); break;
				case GameStart: gameStart(cd); break;
				default:
					break;

				}
			}		
		}catch (IOException | ClassNotFoundException e){
				if(server.clientMap.containsKey(this.getName())) {
					server.removeThread( this.getName() ); //������ ���� ���(���� �� ���� ���) ������ �� ����Ʈ ����					
					server.broadcast(new ChatData(ChatType.Broadcast, "Server: ", "[" + this.getName() + "]���� �����̽��ϴ�."));
					System.out.println(socket.getInetAddress() + "�� ������ ����Ǿ����ϴ�.");	
				}
		}	
	}


}
