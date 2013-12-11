package networkbackup_12_11_morning;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
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
	
	//ä�� �� �̹��� ǥ�� �κ�, ��ũ�� ���� �� ��Ÿ�� ����
	StyleContext sc = new StyleContext();
    final DefaultStyledDocument doc = new DefaultStyledDocument(sc);
	JTextPane txtChat= new JTextPane(doc);
	JScrollPane sp;
	ChatDocStyles chatStyle;
	
	//�޴��� ����
	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("����");	
	JMenuItem photoMenuItem = new JMenuItem("���� ������");
	JMenuItem fileMenuItem = new JMenuItem("���� ����");
	
	ServerAction svrAction;
	
	public void endScroll() {  //Scroll�� ���� �Ʒ��� ������ ���� �޼ҵ�		
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
		
		txtChat.setEditable(false);
		con.add(txtChat,"Center");
		sp = new JScrollPane(txtChat);  //Adding Scroll Bar
	    con.add(sp);
	    
	    chatStyle = new ChatDocStyles(sc);
		
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
		
		/*
		txtChat.setEditorKit(JTextPane.createEditorKitForContentType("text/html"));
	    HyperlinkListener l = new HyperlinkListener() {
	        @Override
	        public void hyperlinkUpdate(HyperlinkEvent e) {
	            if (HyperlinkEvent.EventType.ACTIVATED == e.getEventType()) {
	                try {
	                    txtChat.setPage(e.getURL());
	                } catch (IOException e1) {
	                    e1.printStackTrace();
	                }
	            }

	        }

	    };
	    
	    txtChat.addHyperlinkListener(l);
	    try {
			doc.insertString(doc.getLength(), "http://www.naver.com/\n", sc.getStyle("MainSytle"));
			
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
	}
	
	public ServerUI(String title) {
		super(title);
		init();
		start();
		this.setSize(500,500);
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
