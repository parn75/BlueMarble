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
	
	public String[] getTo() { //����Ʈ���� ���õ� ����� String[]�� ����
		List<String> l = clientUI.lstConnector.getSelectedValuesList();
		String[] to = new String[l.size()];
		l.toArray(to);
		return to;		
	}
	
	public void btnConnectAction() {
		client = new Client(clientUI, clientUI.hostname.getText());
		client.start(); //Ŭ���̾�Ʈ ������ ����
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

		clientUI.doc.insertString(clientUI.doc.getLength(), "������ �����Ͽ����ϴ�. \n", clientUI.sc.getStyle("MainSytle"));

	}
	
	public void fileMenuItemAction() throws BadLocationException, UnknownHostException {
		FileDialog fd = new FileDialog(clientUI, "������ ������ �����ϼ���.", FileDialog.LOAD);		
		fd.setVisible(true);
		String filename = fd.getDirectory()+fd.getFile();
		System.out.println(filename);
		if(filename.equals("") || fd.getDirectory() == null || fd.getFile() == null ) return;

		String[] to = getTo();

		if(to.length>1) {		    	
			clientUI.doc.insertString(clientUI.doc.getLength(), "������ �� ���� �Ѹ��Ը� ������ �� �ֽ��ϴ�. \n", clientUI.sc.getStyle("MainSytle"));
			return;
		}else if(to.length==0) {
			clientUI.doc.insertString(clientUI.doc.getLength(), "������ ����� �������ּ���. \n", clientUI.sc.getStyle("MainSytle"));
			return;
		}

		FileTransferServer fts = new FileTransferServer(filename); //���� ���� ������ ����, ����
		fts.start();	

		InetAddress myAddr = InetAddress.getLocalHost();  //������ ���� ������� �˸��� ����
		Object[] data = new Object[2];  //������ ������ ����� �ּҿ� ���� �̸�
		data[0] = myAddr;
		data[1] = fd.getFile();
		//ChatData cd = new ChatData(ChatType.File, to, myAddr);
		ChatData cd = new ChatData(ChatType.File, to, data);
		client.send(cd);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==clientUI.btnConnect || e.getSource() == clientUI.fldID) {  //���� ��ư�� Ŭ���� ���
			btnConnectAction();
		}else if(e.getSource()==clientUI.btnPersonalMsg || e.getSource()==clientUI.fldChat) { //�ӼӸ�			
			btnPersonalMsgAction();
		}else if(e.getSource() == clientUI.photoMenuItem) { //���� ����			
			try {
				photoMenuItemAction();
			} catch (MalformedURLException | BadLocationException e1) {				
				e1.printStackTrace();
			}
		}else if(e.getSource() == clientUI.fileMenuItem) { //���� ����
			try {
				fileMenuItemAction();
			} catch (UnknownHostException | BadLocationException e1) {				
				e1.printStackTrace();
			}
		}
		
	}

}
