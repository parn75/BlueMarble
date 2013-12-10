package backup1202_FileTransfer;

import java.awt.FileDialog;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

//Ŭ���̾�Ʈ�� ���->������ �ʿ�
class ServerThread extends Thread{
	ServerUI serverUI;
	Server server;//������ ����
	Socket socket ; //Ŭ���̾�Ʈ
	ObjectOutputStream oos;
	ObjectInputStream ois;

	public ServerThread(Server server, Socket socket, ServerUI serverUI){
		this.server = server ; //������ ����
		this.socket = socket ; //client
		this.serverUI = serverUI;

		try{
		    oos = new ObjectOutputStream(socket.getOutputStream());
		    ois = new ObjectInputStream(socket.getInputStream());
		}catch (IOException ioe){
			ioe.getMessage();
		}
	}//������

	//Ŭ���̾�Ʈ���� ��ü�� �������ִ� �޼ҵ�	(�ؽ�Ʈ�� ��쿡�� String ��ü�� ����)
	public void send(Object obj) {
		try {
			oos.writeObject(obj);
			oos.flush();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void toSelected(Object obj) { //����Ʈ���� ���õ� Ŭ���̾�Ʈ���Ը� �޼��� ����
		ChatData chatdata = (ChatData)obj;		
		if(chatdata!=null && chatdata.to.length>0) {   //chatdata�� to �ʵ忡 ���𰡰� ���� ��� => Ŭ���̾�Ʈ���� �޼��� ���۽� ����Ʈ���� ���� ����� ���� => �� �ӼӸ��� ���
			for(int i=0;i<chatdata.to.length;i++) {
				if(chatdata.to[i].equals("To All")) {	//To ALL�� ���õǾ� �ִ� ��� ��ü �޼��� ���� �� ��������		
					server.broadcast(this.getName() + " : " + chatdata.data);
					return;
				}
			}
			server.send(chatdata.to, chatdata.data, this.getName());
		}
		else{  //��ü �޼���
			System.out.println("sending broad");
			server.broadcast(this.getName() + " : " + chatdata.data);			
		}	
	}
	
	public void identifyID(String str) { //ID�� �߷����� ó���ϴ� �޼ҵ�
		String ID = str.substring(9, str.lastIndexOf("]"));  //<= REALID �κ��� �и��Ͽ� ID�� ����
		serverUI.listModel.addElement(ID);  //����Ʈ�� ID�߰�
		this.setName(ID); //Thread�� �̸��� ID�� ���� => ID�ߺ� �Ұ�
		server.clientMap.put(ID, this);  
		server.reverseClientMap.put(this, ID);
		server.sendConnectorList(); //�� Ŭ���̾�Ʈ�� ���� ������ ����Ʈ ����
		server.broadcast("["+ID+"]���� �����ϼ̽��ϴ�.");
	}
	
	public void getFile(String filename, String from) {
		int confirm = JOptionPane.showConfirmDialog(serverUI, from+"���� ������ �����̽��ϴ�. �����Ͻðڽ��ϱ�?", "choose one", JOptionPane.YES_NO_OPTION);
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
					serverUI.doc.insertString(serverUI.doc.getLength(), "������ ��ҵǾ����ϴ�!", serverUI.sc.getStyle("MainSytle"));
				} catch (BadLocationException e) {					
					e.printStackTrace();
				}
		    }		   
		}
	}

	//Ŭ���̾�Ʈ�κ��� �޼����� �޴� ������
	@Override
	public void run(){
		try{
			Object obj=null;
			while((obj=ois.readObject())!=null) {
				if(obj instanceof String) { //ù ���ӿ��� String ��ü ���
					String str = (String)obj;
					if (str.startsWith("*****[ID:")) {  //Ŭ���̾�Ʈ���� ���� ���ӽ� ������ ��� *****[ID:REALID]***** 
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
				}else if(obj instanceof ChatData) { //�ӼӸ��� ��� ChatData ��ü�� �Ѿ��
					toSelected(obj);
				}else if(obj instanceof ImageIcon) {					
					server.broadcast("Image from " + this.getName());
					server.broadcast(obj);
				}
			}				
		}catch (IOException | ClassNotFoundException | BadLocationException e){			
			server.removeThread( this.getName() ); //������ ���� ���(���� �� ���� ���) ������ �� ����Ʈ ����
			server.broadcast("[" + this.getName() + "]���� �����̽��ϴ�.");
			System.out.println(socket.getInetAddress() + "�� ������ ����Ǿ����ϴ�.");
		
		}			
	}
}
