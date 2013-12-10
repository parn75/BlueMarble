package bacnkup1129;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
//GUI
//event
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;

class Client extends Thread {
	Socket socket ; //서버와 통신
	ClientUI clientUI;

	ObjectOutputStream oos;
	ObjectInputStream ois;

	public Client(ClientUI clientUI){		//객체 생성 및 배치
		this.clientUI = clientUI;
		try{ //서버와 연결하는 구문
			socket = new Socket("localhost", 5432);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		}catch (Exception e){
			e.getMessage();
		}
	}
	
	public void showImage(Object obj){
		System.out.println("Got ImageIcon");
		ImageIcon icon = (ImageIcon)obj;	    
	    StyleConstants.setIcon(clientUI.chatStyle.imageStyle, icon);
	    
	    try {	    	
	    	clientUI.doc.insertString(clientUI.doc.getLength(), "ignored text\n", clientUI.chatStyle.imageStyle);	    	   	
		} catch (BadLocationException e1) {		
			e1.printStackTrace();
		}	    
	    //clientUI.endScroll();		
	}

	@Override
	public void run(){
		try{ 

			Object obj=null;
			
			while((obj=ois.readObject())!=null) {				
				if(obj instanceof String) {
					//clientUI.txtChat.append((String)obj + "\n");	
					try {
						clientUI.doc.insertString(clientUI.doc.getLength(), (String)obj + "\n", clientUI.sc.getStyle("MainSytle"));
					} catch (BadLocationException e) {						
						e.printStackTrace();
					}
				}else if (obj instanceof String[]) {		
					String[] str = (String[])obj;   
					List<String> l = clientUI.lstConnector.getSelectedValuesList(); //선택되어 있던 명단을 기억
					
					clientUI.listModel.clear();  //현재 리스트 목록 삭제
					int[] indices = new int[l.size()];
					for(int i=0;i<indices.length;i++) indices[i] = -1;  
					int index=0;
					for(int i=0;i<str.length;i++) {
						clientUI.listModel.addElement(str[i]);
						for(String s:l) if(s.equals(str[i])) indices[index++] = i; //connector						
					}
					clientUI.lstConnector.setSelectedIndices(indices); //선택되어 있던 명단을 복원하여 다시 선택 상태로 전환
				}else if(obj instanceof ImageIcon) {
					showImage(obj);
				}
				clientUI.endScroll();
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