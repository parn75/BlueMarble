package network12_12;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Timer;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;

public class ServerAction extends AbstractAction implements ActionListener{
	private ServerUI serverUI;
	Server server;
	
	private void btnSvrStartAction() { //서버 시작 버튼 액션 처리
		server = new Server(serverUI);
		server.start();
		SendConnectorList scl = new SendConnectorList(server, serverUI);
		Timer timer = new Timer();
		timer.schedule(scl, 1000, 5000); //5초에 한번씩 접속자 명단 전송
		serverUI.btnSvrStart.setText("Server Has Been Started!");
		serverUI.btnSvrStart.setEnabled(false);
		
		Timer timer2 = new Timer();
		SendRoomStatus srs = new SendRoomStatus(server, serverUI);
		timer2.schedule(srs, 2000, 2000);
		
		Timer timer3 = new Timer();
		SendWaitingRoomStatus swr = new SendWaitingRoomStatus(server, serverUI);
		timer3.schedule(swr, 3000, 3000);
		
	}
	
	private void btnPersonalMsgAction() throws BadLocationException { //귓속말 버튼 액션 처리
		List<String> l = serverUI.lstConnector.getSelectedValuesList();			
		String[] to = new String[l.size()];
		l.toArray(to);
		String msg = serverUI.fldChat.getText();		
		if(l.contains("To All")) {  //To All이 선택되어 있으면 전체 메세지 전송 후 리턴
			ChatData cd = new ChatData(ChatType.Broadcast, "[Broadcast] : " + msg);
			server.broadcast(cd);
			return;
		} else server.send(new ChatData(ChatType.Whisper, "From Server:", to, msg)); //server.send(to, msg, "Server: ");
		
		if(l.size()>0) {			
			serverUI.doc.insertString(serverUI.doc.getLength(), "to ", serverUI.sc.getStyle("MainSytle"));
			for(int i=0;i<to.length;i++) {
				 //to Str추가, Ex: to client1, client2, ...
				serverUI.doc.insertString(serverUI.doc.getLength(), to[i] + ",", serverUI.sc.getStyle("MainSytle"));
			}		
			serverUI.doc.insertString(serverUI.doc.getLength(), ":"+ msg + "\n", serverUI.sc.getStyle("MainSytle"));
		}
		serverUI.fldChat.setText("");
	}
	
	public void broadcastImage() { //이미지 파일 전체 전송 처리
		FileDialog fd = new FileDialog(serverUI, "Select Image", FileDialog.LOAD);
		URL url = null;
	    fd.setVisible(true);
	    String filename = fd.getDirectory()+fd.getFile();
	    System.out.println(filename);
	    File f = new File(filename);
	    try {
			url = f.toURI().toURL();
		} catch (MalformedURLException exception) {			
			exception.printStackTrace();			
		}
	    
	    ImageIcon icon = new ImageIcon(url);	
	    ChatData cd = new ChatData(ChatType.Broadcast, "Image from Server");
	    //server.broadcast("Image from Server");
	    server.broadcast(cd);
	    cd.type = ChatType.Image; cd.data = icon;
	    server.broadcast(cd);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==serverUI.btnSvrStart) {
			btnSvrStartAction();
		}else if(e.getSource()==serverUI.btnBroadcast || e.getSource() == serverUI.fldChat) {
			ChatData cd = new ChatData(ChatType.Broadcast, "[Broadcast] :", serverUI.fldChat.getText());
			server.broadcast(cd);
			serverUI.fldChat.setText("");
		}else if(e.getSource()==serverUI.btnPersonalMsg) {
			try {
				btnPersonalMsgAction();
			} catch (BadLocationException e1) {				
				e1.printStackTrace();
			}
		}else if(e.getSource() == serverUI.photoMenuItem) {			
			broadcastImage();
		}
	}
	
	public ServerAction(ServerUI serverUI) {
		this.serverUI = serverUI;
		if(serverUI.debug == true) btnSvrStartAction();
	}
	
	public Server getServer() {
		return server;
	}
	
}

