package network;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RoomList extends Canvas implements MouseListener, MouseWheelListener{
	BufferStrategy bs;
	private BufferedImage playerImage, playerFireImage;	
	ArrayList<RoomData> rooms = new ArrayList<RoomData>();
	final int room_Height = 80;
	int room_HeightStart = 0;

	Image background;  //배경 이미지
	
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
	
	public void render(Graphics g) {  //실제로 화면을 그려주는 메소드
		Dimension d = this.getSize();		
		BufferedImage screen = new BufferedImage(this.getWidth(), room_Height*rooms.size(), BufferedImage.TYPE_INT_ARGB); //화면 크기만큼의 버퍼 이미지에
		Graphics2D screenG = (Graphics2D) screen.getGraphics();
		screenG.setColor(getBackground());
		screenG.fillRect(0, 0, this.getWidth(), this.getHeight());
		int tmpy = 0;
		for(RoomData rd:rooms) {
			screenG.drawImage(rd.getRoomImg(), 0, tmpy, this);			
			tmpy += room_Height;
		}

		//g.drawImage(screen, 0,0,this);	//모든 이미지가 완성되었으면 이제 화면에 표시해 준다.
		g.drawImage(screen, 0, 0, this.getWidth(), this.getHeight(),
				0, room_HeightStart, this.getWidth(), room_HeightStart+this.getHeight(), this);
		screenG.dispose();	
	}
	
	public void updateRoomList() {
		Graphics g = bs.getDrawGraphics();
		render(g);
		g.dispose();
		bs.show();
		if(bs.contentsLost()) System.out.println("content lost!");
	}

	public RoomList() {
		this.setBackground(Color.white);		
		//this.addKeyListener(this);
		this.setFocusable(true);
		
		UpdateScreen gs = new UpdateScreen(this); //스크린 업데이트 테스크
		Timer timer = new Timer();
		timer.schedule(gs, 500, 10); 
		this.addMouseWheelListener(this);
	}

	public void setBufferStrategy(BufferStrategy bs) {
		this.bs = bs;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//클릭한 좌표값을 받아와서 몇번 방을 선택했는지 판단		
		//방이 없다면 방 생성 (방 제목 입력?), 있다면 참가
		//방 정보 업데이트. (참여자
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

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int s = e.getWheelRotation();
		int median = room_Height * rooms.size() - this.getHeight();
		if(room_HeightStart > median) room_HeightStart = median;
		else if(room_HeightStart>=0 && room_HeightStart<=median) room_HeightStart = room_HeightStart + s*10;
		else if(room_HeightStart<0) room_HeightStart = 0;		
	}	

}

class UpdateScreen extends TimerTask{ //화면 업데이트
	RoomList roomList;
	
	public UpdateScreen(RoomList roomList) {
		this.roomList = roomList;
	}

	@Override
	public void run() {
		roomList.updateRoomList();
	}

}
