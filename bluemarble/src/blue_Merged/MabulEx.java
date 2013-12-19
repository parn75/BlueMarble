package blue_Merged;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*class City { 
 public String name;
 // �⺻����, �ǹ� 1,2,3��
 int[] price = new int[4];
 private Image img;
 // ������ �������ִ� ������ 3����
 Boolean[] building = new Boolean[3];
 public String owner;

 public City(String name, int[] price, Image img, String owner) {
 this.name = name;
 this.price = price;
 this.owner = owner;
 }
 }*/
/*
 class Player {
 public int x, y, thisXY, money;

 public enum status {
 WAIT, RUN, BROKE, DOUBLE;
 };

 public String name;
 private Boolean host;
 public Player(String name, Boolean host) {
 this.money = 10000;
 this.thisXY = 0;
 this.x = 600;
 this.y = 380;
 this.name = name;
 this.host = host;
 }
 }*/


public class MabulEx extends JFrame implements WindowListener {
	TablePanel tp;
	WaitingRoomUI waitingRoom;
	public MabulEx() {
		// TODO Auto-generated constructor stub
		//Table ts = new Table();
		//Canvas can = ts;
		//tp = new TablePanel();
		tp = new TablePanel(waitingRoom);
		add("Center", tp);
		setSize(1240, 760);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public MabulEx(WaitingRoomUI waitingRoom) {
		super(waitingRoom.myRoomInfo.getRoomName().toString());
		this.waitingRoom = waitingRoom;
		
		tp = new TablePanel(waitingRoom);
		add("Center", tp);
		setSize(1240, 760);
		setVisible(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		int result = JOptionPane.showConfirmDialog(null, "������ ������ �����Ͻðڽ��ϱ�? ����Ͻø� ���� ȭ������ �̵��մϴ�.", "���� ����", JOptionPane.YES_NO_OPTION);
		if(result == JOptionPane.YES_OPTION) {	
			waitingRoom.client.send(new ChatData(ChatType.WaitingRoomExit,"���� ����"));
			
			ChatData cd = new ChatData();
			cd.setType(ChatType.Exit);
			waitingRoom.client.send(cd);
			try {
				Thread.sleep(200);  //client.send�� ����Ǵµ��� exit�� ���� �ʵ��� ��ٸ�.
			} catch (InterruptedException e1) {			
				e1.printStackTrace();
			}
			System.exit(0); 
		}
		System.out.println("����â �����");
		this.setVisible(false);	
		waitingRoom.setVisible(true);		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}

