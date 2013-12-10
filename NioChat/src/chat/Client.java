package chat;

import java.awt.FileDialog;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
//GUI
//event
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;

class Client extends Thread {
	Socket socket ; //������ ���
	ClientUI clientUI;
	String hostname;

	ObjectOutputStream oos;
	ObjectInputStream ois;

	public Client(ClientUI clientUI, String hostname){		//��ü ���� �� ��ġ
		this.clientUI = clientUI;
		this.hostname = hostname;
		try{ //������ �����ϴ� ����			
			socket = new Socket(hostname, 5432);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		}catch (Exception e){
			e.getMessage();
		}
	}
	
	public void showImage(ChatData cd){
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
	
	public void getFile(ChatData cd) { //������ �޴� �޼ҵ�
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

	@Override
	public void run(){
		try{ 

			Object obj=null;
			
			while((obj=ois.readObject())!=null) {	
				if(obj instanceof ChatData) {
					ChatData cd = (ChatData)obj;
					
					try {
						switch(cd.type) {							
							case ConnectorList:	updateConnectorList(cd);
								break;
							case File:getFile(cd);
								break;
							case ID:
								break;
							case Image: showImage(cd);
								break;
							case Broadcast: clientUI.doc.insertString(clientUI.doc.getLength(), cd.from + (String)cd.data + "\n", clientUI.sc.getStyle("MainSytle"));
								break;
							case Whisper: clientUI.doc.insertString(clientUI.doc.getLength(), cd.from + (String)cd.data + "\n", clientUI.sc.getStyle("WhisperStyle"));
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

	public void send(Object obj) {
		try {
			oos.writeObject(obj);			
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}	
}