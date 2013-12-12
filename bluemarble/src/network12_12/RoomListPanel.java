package network12_12;

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
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class RoomListPanel extends JPanel {
	ArrayList<RoomData> rooms = new ArrayList<RoomData>();
	final int room_Height = 80;
	int room_HeightStart = 0;	
	RoomPanel[] roomList = new RoomPanel[12];
	int listForClient = 0;
	Server server;
	Client client;
	boolean isServer = false;
	boolean isClient = false;
	

	public RoomListPanel() {			
		this.setLayout(new GridLayout(3,3));
		for(int i=0;i<12;i++) {
			roomList[i] = new RoomPanel(this, "빈 방", 0, i);
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
	    for(String s:players) {	  //방안에 있는 플레이어 표시
	    	ig2.drawString(s + ", ", tmpx, 40);
	    	tmpx = tmpx+s.length() * 10;
	    }
	    ig2.setColor(Color.gray);
	    ig2.fill3DRect(200, 40, 100, 30, true);  //Join 버튼
	    ig2.setColor(color.black);
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
	
	public void setRoom(int roomNum, String title) {
		roomList[roomNum].setTitle(title);
	}
	
	public boolean joinRoom(int roomNum, String playerName) {
		return roomList[roomNum].joinRoom(playerName);
	}
	
	public void deletePlayer(int roomNum, String player) {
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

class RoomPanel extends JPanel implements MouseListener{ //ClientAction에 MouseListener move?
	int roomNumber;
	JLabel roomName;
	JLabel players;
	JButton joinB;
	int playerNum=0;
	String[] playerNames = new String[4];
	boolean isCreated = false;
	boolean isClient = false;
	boolean isServer = false;
	boolean isStarted = false;
	RoomListPanel roomListPanel;
	
	public String[] getPlayerNames() {
		return playerNames;
	}
	
	synchronized public boolean joinRoom(String playerName) {
		playerNum++;
	
		if(playerNum<=4) {
			playerNames[playerNum-1] = playerName;
			System.out.println(playerNames[playerNum-1]);	
			if(playerNum == 1) joinB.setText("참가하기");
			setPlayerNum(playerNum);
			return true;
		}
		if(playerNum == 4) joinB.setEnabled(false);
		if(playerNum>4) playerNum = 4;
		return false;	
	}
	
	synchronized public void deletePlayer(String player) {
		for(int i=0;i<playerNames.length;i++) {
			if(playerNames[i].equals(player)) {
				for(int j=i;j<playerNames.length-1;j++) 
					if(playerNames[j+1] != null || playerNames[j+1] !="None") playerNames[j] = playerNames[j+1];
					else playerNames[j] = "None";
				playerNum--;
				setPlayerNum(playerNum);
				if(playerNum==0) joinB.setText("게임 만들기");
			}
		}
	}
	
	public BufferedImage onePanel() {
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
	
	public RoomPanel(RoomListPanel roomListPanel, String title, int playerNum, int roomNumber) {
		for(int i=0;i<playerNames.length;i++) playerNames[i] = "None";
		this.roomNumber = roomNumber;
		this.roomListPanel = roomListPanel;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		roomName = new JLabel(title);
		this.playerNum = playerNum;
		players = new JLabel("참가 인원: " + playerNum);
		joinB = new JButton("게임 만들기");
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
		if(playerNum == 1) joinB.setText("참가하기");
		if(playerNum == 4) joinB.setEnabled(false);
		if(playerNum>0 && playerNum<=4) {
			players.setText("참가 인원: " + playerNum);
		}
	}
	
	public String getTitle() {
		return roomName.getText();
	}
	
	public int getPlayerNum() {
		return playerNum;
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
		if(e.getSource() == joinB && isClient == true && playerNum<4) {
			ChatData cd = new ChatData(ChatType.Join, roomNumber);
			roomListPanel.client.send(cd);
			//클라이언트의 경우 서버에 참가 여부를 보내고 Client에서 Success메세지를 받으면 대기실 시작
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