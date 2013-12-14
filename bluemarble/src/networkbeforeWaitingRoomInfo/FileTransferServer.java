package networkbeforeWaitingRoomInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileTransferServer extends Thread{
	ServerSocket ss ; //접속하기위해서 
	Socket socket ; //클라이언트와 통신
	File file;

	public FileTransferServer(String filename) {
		this.file = new File(filename);
	}

	@Override
	public void run() {
		try{
			ss = new ServerSocket(6432);
			System.out.println("---파일 서버 가동중---");

			FileInputStream fis = new FileInputStream(file);		

			socket = ss.accept(); //클라이언트의 요청이 들어 오면 소켓이 만들어 진다.
			System.out.println("Accepted from " + socket);
			BufferedInputStream bis = new BufferedInputStream(fis);  //파일 인풋
			BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());	//소켓 아웃풋	
			
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
			e.printStackTrace();//관련된 에러 전부 출력
		} //catch		
	}
}
