package backup1205;

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
	}
	
	public String[] getTo() { //리스트에서 선택된 목록을 String[]로 리턴
		List<String> l = clientUI.lstConnector.getSelectedValuesList();
		String[] to = new String[l.size()];
		l.toArray(to);
		return to;		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==clientUI.btnConnect || e.getSource() == clientUI.fldID) {  //접속 버튼을 클릭한 경우
			client = new Client(clientUI, clientUI.hostname.getText());
			client.start(); //클라이언트 스레드 시작
			ChatData cd = new ChatData(ChatType.ID, clientUI.fldID.getText());
			client.send(cd);			
			clientUI.fldID.setEnabled(false);
			clientUI.btnConnect.setEnabled(false);
		}else if(e.getSource()==clientUI.btnPersonalMsg || e.getSource()==clientUI.fldChat) { //귓속말			
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
		}else if(e.getSource() == clientUI.photoMenuItem) { //사진 전송			
			FileDialog fd = new FileDialog(clientUI, "Select Image", FileDialog.LOAD);
			URL url = null;
		    fd.setVisible(true);
		    String filename = fd.getDirectory()+fd.getFile();		   
		    File f = new File(filename);
		    try {
				url = f.toURI().toURL();
			} catch (MalformedURLException exception) {			
				exception.printStackTrace();
			}
		    ImageIcon icon = new ImageIcon(url);
		    String[] to = getTo();
		    ChatData cd = new ChatData(ChatType.Image, to, icon);
		    client.send(cd);
		    try {
				clientUI.doc.insertString(clientUI.doc.getLength(), "사진을 전송하였습니다. \n", clientUI.sc.getStyle("MainSytle"));
			} catch (BadLocationException e1) {				
				e1.printStackTrace();
			}
		}else if(e.getSource() == clientUI.fileMenuItem) { //파일 전송
			FileDialog fd = new FileDialog(clientUI, "전송할 파일을 선택하세요.", FileDialog.LOAD);		
		    fd.setVisible(true);
		    String filename = fd.getDirectory()+fd.getFile();
		    FileTransferServer fts = new FileTransferServer(filename);
		    fts.start();		  
		    String[] to = getTo();
		    
		    try {
		    	if(to.length>1) {		    	
					clientUI.doc.insertString(clientUI.doc.getLength(), "파일은 한 번에 한명에게만 전송할 수 있습니다. \n", clientUI.sc.getStyle("MainSytle"));
					return;
		    	}else if(to.length==0) {
		    		clientUI.doc.insertString(clientUI.doc.getLength(), "전송할 사람을 선택해주세요. \n", clientUI.sc.getStyle("MainSytle"));
		    		return;
		    	}
		    } catch (BadLocationException e1) {				
				e1.printStackTrace();
			}
			try {
				InetAddress myAddr = InetAddress.getLocalHost();
				ChatData cd = new ChatData(ChatType.File, to, myAddr);
				client.send(cd);
			} catch (UnknownHostException e1) {			
				e1.printStackTrace();
			}		    
		}
		
	}

}
