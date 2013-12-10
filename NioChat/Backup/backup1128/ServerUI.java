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
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerUI extends JFrame{
	JButton btnSvrStart = new JButton("Server Start");
	JButton btnBroadcast = new JButton("To All");
	JButton btnPersonalMsg = new JButton("To Selected People");
	JTextField fldChat = new JTextField();
	JTextArea txtChat = new JTextArea();
	DefaultListModel<String> listModel = new DefaultListModel<String>();
	JList<String> lstConnector; // = new JList<String>();	
	JPanel pnlBroadcast = new JPanel();
	
	ServerAction svrAction;	
	
	private void start() {
		svrAction = new ServerAction(this);
		btnSvrStart.addActionListener(svrAction);
		btnBroadcast.addActionListener(svrAction);
		btnPersonalMsg.addActionListener(svrAction);	
	}
	
	private void init() {
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		con.add(btnSvrStart, "North");
		txtChat.setEditable(false);
		con.add(txtChat,"Center");
		pnlBroadcast.setLayout(new BoxLayout(pnlBroadcast,BoxLayout.X_AXIS));
		pnlBroadcast.add(fldChat);
		pnlBroadcast.add(btnPersonalMsg);
		pnlBroadcast.add(btnBroadcast);	
		con.add(pnlBroadcast,"South");		
		
		listModel.addElement("To All");
		lstConnector = new JList<String>(listModel);
		con.add(lstConnector, "East");
		lstConnector.setBorder(BorderFactory.createLineBorder(Color.BLACK));		
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
		ServerUI sUI = new ServerUI("Server");
	}

}
