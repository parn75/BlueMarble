package beforechangingChatData;

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
	}
	
	private void btnPersonalMsgAction() throws BadLocationException { //귓속말 버튼 액션 처리
		List<String> l = serverUI.lstConnector.getSelectedValuesList();			
		String[] str = new String[l.size()];
		l.toArray(str);
		String msg = serverUI.fldChat.getText();
		if(l.contains("To All")) {  //To All이 선택되어 있으면 전체 메세지 전송 후 리턴
			server.broadcast("[Broadcast] : " + msg);
			return;
		} else server.send(str, msg, "Server: ");
		if(l.size()>0) {			
			serverUI.doc.insertString(serverUI.doc.getLength(), "to ", serverUI.sc.getStyle("MainSytle"));
			for(int i=0;i<str.length;i++) {
				 //to Str추가, Ex: to client1, client2, ...
				serverUI.doc.insertString(serverUI.doc.getLength(), str[i] + ",", serverUI.sc.getStyle("MainSytle"));
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
	    server.broadcast("Image from Server");
	    server.broadcast(icon);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==serverUI.btnSvrStart) {
			btnSvrStartAction();
		}else if(e.getSource()==serverUI.btnBroadcast || e.getSource() == serverUI.fldChat) {
			server.broadcast("[Broadcast] : " + serverUI.fldChat.getText());
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
	}
	
}

