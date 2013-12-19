package blue_Merged;

import java.awt.FileDialog;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;

class Client extends Thread {
	Socket socket ; //������ ���
	ClientUI clientUI;
	String hostname, myName;
	WaitingRoomUI waitingRoom;
	boolean inWaitingRoom = false;
	boolean startWaitingRoom = false;
	boolean inGame = false;
	int myRoomNum = -1;
	

	ObjectOutputStream oos;
	ObjectInputStream ois;

	MabulEx marble;
	
	public String getMyName() {
		return myName;
	}
	
	public void setMyName(String name) {
		this.myName = name;
	}
	
	public Client(ClientUI clientUI, String hostname){		//��ü ���� �� ��ġ
		this.clientUI = clientUI;
		this.hostname = hostname;
		try{ //������ �����ϴ� ����			
			//socket = new Socket(hostname, 5432);
			socket = new Socket(hostname, 15452);
			socket.setKeepAlive(true);
			
			oos = new ObjectOutputStream(socket.getOutputStream());			
			ois = new ObjectInputStream(socket.getInputStream());
			
			System.out.println(socket.getKeepAlive());
			System.out.println(oos.toString());
			System.out.println(ois.toString());
		/*	
			String ID = "sss1";
			setMyName(ID);
			ChatData cd = new ChatData(ChatType.ID, ID);
			send(cd); 
		*/	
		}catch(UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void showImage(ChatData cd){  //�ʿ� ����
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
			List<String> l = clientUI.lstConnector.getSelectedValuesList(); //���õǾ� �ִ� ����� ���
			
			clientUI.listModel.clear();  //���� ����Ʈ ��� ����
			int[] indices = new int[l.size()];
			for(int i=0;i<indices.length;i++) indices[i] = -1;  
			int index=0;
			for(int i=0;i<str.length;i++) {
				clientUI.listModel.addElement(str[i]);
				for(String s:l) if(s.equals(str[i])) indices[index++] = i; //connector						
			}
			clientUI.lstConnector.setSelectedIndices(indices); //���õǾ� �ִ� ����� �����Ͽ� �ٽ� ���� ���·� ��ȯ
		}
	}
	
	public void getFile(ChatData cd) { //������ �޴� �޼ҵ� //�ʿ� ����
		Object[] inData = new Object[2]; //���� �������� ��� Object[0]�� ���� ����� InetAddress, Object[1]�� ���� �̸�(String)��. 
		inData = (Object[]) cd.data;
		int confirm = JOptionPane.showConfirmDialog(clientUI, cd.from+" " + inData[1]+ " ������ �����̽��ϴ�. �����Ͻðڽ��ϱ�?", "choose one", JOptionPane.YES_NO_OPTION);
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
					clientUI.doc.insertString(clientUI.doc.getLength(), "������ ��ҵǾ����ϴ�!", clientUI.sc.getStyle("MainSytle"));
				} catch (BadLocationException e) {					
					e.printStackTrace();
				}
		    }		   
		}
	}
	
	public void joinWaitingRoom(ChatData cd) {
		String msg = (String)cd.data;
		if(msg.contains("Success")) {
			System.out.println("�� ���� ����"); //�� ���� �۾�
			myRoomNum = Integer.parseInt(msg.substring(msg.lastIndexOf('(')+1, msg.lastIndexOf(')') ));
			System.out.println("MyRoomNum: " + myRoomNum +"");
			inWaitingRoom = true;
			waitingRoom = new WaitingRoomUI(this);
			clientUI.setVisible(false);
		}else JOptionPane.showMessageDialog(clientUI, msg);
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
							case ConnectorList:	updateConnectorList(cd); //������ ����Ʈ ������Ʈ
								break;
							case File:getFile(cd); //���� �ޱ�
								break;
							case ID:
								break;
							case Image: showImage(cd); //�̹��� �����ֱ�
								break;
							case Broadcast:
								if(clientUI.isVisible()) clientUI.requestFocus();
								clientUI.doc.insertString(clientUI.doc.getLength(), cd.from + (String)cd.data + "\n", clientUI.sc.getStyle("MainSytle"));
								break;
							case Whisper: 
								if(clientUI.isVisible()) clientUI.requestFocus();
								clientUI.doc.insertString(clientUI.doc.getLength(), cd.from + (String)cd.data + "\n", clientUI.sc.getStyle("WhisperStyle"));
								break;
							case Join: joinWaitingRoom(cd); //���� ����									
								break;
							case RoomStatus: 		
								//System.out.println((String)cd.data);
								updateRoomStatus(cd); //��ü ���� ���� ������
								break;
							case WaitingRoomStatus: //if(inWaitingRoom == true) updateWaitingRoomStatus(cd); //���� �������ִ� ���� ���� ������
								break;
							case GameData: TablePanel.control.recieveData(cd.from, cd.data); /*cd.from, cd.data*/ break; //���� �����͸� ���� ����
							case WaitingRoomChat:  //���� ���� ä��	
								if(waitingRoom.isVisible()) {
									waitingRoom.requestFocus(); 
									waitingRoom.doc.insertString(waitingRoom.doc.getLength(), cd.from + (String)cd.data + "\n", waitingRoom.sc.getStyle("MainSytle"));
									waitingRoom.endScroll();
								}else if(marble.isVisible()) {
									marble.requestFocus();
									marble.tp.chatView.append(cd.from + (String)cd.data + "\n");
								}
								break;
							case GameStart: marble = new MabulEx(waitingRoom);
											waitingRoom.setVisible(false);
								break;
							default:
								break;					
						}
					} catch (BadLocationException e) {						
						e.printStackTrace();
					}
				}
				clientUI.endScroll(); //��ũ���� ���� �Ʒ��� �̵�
			}
			
		}catch (IOException | ClassNotFoundException e){
			e.getMessage();
		}
	}

	synchronized private void updateRoomStatus(ChatData cd) { //��ü ���� ���� ������Ʈ

		RoomListPanel roomList = RoomListPanel.getInstance();
		WaitingRoomInfo[] wholeInfo = (WaitingRoomInfo[])(cd.data);	
		roomList.setWholeRoomInfo(wholeInfo);

		if(inWaitingRoom==true && myRoomNum>=0) {
			if(waitingRoom.isVisible()) waitingRoom.updateWaitingRoom(wholeInfo[myRoomNum]);
			//System.out.println("�� ������Ʈ");
		}
		
	}

	public void send(Object obj) { //������ ����
		try {
			oos.writeObject(obj);
			oos.flush();
			System.out.println("�����Ͱ� ���۵Ǿ����ϴ�.");
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}	
}