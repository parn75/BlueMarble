package backup1128afterclass;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;

public class ServerAction implements ActionListener{
	private ServerUI serverUI;
	Server server;
	
	private void btnSvrStartAction() { //���� ���� ��ư �׼� ó��
		server = new Server(serverUI);
		server.start();
		SendConnectorList scl = new SendConnectorList(server, serverUI);
		Timer timer = new Timer();
		timer.schedule(scl, 1000, 5000); //5�ʿ� �ѹ��� ������ ��� ����
		serverUI.btnSvrStart.setText("Server Has Been Started!");
		serverUI.btnSvrStart.setEnabled(false);
	}
	
	private void btnPersonalMsgAction() throws BadLocationException { //�ӼӸ� ��ư �׼� ó��
		List<String> l = serverUI.lstConnector.getSelectedValuesList();			
		String[] str = new String[l.size()];
		l.toArray(str);
		String msg = serverUI.fldChat.getText();
		if(l.contains("To All")) {  //To All�� ���õǾ� ������ ��ü �޼��� ���� �� ����
			server.broadcast("[Broadcast] : " + msg);
			return;
		} else server.send(str, msg, "Server: ");
		if(l.size()>0) {
			//serverUI.txtChat.append("to ");
			serverUI.doc.insertString(serverUI.doc.getLength(), "to ", serverUI.sc.getStyle("MainSytle"));
			for(int i=0;i<str.length;i++) {
				//serverUI.txtChat.append(str[i] + ","); //to Str�߰�, Ex: to client1, client2, ...
				serverUI.doc.insertString(serverUI.doc.getLength(), str[i] + ",", serverUI.sc.getStyle("MainSytle"));
			}			
			//serverUI.txtChat.append(":"+ msg + "\n");
			serverUI.doc.insertString(serverUI.doc.getLength(), ":"+ msg + "\n", serverUI.sc.getStyle("MainSytle"));
		}
		serverUI.fldChat.setText("");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==serverUI.btnSvrStart) {
			btnSvrStartAction();
		}else if(e.getSource()==serverUI.btnBroadcast) {
			server.broadcast("[Broadcast] : " + serverUI.fldChat.getText());
			serverUI.fldChat.setText("");		
		}else if(e.getSource()==serverUI.btnPersonalMsg) {
			try {
				btnPersonalMsgAction();
			} catch (BadLocationException e1) {				
				e1.printStackTrace();
			}
		}else if(e.getSource() == serverUI.photoMenuItem) {			
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
		    String[] strary = {"To All"};		    
		    server.broadcast("Image from Server");
		    server.broadcast(icon);
		}
	}
	
	public ServerAction(ServerUI serverUI) {
		this.serverUI = serverUI;
	}
	
}

