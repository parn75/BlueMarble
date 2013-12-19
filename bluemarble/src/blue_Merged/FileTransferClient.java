package blue_Merged;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class FileTransferClient extends Thread{
	Socket socket ; //서버와 통신
	String saveFileName;
	InetAddress host;
	
	public FileTransferClient(InetAddress host, String saveFileName) {
		this.host = host;
		this.saveFileName = saveFileName;		
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket(host, 6432);
			BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());  //네트웤에서 파일을 읽어서
			
			File wfile = new File(saveFileName); //로컬 파일로 저장
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
			System.out.println("파일 저장 완료!");
			interrupt();
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}
}
