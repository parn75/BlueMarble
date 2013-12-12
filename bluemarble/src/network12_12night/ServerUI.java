package network12_12night;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;

public class ServerUI extends JFrame{
	boolean debug = true;
	private static final long serialVersionUID = -5472820133041236799L;
	JTextField hostInfo;
	JButton btnSvrStart = new JButton("Server Start");
	JPanel topP = new JPanel();
	JButton btnBroadcast = new JButton("To All");
	JButton btnPersonalMsg = new JButton("To Selected People");
	JTextField fldChat = new JTextField();	
	DefaultListModel<String> listModel = new DefaultListModel<String>();
	JList<String> lstConnector; 	
	JPanel pnlBroadcast = new JPanel();
	
	//채팅 및 이미지 표시 부분, 스크롤 설정 및 스타일 지정
	JPanel middleP = new JPanel();
	RoomListPanel roomList;
	StyleContext sc = new StyleContext();
    final DefaultStyledDocument doc = new DefaultStyledDocument(sc);
	JTextPane txtChat= new JTextPane(doc);
	JScrollPane sp;
	ChatDocStyles chatStyle;
	
	//메뉴바 설정
	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("파일");	
	JMenuItem photoMenuItem = new JMenuItem("사진 보내기");
	JMenuItem fileMenuItem = new JMenuItem("파일 전송");
	
	ServerAction svrAction;
	
	public void endScroll() {  //Scroll을 가장 아래로 내리기 위한 메소드		
		int pos = doc.getLength();		
		txtChat.setCaretPosition(pos);
		//txtChat.requestFocus();
	}
	
	private void start() {
		svrAction = new ServerAction(this);
		btnSvrStart.addActionListener(svrAction);
		btnBroadcast.addActionListener(svrAction);
		btnPersonalMsg.addActionListener(svrAction);
		photoMenuItem.addActionListener(svrAction);
		fldChat.addActionListener(svrAction);
		roomList.setServer(svrAction.server);
	}
	
	private void init() {
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		hostInfo = new JTextField();
		try {
			hostInfo.setText("Server Address: " + InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {			
			e.printStackTrace();
		}
		hostInfo.setEditable(false);
		topP.add(hostInfo);
		topP.add(btnSvrStart);
		con.add(topP, "North");
		
		middleP.setLayout(new GridLayout(2,1));
		txtChat.setEditable(false);
		middleP.add(txtChat);
		roomList = new RoomListPanel();		
		sp = new JScrollPane(txtChat);  //Adding Scroll Bar		

		middleP.add(roomList);	
		middleP.add(sp);
		con.add(middleP, "Center");	

		pnlBroadcast.setLayout(new BoxLayout(pnlBroadcast,BoxLayout.X_AXIS));
		pnlBroadcast.add(fldChat);
		pnlBroadcast.add(btnPersonalMsg);
		pnlBroadcast.add(btnBroadcast);	
		con.add(pnlBroadcast,"South");		
		
		listModel.addElement("To All");
		lstConnector = new JList<String>(listModel);
		con.add(lstConnector, "East");
		lstConnector.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		fileMenu.add(photoMenuItem);
		fileMenu.add(fileMenuItem);
		menuBar.add(fileMenu);		
		this.setJMenuBar(menuBar);
		
	}
	
	public ServerUI(String title) {
		super(title);
		init();
		start();
		this.setSize(500,550);
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationByPlatform(true);		
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ServerUI sUI = new ServerUI("Server");
	}

}
