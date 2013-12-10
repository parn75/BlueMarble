package oldchat;

import java.net.*;
import java.io.*;
import java.awt.*;//GUI
import java.awt.event.*;//event

class Client extends Frame implements Runnable, ActionListener {
	private static final long serialVersionUID = 1L;
	
	TextArea ta; //클라이언트의 대화를 출력
	TextField tf; //입력
	Socket socket ; //서버와 통신

	PrintWriter pw;
	BufferedReader br;

	public Client(){
		//객체 생성 및 배치
		ta = new TextArea();
		tf = new TextField();
		super.add(ta, "Center") ; //중앙
		super.add(tf, "South") ; //아래
		tf.addActionListener(this);
		super.setBounds(200,200,400,300);
		super.setVisible(true);
		tf.requestFocus();//커서 입력

		addWindowListener(new WindowAdapter(){ //종료
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});	   
		try{ //서버와 연결하는 구문
			socket = new Socket("localhost", 5432);

			pw = new PrintWriter(socket.getOutputStream(), true);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}catch (Exception e){
			e.getMessage();
		}

		//쓰레드가동
		Thread ct = new Thread(this);//ChatGUIClient
		ct.start();//run()
	}
	public void actionPerformed(ActionEvent e){
        String str = tf.getText();//textfield에 입력한값	
		if(str.length() > 0){
			pw.println(str) ; //서버 → 각 클라이언트
		}		
		tf.setText( "" );
	}
	public void run(){
		try{  //각 클라이언트에게 전달받은 문자열
			String str1 = null ;
			while(( str1 = br.readLine() ) != null){
				if( str1.indexOf("size|") >= 0){ //이 경우에는 창의 타이틀에 입력
					super.setTitle("현재 접속 인원 : " + str1.substring("size|".length()) + "명") ;
				}else{
					ta.append(str1+'\n');
				}				
			}
		}catch (IOException e){
			e.getMessage();
		}
	}
	public static void main(String[] args){
		new Client();
	}
}