package networkbeforeWaitingRoomInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileTransferServer extends Thread{
	ServerSocket ss ; //�����ϱ����ؼ� 
	Socket socket ; //Ŭ���̾�Ʈ�� ���
	File file;

	public FileTransferServer(String filename) {
		this.file = new File(filename);
	}

	@Override
	public void run() {
		try{
			ss = new ServerSocket(6432);
			System.out.println("---���� ���� ������---");

			FileInputStream fis = new FileInputStream(file);		

			socket = ss.accept(); //Ŭ���̾�Ʈ�� ��û�� ��� ���� ������ ����� ����.
			System.out.println("Accepted from " + socket);
			BufferedInputStream bis = new BufferedInputStream(fis);  //���� ��ǲ
			BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());	//���� �ƿ�ǲ	
			
			int i=0;
			while(i != -1) {
				//i = ois.read();
				byte[] b = new byte[1024];
				i = bis.read(b);						
				bos.write(b);
				bos.flush();
			}
			
			fis.close();
			bis.close();
			bos.close();
			socket.close();
			interrupt();
		}catch(IOException e){ 			
			e.printStackTrace();//���õ� ���� ���� ���
		} //catch		
	}
}
