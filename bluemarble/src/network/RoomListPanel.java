package network;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

public class RoomListPanel extends JPanel {
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
	
	public BufferedImage onePanel() {
		BufferedImage bi = new BufferedImage(this.getWidth(), 100, BufferedImage.TYPE_INT_ARGB); 
		Graphics2D ig2 = bi.createGraphics();
		 ig2.setPaint(Color.blue);
		 ig2.fillRect(0, 0, this.getWidth(), this.getHeight());
		 return bi;
	}

	public RoomListPanel() {
		JScrollPane sp = new JScrollPane(this);		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		for(int i=0;i<10;i++) {
		RoomPanel rp = new RoomPanel();
		this.add(rp);
		}
	}

	public static void main(String[] args) {
		JFrame jf = new JFrame();
		RoomListPanel rlp = new RoomListPanel();
		jf.setSize(500,500);
		jf.add(rlp);
		jf.setVisible(true);
	}

}

class RoomPanel extends JPanel {	
	
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
	
	public RoomPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		JLabel roomName = new JLabel("방제");
		JLabel players = new JLabel("참가자");
		JButton joinB = new JButton("게임 참가");
		this.add(roomName);
		this.add(players);
		this.add(joinB);
	}	
	
}