package backup1128;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientUI extends JFrame{
	static JTextField fldID = new JTextField();
	static JButton btnConnect = new JButton("Connect");	
	static JButton btnPersonalMsg = new JButton("Send");	
	static JTextArea txtChat = new JTextArea();
	static JTextField fldChat = new JTextField();
	static JList<String> lstConnector = new JList<String>();
	static DefaultListModel<String> listModel = new DefaultListModel<String>();
	static JPanel pnlBroadcast = new JPanel();
	static JPanel pnlConnect = new JPanel();
	
	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("파일");	
	static JMenuItem photoMenuItem = new JMenuItem("사진 보내기");
	JMenuItem fileMenuItem = new JMenuItem("파일 전송");
	
	static Client client;	


	private void start() {
		btnConnect.addActionListener(new ClientAction(this));		
		btnPersonalMsg.addActionListener(new ClientAction(this));
		photoMenuItem.addActionListener(new ClientAction(this));
	}
	
	private void init() {
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		pnlConnect.setLayout(new BoxLayout(pnlConnect,BoxLayout.X_AXIS));
		pnlConnect.add(fldID);
		pnlConnect.add(btnConnect);
		con.add(pnlConnect, "North");
		txtChat.setEditable(false);
		con.add(txtChat,"Center");
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
		this.setSize(500,500);
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationByPlatform(true);		
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
		ClientUI cUI = new ClientUI("Client");
	}
}
