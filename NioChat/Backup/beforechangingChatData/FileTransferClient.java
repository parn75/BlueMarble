package beforechangingChatData;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class FileTransferClient extends Thread{
	Socket socket ; //������ ���
	String saveFileName;
	
	public FileTransferClient(String saveFileName) {
		this.saveFileName = saveFileName;
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket("localhost", 6432);
			BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());  //��Ʈ�p���� ������ �о
			
			File wfile = new File(saveFileName); //���� ���Ϸ� ����
			FileOutputStream fos = new FileOutputStream(wfile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);	//
			
			int i=0;
			while(i != -1) {				
				byte[] b = new byte[1024];
				i = bis.read(b);						
				bos.write(b);
				bos.flush();
			}
			fos.close();
			bos.close();
			bis.close();
			socket.close();
			System.out.println("���� ���� �Ϸ�!");
			interrupt();
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {
		FileTransferClient fc = new FileTransferClient("d:/111");
		fc.start();
	}
}
