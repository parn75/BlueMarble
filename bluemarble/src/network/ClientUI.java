package network;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

public class ClientUI extends JFrame{
	public boolean debug = true;
	private final int XSIZE = 700, YSIZE = 700;
	private static final long serialVersionUID = -8584629477552591872L;
	
	JLabel lblHost = new JLabel("Host Name ");
	JTextField hostname = new JTextField();
	JLabel lblID = new JLabel("ID ");
	JTextField fldID = new JTextField();
	JButton btnConnect = new JButton("Connect");	
	JButton btnPersonalMsg = new JButton("Send");	
	JTextField fldChat = new JTextField();
	JList<String> lstConnector = new JList<String>();
	DefaultListModel<String> listModel = new DefaultListModel<String>();
	JPanel pnlBroadcast = new JPanel();
	JPanel pnlConnect = new JPanel();
	
	//ä�� �� �̹��� ǥ�� �κ�, ��ũ�� ���� �� ��Ÿ�� ����
	RoomList roomList;
	BufferStrategy bs;
	JPanel middleP = new JPanel();
	StyleContext sc = new StyleContext();
    final DefaultStyledDocument doc = new DefaultStyledDocument(sc);
	JTextPane txtChat = new JTextPane(doc);
	JScrollPane sp;
	JScrollPane roomScroll;
	ChatDocStyles chatStyle;
	
	//�޴��� ����
	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("����");	
	JMenuItem photoMenuItem = new JMenuItem("���� ������");
	JMenuItem fileMenuItem = new JMenuItem("���� ����");
	
	ClientAction clientAction;


	private void start() {
		clientAction = new ClientAction(this);
		btnConnect.addActionListener(clientAction);		
		btnPersonalMsg.addActionListener(clientAction);
		photoMenuItem.addActionListener(clientAction);
		fileMenuItem.addActionListener(clientAction);
		fldID.addActionListener(clientAction);
		fldChat.addActionListener(clientAction);
	}
	
	public void endScroll() {  //Scroll�� ���� �Ʒ��� ������ ���� �޼ҵ�		
		int pos = doc.getLength();		
		txtChat.setCaretPosition(pos);
		//txtChat.requestFocus();
	}
	
	private void init() {
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		pnlConnect.setLayout(new BoxLayout(pnlConnect,BoxLayout.X_AXIS));
		pnlConnect.add(lblHost);
		pnlConnect.add(hostname);
		pnlConnect.add(lblID);
		pnlConnect.add(fldID);
		pnlConnect.add(btnConnect);
		con.add(pnlConnect, "North");
		
		//middleP.setLayout(new BoxLayout(middleP,BoxLayout.Y_AXIS));	
		middleP.setLayout(new GridLayout(1,2));
		txtChat.setEditable(false);
		middleP.add(txtChat);
		roomList = new RoomList();		
		//con.add(txtChat,"Center");
		sp = new JScrollPane(txtChat);  //Adding Scroll Bar		
	    //con.add(sp);
		//createDocumentStyles();
		middleP.add(roomList);	
		middleP.add(sp);
		con.add(middleP, "Center");
		chatStyle = new ChatDocStyles(sc);

		pnlBroadcast.setLayout(new BoxLayout(pnlBroadcast,BoxLayout.X_AXIS));		
		pnlBroadcast.add(fldChat);
		pnlBroadcast.add(btnPersonalMsg);		
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
	
	public ClientUI(String title) {
		super(title);
		init();
		start();
		this.setSize(XSIZE,YSIZE);
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationByPlatform(true);		
		setVisible(true);
		
		roomList.createBufferStrategy(2);
		bs = roomList.getBufferStrategy();
		roomList.setBufferStrategy(bs);
		roomList.setVisible(true);
		ArrayList<String> al = new ArrayList<String>();
		al.add("player1");
		al.add("player2");
		for(int i=0;i<10;i++) {
			Color color = new Color(255,(int)(Math.random() * 250),(int)(Math.random() * 250));
			BufferedImage roomImg = roomList.drawRoom(al, color, i);
			roomList.rooms.add(new RoomData(roomImg, i, al, false, color));
		}
	}

	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ClientUI cUI = new ClientUI("Client");
	}
}
