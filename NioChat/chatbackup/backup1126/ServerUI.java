package backup1126;

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
	static JButton btnSvrStart = new JButton("Server Start");
	//static JButton btnSvrStop = new JButton("Server Stop");
	static JButton btnBroadcast = new JButton("To All");
	static JButton btnPersonalMsg = new JButton("To Selected People");
	static JTextField fldBroadcast = new JTextField();
	public static JTextArea txtarLog = new JTextArea();
	static DefaultListModel<String> listModel = new DefaultListModel<String>();
	static JList<String> lstConnector; // = new JList<String>();	
	JPanel pnlBroadcast = new JPanel();
	
	ServerAction svrAction;
	static Server nSvr;
	
	private void start() {
		svrAction = new ServerAction();
		btnSvrStart.addActionListener(svrAction);
		btnBroadcast.addActionListener(svrAction);
		btnPersonalMsg.addActionListener(svrAction);	
	}
	
	private void init() {
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		con.add(btnSvrStart, "North");
		con.add(txtarLog,"Center");
		pnlBroadcast.setLayout(new BoxLayout(pnlBroadcast,BoxLayout.X_AXIS));
		pnlBroadcast.add(fldBroadcast);
		pnlBroadcast.add(btnPersonalMsg);
		pnlBroadcast.add(btnBroadcast);	
		con.add(pnlBroadcast,"South");	
		
		listModel.addElement("Connectors");
		lstConnector = new JList(listModel);
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
