package backup1128;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
//GUI
//event
import java.util.List;

class Client extends Thread {
	private static final long serialVersionUID = 1L;
	Socket socket ; //������ ���

	ObjectOutputStream oos;
	ObjectInputStream ois;

	public Client(){		//��ü ���� �� ��ġ
		try{ //������ �����ϴ� ����
			socket = new Socket("localhost", 5432);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		}catch (Exception e){
			e.getMessage();
		}
	}

	@Override
	public void run(){
		try{ 

			Object obj=null;			
			
			while((obj=ois.readObject())!=null) {				
				if(obj instanceof String) {
					ClientUI.txtChat.append((String)obj + "\n");				
				}else if (obj instanceof String[]) {					
					String[] str = (String[])obj;   
					List<String> l = ClientUI.lstConnector.getSelectedValuesList(); //���õǾ� �ִ� ����� ���
					
					ClientUI.listModel.clear();  //���� ����Ʈ ��� ����
					int[] indices = new int[l.size()];
					for(int i=0;i<indices.length;i++) indices[i] = -1;  
					int index=0;
					for(int i=0;i<str.length;i++) {
						ClientUI.listModel.addElement(str[i]);
						for(String s:l) if(s.equals(str[i])) indices[index++] = i; //connector						
					}
					ClientUI.lstConnector.setSelectedIndices(indices); //���õǾ� �ִ� ����� �����Ͽ� �ٽ� ���� ���·� ��ȯ
				}
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
	
	public static void main(String[] args){
		new Client();
	}
}