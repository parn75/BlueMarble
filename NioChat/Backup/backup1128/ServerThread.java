package backup1128;

import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.text.Caret;

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

	//Ŭ���̾�Ʈ�κ��� �޼����� �޴� ������
	@Override
	public void run(){
		try{
			Object obj=null;
			while((obj=ois.readObject())!=null) {
				if(obj instanceof String) { //ù ���ӿ��� String ��ü ���
					String str = (String)obj;
					if (str.startsWith("*****[")) {  //Ŭ���̾�Ʈ���� ���� ���ӽ� ������ ��� *****[ID:REALID]***** 
						identifyID(str);
					}else {
						serverUI.txtChat.append("Log: String Object wihout header from " + this.getName() +" : " + (String)obj + "\n");  //String ��ü�� �������� ����� ���°�� ������ ǥ��
					}
				}else if(obj instanceof ChatData) { //�ӼӸ��� ��� ChatData ��ü�� �Ѿ��
					toSelected(obj);
				}else if(obj instanceof ImageIcon) {
					System.out.println("Got ImageIcon");
					ImageIcon img = (ImageIcon)obj;
					Image photo = img.getImage();
					photo = photo.getScaledInstance(50, 50, Image.SCALE_AREA_AVERAGING); //get scaled image
					img.setImage(photo);
					Caret c = serverUI.txtChat.getCaret();
					//JButton btnImg = new JButton(img);
					
					//serverUI.txtChat.add(btnImg);
					img.paintIcon(serverUI.txtChat, serverUI.txtChat.getGraphics(), 100, 100);
					//serverUI.txtChat.add(img) //�̹����� ä��â�� ǥ���ϱ� ���ؼ��� TextArea�� JEditorPane or JTextPane���� ���� �ʿ�?
				}
			}				
		}catch (IOException | ClassNotFoundException e){			
			server.removeThread( this.getName() ); //������ ���� ���(���� �� ���� ���) ������ �� ����Ʈ ����
			server.broadcast("[" + this.getName() + "]���� �����̽��ϴ�.");
			System.out.println(socket.getInetAddress() + "�� ������ ����Ǿ����ϴ�.");
		
		}
		
	}
}
