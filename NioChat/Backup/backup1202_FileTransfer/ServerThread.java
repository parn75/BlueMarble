package backup1202_FileTransfer;

import java.awt.FileDialog;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

//클라이언트와 통신->쓰레드 필요
class ServerThread extends Thread{
	ServerUI serverUI;
	Server server;//서버의 정보
	Socket socket ; //클라이언트
	ObjectOutputStream oos;
	ObjectInputStream ois;

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
	
	public void identifyID(String str) { //ID를 추려내어 처리하는 메소드
		String ID = str.substring(9, str.lastIndexOf("]"));  //<= REALID 부분을 분리하여 ID로 저장
		serverUI.listModel.addElement(ID);  //리스트에 ID추가
		this.setName(ID); //Thread의 이름을 ID로 설정 => ID중복 불가
		server.clientMap.put(ID, this);  
		server.reverseClientMap.put(this, ID);
		server.sendConnectorList(); //각 클라이언트에 현재 접속자 리스트 전송
		server.broadcast("["+ID+"]님이 접속하셨습니다.");
	}
	
	public void getFile(String filename, String from) {
		int confirm = JOptionPane.showConfirmDialog(serverUI, from+"님이 파일을 보내셨습니다. 저장하시겠습니까?", "choose one", JOptionPane.YES_NO_OPTION);
		if(confirm == JOptionPane.OK_OPTION) {
			FileDialog fd = new FileDialog(serverUI, "Save as", FileDialog.SAVE);
		    fd.setVisible(true);
		    String fname = fd.getDirectory()+fd.getFile();
		    System.out.println(fname);
		    if(fname!= null && fd.getDirectory()!= null && fd.getFile()!=null && fname!="") {
		    	FileTransferClient ftc = new FileTransferClient(fname);
		    	ftc.start();
		    } else { 			    
				try {
					serverUI.doc.insertString(serverUI.doc.getLength(), "전송이 취소되었습니다!", serverUI.sc.getStyle("MainSytle"));
				} catch (BadLocationException e) {					
					e.printStackTrace();
				}
		    }		   
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
					if (str.startsWith("*****[ID:")) {  //클라이언트에서 최초 접속시 보내는 헤더 *****[ID:REALID]***** 
						identifyID(str);
					}else if(str.startsWith("*****[filename:")) {
						String filename = str.substring(15, str.lastIndexOf("]"));
						getFile(filename, this.getName());
					}
					else {
						String tmp = "Log: String Object without header from " + this.getName() +" : " + (String)obj + "\n";						
						serverUI.doc.insertString(serverUI.doc.getLength(), tmp, serverUI.sc.getStyle("MainSytle"));						
						serverUI.endScroll();
					}
				}else if(obj instanceof ChatData) { //귓속말의 경우 ChatData 객체로 넘어옴
					toSelected(obj);
				}else if(obj instanceof ImageIcon) {					
					server.broadcast("Image from " + this.getName());
					server.broadcast(obj);
				}
			}				
		}catch (IOException | ClassNotFoundException | BadLocationException e){			
			server.removeThread( this.getName() ); //소켓이 닫힌 경우(읽을 수 없는 경우) 쓰레드 및 리스트 제거
			server.broadcast("[" + this.getName() + "]님이 나가셨습니다.");
			System.out.println(socket.getInetAddress() + "의 연결이 종료되었습니다.");
		
		}			
	}
}
