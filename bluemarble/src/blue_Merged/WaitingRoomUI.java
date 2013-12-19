package blue_Merged;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;


public class WaitingRoomUI extends JFrame implements ActionListener, WindowListener{
	public boolean debug = true;
	private final int XSIZE = 500, YSIZE = 550;
	private static final long serialVersionUID = -8584629477552591872L;
	
	JLabel lblHost = new JLabel("Host Name");
	JButton gameStartB = new JButton("게임 시작");

	JButton btnPersonalMsg = new JButton("Send");	
	JTextField fldChat = new JTextField();
	
	JPanel pnlBroadcast = new JPanel();
	JPanel pnlConnect = new JPanel();
	
	//채팅 및 이미지 표시 부분, 스크롤 설정 및 스타일 지정
	BufferStrategy bs;
	JPanel middleP = new JPanel();
	JPanel middleP2 = new JPanel();
	StyleContext sc = new StyleContext();
    final DefaultStyledDocument doc = new DefaultStyledDocument(sc);
	JTextPane txtChat = new JTextPane(doc);
	JScrollPane sp;
	JScrollPane roomScroll;
	ChatDocStyles chatStyle;
	
	//메뉴바 설정
	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("파일");	
	JMenuItem photoMenuItem = new JMenuItem("사진 보내기");
	JMenuItem fileMenuItem = new JMenuItem("파일 전송");
	
	ClientAction clientAction;
	Client client;
	WaitingRoomInfo myRoomInfo;
	JLabel[] players = new JLabel[4];

	private void start() {		
		gameStartB.addActionListener(this);		
		btnPersonalMsg.addActionListener(this);
		photoMenuItem.addActionListener(this);
		fileMenuItem.addActionListener(this);		
		fldChat.addActionListener(this);		
	}
	
	public void endScroll() {  //Scroll을 가장 아래로 내리기 위한 메소드		
		int pos = doc.getLength();		
		txtChat.setCaretPosition(pos);
		//txtChat.requestFocus();
	}
	
	public BufferedImage drawRoom(String roomName, int firstColor, int medianColor) {
		//System.out.println(roomName+"의 length는 " + roomName.length() + "입니다.");
		int fontSize = 30;
		int roomHeight = 100;
		BufferedImage bi = new BufferedImage(XSIZE, roomHeight, BufferedImage.TYPE_INT_ARGB); 
		Graphics2D ig2 = bi.createGraphics();
		int c = 255/roomHeight;
		for(int i=0;i<=roomHeight;i++) {			
			Color color = new Color(c*i,firstColor , medianColor);
			ig2.setPaint(color);
			ig2.drawLine(0, i, XSIZE, i);
		}

	    ig2.setPaint(Color.black);
	    Font f = new Font("굴림", Font.BOLD, fontSize);
	    ig2.setFont(f);
	    int fx = (XSIZE - fontSize*roomName.length()/2)/2;
	    ig2.drawString(roomName, fx, roomHeight/2 + fontSize/2);
	    return bi;
	}
	
	private void init() {
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		//pnlConnect.setLayout(new BoxLayout(pnlConnect,BoxLayout.X_AXIS));
		pnlConnect.setLayout(new BorderLayout());
		Icon ic = new ImageIcon(drawRoom("방 이름",200, 200));
		lblHost.setIcon(ic);
		pnlConnect.add(lblHost, "North");
		pnlConnect.add(gameStartB, "South");
		con.add(pnlConnect, "North");
		
		middleP.setLayout(new GridLayout(5,1));	

		for(int i=0;i<4;i++) {
			players[i] = new JLabel();
			ic = new ImageIcon(drawRoom("None", 100, 130+30*i));
			players[i].setIcon(ic);
			middleP.add(players[i]);
			if(myRoomInfo!= null)
				if(myRoomInfo.getPlayerNames()[i]!=null) players[i].setText(myRoomInfo.getPlayerNames()[i].toString()); else players[i].setText("None");			
		}
		txtChat.setEditable(false);
		middleP.add(txtChat);
		sp = new JScrollPane(txtChat);  //Adding Scroll Bar		
		middleP.add(sp);
		con.add(middleP, "Center");
	
		chatStyle = new ChatDocStyles(sc);

		pnlBroadcast.setLayout(new BoxLayout(pnlBroadcast,BoxLayout.X_AXIS));		
		pnlBroadcast.add(fldChat);
		pnlBroadcast.add(btnPersonalMsg);		
		con.add(pnlBroadcast,"South");	
	}
	/*
	public void updateWaitingRoom(WaitingRoomInfo roomInfo) {
		System.out.println("updating waiting room");
		if (roomInfo==null) return;
		System.out.println("roomInfo is not null");
		System.out.println(roomInfo.getPlayerNum() +  roomInfo.getPlayerNames()[0].toString());
		for(int i=0;i<4;i++) {
			if(roomInfo.getPlayerNames()[i]!=null) players[i].setText(roomInfo.getPlayerNames()[i].toString()); else players[i].setText("None");		
		}
	}
	*/
	public void updateWaitingRoom(WaitingRoomInfo roomInfo) {		
		this.myRoomInfo = roomInfo;
		if (roomInfo==null) return;		
		
		Icon ic = new ImageIcon(drawRoom(roomInfo.getRoomName().toString(), 200, 200));
		lblHost.setIcon(ic);
		
		for(int i=0;i<4;i++) {
			ic = new ImageIcon(drawRoom(roomInfo.getPlayerNames()[i].toString(), 100, 170+20*i));
			players[i].setIcon(ic);			
		}
	}
	
	public WaitingRoomUI(WaitingRoomInfo roomInfo, Client client) {
		super(roomInfo.getRoomName().toString());
		this.myRoomInfo = roomInfo;
		this.client = client;
		init();
		start();
		this.setSize(XSIZE,YSIZE);
		this.setResizable(false);
		//this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationByPlatform(true);
		this.addWindowListener(this);
		setVisible(true);
	}
	
	public WaitingRoomUI(Client client) {
		//super(wrs.title);
		//this.wrs = wrs;
		this.client = client;
		init();
		start();
		this.setSize(XSIZE,YSIZE);
		this.setResizable(false);
		//this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationByPlatform(true);
		this.addWindowListener(this);
		setVisible(true);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnPersonalMsg) {					
			String msg = fldChat.getText();		
			client.send(new ChatData(ChatType.WaitingRoomChat,msg));	
			fldChat.setText("");
		}if(e.getSource()==gameStartB) {	
			System.out.println("게임이 시작됩니다.");
			ChatData cd = new ChatData();
			cd.setType(ChatType.GameStart);
			client.send(cd);
			//marble = new MabulEx();
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("windowClosing");
		client.startWaitingRoom = false;
		client.inWaitingRoom = false;
		client.clientUI.setVisible(true);
		client.send(new ChatData(ChatType.WaitingRoomExit,"대기실 나감"));
		this.dispose();
				
	}

	@Override
	public void windowClosed(WindowEvent e) {		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
