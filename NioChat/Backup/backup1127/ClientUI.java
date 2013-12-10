package backup1127;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
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
	
	static Client client;	


	private void start() {
		btnConnect.addActionListener(new ClientAction());		
		btnPersonalMsg.addActionListener(new ClientAction());	
	}
	
	private void init() {
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		pnlConnect.setLayout(new BoxLayout(pnlConnect,BoxLayout.X_AXIS));
		pnlConnect.add(fldID);
		pnlConnect.add(btnConnect);
		con.add(pnlConnect, "North");
		con.add(txtChat,"Center");
		pnlBroadcast.setLayout(new BoxLayout(pnlBroadcast,BoxLayout.X_AXIS));
		pnlBroadcast.add(fldChat);
		pnlBroadcast.add(btnPersonalMsg);		
		con.add(pnlBroadcast,"South");
		
		listModel.addElement("Connectors");
		lstConnector = new JList<String>(listModel);
		con.add(lstConnector, "East");
		lstConnector.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	
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
