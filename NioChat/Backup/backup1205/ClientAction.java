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
	
	public String[] getTo() { //����Ʈ���� ���õ� ����� String[]�� ����
		List<String> l = clientUI.lstConnector.getSelectedValuesList();
		String[] to = new String[l.size()];
		l.toArray(to);
		return to;		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==clientUI.btnConnect || e.getSource() == clientUI.fldID) {  //���� ��ư�� Ŭ���� ���
			client = new Client(clientUI, clientUI.hostname.getText());
			client.start(); //Ŭ���̾�Ʈ ������ ����
			ChatData cd = new ChatData(ChatType.ID, clientUI.fldID.getText());
			client.send(cd);			
			clientUI.fldID.setEnabled(false);
			clientUI.btnConnect.setEnabled(false);
		}else if(e.getSource()==clientUI.btnPersonalMsg || e.getSource()==clientUI.fldChat) { //�ӼӸ�			
			String[] to = getTo();			
			String msg = clientUI.fldChat.getText();		
			if((to.length>0 && to[0].equals("To All")) || to.length == 0) client.send(new ChatData(ChatType.Broadcast,to,msg));
			else {
				client.send(new ChatData(ChatType.Whisper,to,msg));
				System.out.println("�ӼӸ� ������ " + to[0] );
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
		}else if(e.getSource() == clientUI.photoMenuItem) { //���� ����			
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
				clientUI.doc.insertString(clientUI.doc.getLength(), "������ �����Ͽ����ϴ�. \n", clientUI.sc.getStyle("MainSytle"));
			} catch (BadLocationException e1) {				
				e1.printStackTrace();
			}
		}else if(e.getSource() == clientUI.fileMenuItem) { //���� ����
			FileDialog fd = new FileDialog(clientUI, "������ ������ �����ϼ���.", FileDialog.LOAD);		
		    fd.setVisible(true);
		    String filename = fd.getDirectory()+fd.getFile();
		    FileTransferServer fts = new FileTransferServer(filename);
		    fts.start();		  
		    String[] to = getTo();
		    
		    try {
		    	if(to.length>1) {		    	
					clientUI.doc.insertString(clientUI.doc.getLength(), "������ �� ���� �Ѹ��Ը� ������ �� �ֽ��ϴ�. \n", clientUI.sc.getStyle("MainSytle"));
					return;
		    	}else if(to.length==0) {
		    		clientUI.doc.insertString(clientUI.doc.getLength(), "������ ����� �������ּ���. \n", clientUI.sc.getStyle("MainSytle"));
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
