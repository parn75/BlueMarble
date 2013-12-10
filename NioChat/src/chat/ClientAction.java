package chat;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;

public class ClientAction implements ActionListener{
	ClientUI clientUI;
	Client client;
	
	public ClientAction(ClientUI clientUI) {
		this.clientUI = clientUI;
		if(clientUI.debug == true) {
			int i = (int) (Math.random()*1000);			
			clientUI.hostname.setText("localhost");
			clientUI.fldID.setText("Client" + i);
			btnConnectAction();
		}
	}
	
	public String[] getTo() { //리스트에서 선택된 목록을 String[]로 리턴
		List<String> l = clientUI.lstConnector.getSelectedValuesList();
		String[] to = new String[l.size()];
		l.toArray(to);
		return to;		
	}
	
	public void btnConnectAction() {
		client = new Client(clientUI, clientUI.hostname.getText());
		client.start(); //클라이언트 스레드 시작
		ChatData cd = new ChatData(ChatType.ID, clientUI.fldID.getText());
		client.send(cd);
		clientUI.hostname.setEditable(false);
		clientUI.fldID.setEnabled(false);
		clientUI.btnConnect.setEnabled(false);
	}
	
	public void btnPersonalMsgAction() {
		String[] to = getTo();			
		String msg = clientUI.fldChat.getText();		
		if((to.length>0 && to[0].equals("To All")) || to.length == 0) client.send(new ChatData(ChatType.Broadcast,to,msg));
		else {
			client.send(new ChatData(ChatType.Whisper,to,msg));
			System.out.println("귓속말 보내기 " + to[0] );
			try {
				clientUI.doc.insertString(clientUI.doc.getLength(), "to ", clientUI.sc.getStyle("WhisperStyle"));
				for(int i=0;i<to.length-1;i++) 
					clientUI.doc.insertString(clientUI.doc.getLength(), to[i] + ",", clientUI.sc.getStyle("WhisperStyle"));
				clientUI.doc.insertString(clientUI.doc.getLength(), to[to.length-1] + ":" + msg + "\n ", clientUI.sc.getStyle("WhisperStyle"));
			} catch (BadLocationException e1) {					
				e1.printStackTrace();
			}
			clientUI.endScroll();				
		}
		clientUI.fldChat.setText("");
	}
	
	public void photoMenuItemAction() throws MalformedURLException, BadLocationException {
		FileDialog fd = new FileDialog(clientUI, "Select Image", FileDialog.LOAD);
		URL url = null;
		fd.setVisible(true);
		String filename = fd.getDirectory()+fd.getFile();		   
		File f = new File(filename);

		url = f.toURI().toURL();

		ImageIcon icon = new ImageIcon(url);
		String[] to = getTo();
		ChatData cd = new ChatData(ChatType.Image, to, icon);
		client.send(cd);

		clientUI.doc.insertString(clientUI.doc.getLength(), "사진을 전송하였습니다. \n", clientUI.sc.getStyle("MainSytle"));

	}
	
	public void fileMenuItemAction() throws BadLocationException, UnknownHostException {
		FileDialog fd = new FileDialog(clientUI, "전송할 파일을 선택하세요.", FileDialog.LOAD);		
		fd.setVisible(true);
		String filename = fd.getDirectory()+fd.getFile();
		System.out.println(filename);
		if(filename.equals("") || fd.getDirectory() == null || fd.getFile() == null ) return;

		String[] to = getTo();

		if(to.length>1) {		    	
			clientUI.doc.insertString(clientUI.doc.getLength(), "파일은 한 번에 한명에게만 전송할 수 있습니다. \n", clientUI.sc.getStyle("MainSytle"));
			return;
		}else if(to.length==0) {
			clientUI.doc.insertString(clientUI.doc.getLength(), "전송할 사람을 선택해주세요. \n", clientUI.sc.getStyle("MainSytle"));
			return;
		}

		FileTransferServer fts = new FileTransferServer(filename); //파일 서버 스레드 생성, 시작
		fts.start();	

		InetAddress myAddr = InetAddress.getLocalHost();  //파일을 받을 사람에게 알림을 보냄
		Object[] data = new Object[2];  //파일을 보내는 사람의 주소와 파일 이름
		data[0] = myAddr;
		data[1] = fd.getFile();
		//ChatData cd = new ChatData(ChatType.File, to, myAddr);
		ChatData cd = new ChatData(ChatType.File, to, data);
		client.send(cd);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==clientUI.btnConnect || e.getSource() == clientUI.fldID) {  //접속 버튼을 클릭한 경우
			btnConnectAction();
		}else if(e.getSource()==clientUI.btnPersonalMsg || e.getSource()==clientUI.fldChat) { //귓속말			
			btnPersonalMsgAction();
		}else if(e.getSource() == clientUI.photoMenuItem) { //사진 전송			
			try {
				photoMenuItemAction();
			} catch (MalformedURLException | BadLocationException e1) {				
				e1.printStackTrace();
			}
		}else if(e.getSource() == clientUI.fileMenuItem) { //파일 전송
			try {
				fileMenuItemAction();
			} catch (UnknownHostException | BadLocationException e1) {				
				e1.printStackTrace();
			}
		}
		
	}

}
