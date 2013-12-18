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

public class RoomListPanel extends JPanel {  //각 대기실을 모아주기위한 바탕 패널
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
			roomList[i] = new RoomPanel(this, "빈 방", i);
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
	
	public void setRoom(int roomNum, String title) { //해당 방 번호의 제목을 변경한다
		roomList[roomNum].setTitle(title);
	}
	
	public boolean joinRoom(int roomNum, String playerName) { //해당 방에 플레이어 추가
		return roomList[roomNum].joinRoom(playerName);
	}
	
	public void deletePlayer(int roomNum, String player) { //해당 방에서 플레이어 제거
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
	JLabel roomName; //방제
	JLabel players; //플레이어 숫자를 표시하기 위한 라벨
	JButton joinB;
	WaitingRoomInfo roomInfo = new WaitingRoomInfo();
	boolean isCreated = false;
	boolean isClient = false; //클라이언트에 있는 패널인지?
	boolean isServer = false; //서버에 있는 패널인지?
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
	
	synchronized public boolean joinRoom(String playerName) {	//플레이어가 참가한 경우 ServerThread에서만 호출
		if(roomInfo.getPlayerNum() >= 4) return false;		
		int result = roomInfo.joinRoom(new StringBuffer(playerName));		
		if(result <=4) setPlayerNum(roomInfo.getPlayerNum());
		if(result == 4) joinB.setEnabled(false); 
		return true;
	}
	
	synchronized public void deletePlayer(String player) { //플레이어가 나간 경우 ServerThread에서만 호출		
		int result = roomInfo.deletePlayer(player);
		setPlayerNum(result);
		if(result==0) joinB.setText("게임 만들기");
		
	}
	
	public BufferedImage onePanel() { //패널의 바탕을 칠한다. 외부 이미지 로딩으로 변경?
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
		players = new JLabel("참가 인원: " + roomInfo.getPlayerNum());
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
		if(playerNum == 0) joinB.setText("게임 만들기");
		if(playerNum > 0 && playerNum<= 4) joinB.setText("참가하기");
		if(playerNum == 4) joinB.setEnabled(false);
		if(playerNum < 4 && isClient == true) joinB.setEnabled(true);
		if(playerNum>=0 && playerNum<=4) {
			players.setText("참가 인원: " + playerNum);
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
			if(joinB.getText() == "게임 만들기") {
				String title = JOptionPane.showInputDialog("방 제목을 입력해 주세요.");
				wri.setRoomName(new StringBuffer(title));
			}else wri.setRoomName(new StringBuffer("참가"));			
			wri.setRoomNum(roomInfo.getRoomNum());
			ChatData cd = new ChatData(ChatType.Join, wri);
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