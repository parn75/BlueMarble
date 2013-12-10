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
	Socket socket ; //서버와 통신

	ObjectOutputStream oos;
	ObjectInputStream ois;

	public Client(){		//객체 생성 및 배치
		try{ //서버와 연결하는 구문
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
					List<String> l = ClientUI.lstConnector.getSelectedValuesList(); //선택되어 있던 명단을 기억
					
					ClientUI.listModel.clear();  //현재 리스트 목록 삭제
					int[] indices = new int[l.size()];
					for(int i=0;i<indices.length;i++) indices[i] = -1;  
					int index=0;
					for(int i=0;i<str.length;i++) {
						ClientUI.listModel.addElement(str[i]);
						for(String s:l) if(s.equals(str[i])) indices[index++] = i; //connector						
					}
					ClientUI.lstConnector.setSelectedIndices(indices); //선택되어 있던 명단을 복원하여 다시 선택 상태로 전환
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