package bluemarble_Merged;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class RoomListPanel extends JPanel {  //�� ������ ����ֱ����� ���� �г�
	private static RoomListPanel Instance = new RoomListPanel();
	RoomPanel[] roomList = new RoomPanel[12];
	Server server;
	Client client;
	boolean isServer = false;
	boolean isClient = false;
	
	public static RoomListPanel getInstance() {
		return Instance;
	}
	
	public WaitingRoomInfo[] getWholeRoomInfo() {
		WaitingRoomInfo[] wholeRoomInfo = new WaitingRoomInfo[12];
		for(int i=0;i<12;i++) {
			wholeRoomInfo[i] = roomList[i].getRoomInfo();
		}
		return wholeRoomInfo;
	}
	
	public WaitingRoomInfo[] cloneWholeRoomInfo() {
		WaitingRoomInfo[] wrs = new WaitingRoomInfo[12];
		for(int i=0;i<wrs.length;i++) {
			wrs[i] = new WaitingRoomInfo();
			wrs[i].setPlayerNames(roomList[i].roomInfo.getPlayerNames().clone());
			wrs[i].setPlayerNum(roomList[i].roomInfo.getPlayerNum());
			wrs[i].setRoomNum(roomList[i].roomInfo.getRoomNum());			
			wrs[i].setRoomName(roomList[i].roomInfo.getRoomName());
			wrs[i].setIsStarted(roomList[i].roomInfo.getIsStarted());
		}	
		return wrs;
	}
	
	public void setWholeRoomInfo(WaitingRoomInfo[] wholeRoomInfo) {
		for(int i=0;i<12;i++) {		
			roomList[i].updateRoomInfo(wholeRoomInfo[i]);		
		}
	}

	private RoomListPanel() {			
		this.setLayout(new GridLayout(3,3));
		for(int i=0;i<12;i++) {
			roomList[i] = new RoomPanel(this, "�� ��", i);
			this.add(roomList[i]);
		}
	}
	
	public void setServer(Server server) {
		this.server = server;
		isServer = true;
		for(int i=0;i<12;i++) {
			roomList[i].setServer(true);
			roomList[i].setUnJoinable();
		}
	}
	
	public void setClient(Client client) {
		this.client = client;
		isClient = true;
		for(int i=0;i<12;i++) roomList[i].setClient(true);
	}
	
	public BufferedImage drawRoom(ArrayList<String> players, Color color, int roomNumber) {
		BufferedImage bi = new BufferedImage(this.getWidth(), 100, BufferedImage.TYPE_INT_ARGB); 
		Graphics2D ig2 = bi.createGraphics();
	    ig2.setPaint(color);
	    ig2.fillRect(0, 0, this.getWidth(), 100);
	    ig2.setPaint(Color.black);
	    ig2.drawRect(0, 0, this.getWidth(), 100);
	    int tmpx = 20;
	    Font f = new Font("Georgia", Font.BOLD, 15);
	    ig2.setFont(f);	    
	    ig2.drawString("RoomName   " + roomNumber+"", 0, 20);
	    for(String s:players) {	  //��ȿ� �ִ� �÷��̾� ǥ��
	    	ig2.drawString(s + ", ", tmpx, 40);
	    	tmpx = tmpx+s.length() * 10;
	    }
	    ig2.setColor(Color.gray);
	    ig2.fill3DRect(200, 40, 100, 30, true);  //Join ��ư
	    ig2.setColor(Color.black);
	    ig2.drawString("Join", 230, 60);
	    return bi;
	}	
	
	public BufferedImage onePanel() {
		BufferedImage bi = new BufferedImage(this.getWidth(), 100, BufferedImage.TYPE_INT_ARGB); 
		Graphics2D ig2 = bi.createGraphics();
		 ig2.setPaint(Color.blue);
		 ig2.fillRect(0, 0, this.getWidth(), this.getHeight());
		 return bi;
	}
	
	public void setRoom(int roomNum, String title) { //�ش� �� ��ȣ�� ������ �����Ѵ�
		roomList[roomNum].setTitle(title);
	}
	
	public boolean joinRoom(int roomNum, String playerName) { //�ش� �濡 �÷��̾� �߰�
		return roomList[roomNum].joinRoom(playerName);
	}
	
	public void deletePlayer(int roomNum, String player) { //�ش� �濡�� �÷��̾� ����
		roomList[roomNum].deletePlayer(player);
	}

	public static void main(String[] args) {
		JFrame jf = new JFrame();
		RoomListPanel rlp = new RoomListPanel();
		jf.setSize(500,500);
		jf.add(rlp);
		jf.setVisible(true);
	}
	


}

class RoomPanel extends JPanel implements MouseListener{
	JLabel roomName; //����
	JLabel players; //�÷��̾� ���ڸ� ǥ���ϱ� ���� ��
	JButton joinB;
	WaitingRoomInfo roomInfo = new WaitingRoomInfo();
	boolean isCreated = false;
	boolean isClient = false; //Ŭ���̾�Ʈ�� �ִ� �г�����?
	boolean isServer = false; //������ �ִ� �г�����?
	boolean isStarted = false; 
	RoomListPanel roomListPanel;
	
	public StringBuffer[] getPlayerNames() {
		return roomInfo.getPlayerNames();
	}
	
	public void updateRoomInfo(WaitingRoomInfo roomInfo) {
		this.roomInfo = roomInfo;
		if(roomInfo.getRoomName() != null) roomName.setText(roomInfo.getRoomName().toString());
		setPlayerNum(roomInfo.getPlayerNum());		
	}
	
	synchronized public boolean joinRoom(String playerName) {	//�÷��̾ ������ ��� ServerThread������ ȣ��
		if(roomInfo.getPlayerNum() >= 4) return false;		
		int result = roomInfo.joinRoom(new StringBuffer(playerName));		
		if(result <=4) setPlayerNum(roomInfo.getPlayerNum());
		if(result == 4) joinB.setEnabled(false); 
		return true;
	}
	
	synchronized public void deletePlayer(String player) { //�÷��̾ ���� ��� ServerThread������ ȣ��		
		int result = roomInfo.deletePlayer(player);
		setPlayerNum(result);
		if(result==0) joinB.setText("���� �����");
		
	}
	
	public BufferedImage onePanel() { //�г��� ������ ĥ�Ѵ�. �ܺ� �̹��� �ε����� ����?
		BufferedImage bi = new BufferedImage(this.getWidth(), 100, BufferedImage.TYPE_INT_ARGB); 
		Graphics2D ig2 = bi.createGraphics();
		 ig2.setPaint(new Color(120,240,120));
		 ig2.fillRect(0, 0, this.getWidth(), this.getHeight());
		 ig2.drawRect(0, 0, this.getWidth(), this.getHeight());
		 return bi;
	}
	
	@Override
	  protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	        g.drawImage(onePanel(), 0, 0, null);
	    
	}
	
	public RoomPanel(RoomListPanel roomListPanel, String title, int roomNumber) {		
		roomInfo.setRoomNum(roomNumber); 
		roomInfo.setRoomName(new StringBuffer(title));
		this.roomListPanel = roomListPanel;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		roomName = new JLabel(title);		
		players = new JLabel("���� �ο�: " + roomInfo.getPlayerNum());
		joinB = new JButton("���� �����");
		this.add(roomName);
		this.add(players);
		this.add(joinB);
		joinB.addMouseListener(this);
	}
	
	public void initRoomList() {
		
	}
	
	public void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}
	
	public boolean isStarted() {
		return isStarted;
	}
	
	public void setTitle(String title) {
		roomName.setText(title);
	}
	
	public void setPlayerNum(int playerNum) {
		if(playerNum == 0) joinB.setText("���� �����");
		if(playerNum > 0 && playerNum<= 4) joinB.setText("�����ϱ�");
		if(playerNum == 4) joinB.setEnabled(false);
		if(playerNum < 4 && isClient == true) joinB.setEnabled(true);
		if(playerNum>=0 && playerNum<=4) {
			players.setText("���� �ο�: " + playerNum);
		}
	}
	
	public WaitingRoomInfo getRoomInfo() {
		return roomInfo;
	}
	
	public void setRoomInfo(WaitingRoomInfo roomInfo) {
		this.roomInfo = roomInfo;
	}
	
	public String getTitle() {
		return roomName.getText();
	}
	
	public void setJoinable() {
		joinB.setEnabled(true);
	}
	
	public void setUnJoinable() {
		joinB.setEnabled(false);
	}
	
	public void setServer(boolean isServer) {
		this.isServer = isServer;		
	}
	
	public void setClient(boolean isClient) {
		this.isClient = isClient;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == joinB && isClient == true && roomInfo.getPlayerNum()<4) {
			WaitingRoomInfo wri = new WaitingRoomInfo();
			if(joinB.getText() == "���� �����") {
				String title = JOptionPane.showInputDialog("�� ������ �Է��� �ּ���.");
				wri.setRoomName(new StringBuffer(title));
			}else wri.setRoomName(new StringBuffer("����"));			
			wri.setRoomNum(roomInfo.getRoomNum());
			ChatData cd = new ChatData(ChatType.Join, wri);
			roomListPanel.client.send(cd);
			//Ŭ���̾�Ʈ�� ��� ������ ���� ���θ� ������ Client���� Success�޼����� ������ ���� ����
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}