package oldchat;

import java.net.*;
import java.io.*;
import java.awt.*;//GUI
import java.awt.event.*;//event

class Client extends Frame implements Runnable, ActionListener {
	private static final long serialVersionUID = 1L;
	
	TextArea ta; //Ŭ���̾�Ʈ�� ��ȭ�� ���
	TextField tf; //�Է�
	Socket socket ; //������ ���

	PrintWriter pw;
	BufferedReader br;

	public Client(){
		//��ü ���� �� ��ġ
		ta = new TextArea();
		tf = new TextField();
		super.add(ta, "Center") ; //�߾�
		super.add(tf, "South") ; //�Ʒ�
		tf.addActionListener(this);
		super.setBounds(200,200,400,300);
		super.setVisible(true);
		tf.requestFocus();//Ŀ�� �Է�

		addWindowListener(new WindowAdapter(){ //����
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});	   
		try{ //������ �����ϴ� ����
			socket = new Socket("localhost", 5432);

			pw = new PrintWriter(socket.getOutputStream(), true);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}catch (Exception e){
			e.getMessage();
		}

		//�����尡��
		Thread ct = new Thread(this);//ChatGUIClient
		ct.start();//run()
	}
	public void actionPerformed(ActionEvent e){
        String str = tf.getText();//textfield�� �Է��Ѱ�	
		if(str.length() > 0){
			pw.println(str) ; //���� �� �� Ŭ���̾�Ʈ
		}		
		tf.setText( "" );
	}
	public void run(){
		try{  //�� Ŭ���̾�Ʈ���� ���޹��� ���ڿ�
			String str1 = null ;
			while(( str1 = br.readLine() ) != null){
				if( str1.indexOf("size|") >= 0){ //�� ��쿡�� â�� Ÿ��Ʋ�� �Է�
					super.setTitle("���� ���� �ο� : " + str1.substring("size|".length()) + "��") ;
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