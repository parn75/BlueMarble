package network12_14night;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


public class TablePanel extends JPanel implements MouseListener,Runnable{
	private Image offscreenImage;
	private Graphics offscreenGraphics;
	protected final Image backGroundImg = Toolkit.getDefaultToolkit().getImage(
			"./img/board.jpg");
	protected final Image user = Toolkit.getDefaultToolkit().getImage(
			"./img/user.png");
	protected final Image diceBtn = Toolkit.getDefaultToolkit().getImage(
			"./img/diceon.png");
	protected final Image building = Toolkit.getDefaultToolkit().getImage(
			"./img/city.png");
	protected final Rectangle BTNRECT = new Rectangle(555,290, 200, 200);
	protected final int[][] mapXY = { { 530, 570 }, { 480, 528 }, { 425, 485 },
			{365, 445 }, { 315, 405 }, { 265, 365 }, { 215, 325 },
			{ 165, 285 }, { 105, 245 }, {180, 215 }, { 235,190 },
			{ 295, 160 },{355,135},{410,105},{465,85},
			{515,55},{585,20},{655,60},{705,90},
			{760,125},{815,155},{870,185},{933,220}
			,{1000,260},{1085,310},{1005,352},{940,390},
			{870,420},{805,455},{748,490},{690,515},{630,550}};
	
	private JPanel bottomPanel = new JPanel();
	public JTextArea chatView;
	public JTextField chatInput;
	private JScrollPane chetScroll; 
	private JSplitPane chetResize;  
	private JLabel chetDummyLB = new JLabel();
	private Purchase inCity;

	//스테틱을 안쓰고 던져줄 방법을 생각
	public static int userNum = 0;
	public static Boolean trunOver = false;
	public static ArrayList<Building> buildingList = new ArrayList<Building>();
	public static ArrayList<Player> ply = new ArrayList<Player>();
	public static HashMap<Object, City> cityMap = new HashMap<Object, City>();	
	public static final String[] cityNameList = {"출발지","방콕","마카오","베이징","독도","타이페이",
		"두바이","카이로","무인도","발리","도쿄","하와이","시드니","상파울로","찬스카드","퀘벡","복지기구","프라하","푸켓",
		"베를린","모스코바","찬스카드","로마","제네바","세계여행","타이티","파리","찬스카드","런던","서울","뉴욕"};
	
	private int dice1, dice2;
	private Boolean f = false;
	Player dPlayer;
	WaitingRoomUI waitingRoom;

	public TablePanel(WaitingRoomUI waitingRoom) {
		this.waitingRoom = waitingRoom;
		StringBuffer[] sb = waitingRoom.myRoomInfo.getPlayerNames();
		sb[0].toString();
		this.isDoubleBuffered();
		TableInit();
		ply.add(new Player("유저 1", false));
		//ply.add(new Player("유저 2", false));
		setLayout(new GridLayout(2, 1));
		bottomPanel.setLayout(new BorderLayout());
		setSize(500, 500);
		setLayout(null);
		setDoubleBuffered(true);
		Thread aniThread = new Thread(this);
		aniThread.start();
		TextSetting();
		chetDummyLB.setOpaque(false);
		chetDummyLB.setBorder(null);
		chetResize = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chetDummyLB, chetScroll);
		chetResize.setDividerLocation(300);
		chetResize.setBounds(310, 350, 620, 360);
		chetResize.setOpaque(false);
		chetResize.setBorder(null);
		add(chetResize);
		bottomPanel.addMouseListener(this); 
		chetResize.addMouseListener(this);
		addMouseListener(this);
		// add(chetScroll);
		add(chatInput);


	}
	public void TableInit() {
		int[] priceInt = { 1000, 2000, 3000, 4000 };
		for (int i = 0; i < cityNameList.length; i++) {
			cityMap.put(cityNameList[i], new City(i + 1, priceInt, cityNameList[i]));
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		BufferedImage bi = new BufferedImage(this.getWidth(),
								this.getHeight(),  BufferedImage.TYPE_INT_ARGB );
		Graphics g2 = bi.getGraphics();
		g2.drawImage(backGroundImg, 0, 0, 1240, 760, this);
		g2.drawImage(diceBtn,555,290,this);
		g2.drawImage(user,ply.get(0).x,ply.get(0).y,this);
		g2.setColor(new Color(0));
		for (Building t : buildingList) {
			for(int j=0; j<t.buildCnt;j++){
			g2.drawImage(building, mapXY[t.thisXY][0]+j*5,
					mapXY[t.thisXY][1], this);
			}
		}
		g2.setColor(Color.black);
		g2.drawRect(mapXY[1][0]+30,mapXY[1][1]-10,20,20);
		g2.drawRect(mapXY[1][0]+50,mapXY[1][1],20,20);
		g2.drawRect(mapXY[1][0]+70,mapXY[1][1]+10,20,20);
		
		g2.drawRect(mapXY[3][0]+35,mapXY[3][1]-10,20,20);
		g2.drawRect(mapXY[3][0]+55,mapXY[3][1],20,20);
		g2.drawRect(mapXY[3][0]+75,mapXY[3][1]+10,20,20);
		
		g2.drawRect(mapXY[4][0]+30,mapXY[4][1]-15,20,20);
		g2.drawRect(mapXY[4][0]+50,mapXY[4][1],20,20);
		g2.drawRect(mapXY[4][0]+70,mapXY[4][1]+15,20,20);
		g2.setColor(Color.white);
		g2.drawString("1p 가진돈 : " + ply.get(0).money, 20, 20);
		//좌표위치 테스트
		//for(int i=0; i<mapXY.length; i++){
		//	g2.drawRect(mapXY[i][0], mapXY[i][1],50, 50);
		//}
		
		
		g.drawImage(bi,0, 0,this);
		
		
	}

	public void TextSetting() {
		chatInput = new JTextField(100);
		chatInput.setBounds(310, 710, 620, 25);

		chatView = new JTextArea();
		chatView.setOpaque(false); 
		chatView.setLineWrap(true);
		chatView.setWrapStyleWord(true);
		chatView.setSize(400, 100);
		chatView.setLocation(0, 0);
		chatView.setOpaque(false);
		chatView.setForeground(Color.white);
		chatView.setEditable(false);
		chatView.setDragEnabled(true);

		chetScroll = new JScrollPane(chatView);
		chetScroll.setSize(400, 100);
		chetScroll.setLocation(0, 0);
		chetScroll.setOpaque(false);
		chetScroll.getViewport().setOpaque(false);
		chetScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chetScroll.setVisible(true);
		chetScroll.setBounds(310, 550, 620, 160); 

		chatInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyPressed(e);
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					//chatView.append(chatInput.getText() + "\n");
					ChatData cd = new ChatData(ChatType.WaitingRoomChat,chatInput.getText() );
					waitingRoom.client.send(cd);
					chatInput.setText("");
				}
			}
		});// KeyAdapter END
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		Rectangle mouseEventRect = new Rectangle(e.getXOnScreen(), e.getYOnScreen(), 10, 10);
		if (mouseEventRect.intersects(BTNRECT)) {
			System.out.println("눌림");
			f = true;
			trunOver = true;
		}
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
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	} 
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
		while (!Thread.currentThread().isInterrupted()) {
			//에니메이션용 쓰레드
			repaint();
			animation();
			}
			Thread.sleep(100);
			
		} catch (InterruptedException e) {

		} finally {
			System.out.println("쓰레드 사망");
		}
	}
	

	public void animation() {
		if(ply.size()!=0)
		dPlayer = ply.get(userNum);
		try {
			if (f == true) {
				for (int i = 0; i < 10; i++) {

					//repaint();
					dice1 = (int) (Math.random() * 5 + 1);
					dice2 = (int) (Math.random() * 5 + 1);
					Thread.sleep(100);

				}
				dice1 = (int) (Math.random() * 5 + 1);
				dice2 = (int) (Math.random() * 5 + 1);
				System.out.println(dice1+"/"+dice2);
				userMove();
			
			}
		} catch (Exception e) {
		}
	}// func END

	public void userMove() {
		try {
			System.out.println("무브");
			Thread.sleep(300);
			f = false;
			int diceRst = dice1 + dice2;
			// 움직임
			for (int i = 1; i < diceRst + 3; i++) {
				dPlayer.thisXY++;
				if (dPlayer.thisXY >= 32)
					dPlayer.thisXY = 0;
				dPlayer.x = mapXY[dPlayer.thisXY][0];
				dPlayer.y = mapXY[dPlayer.thisXY][1];
				repaint();
				System.out.println(dPlayer.thisXY+"/"+(diceRst+3));
				Thread.sleep(300);
			}
			System.out.println(dPlayer.name+"/"+dPlayer.thisXY);
			// 이동 이벤트처리
			City c = null;
			System.out.println("1");
			
			c = (City)cityMap.get(cityNameList[ply.get(userNum).thisXY]);
			System.out.println(cityMap.get(cityNameList[ply.get(userNum).thisXY]).getCityName());
			System.out.println("2//"+ply.get(userNum).thisXY);
			// 도착한 위치에 주인이없으면 건물을 사는것을 호출하고 있으면 돈을 지불하고 턴을 넘긴다
			System.out.println(c.getCityName());

			System.out.println("3");
			if (c != null && c.getCollecterName() == null) {
				if (ply != null)
					c = (City) cityMap
							.get(cityNameList[ply.get(userNum).thisXY]);
				if (c != null) {
					int[] p = c.getPrice(); 
					System.out.println(p[0] + "/" + p[1]);
					inCity=new Purchase(c.getCityName(), p[0]);
				
				}
				System.out.println("구매");
				inCity.setVisible(true);
			} else if (ply.get(userNum).money < 0) {
				System.exit(0);
			}else if (c != null && c.getCollecterName() != null
					&& !c.getCollecterName().equals(ply.get(userNum).name)) {
				System.out.println("걸림");
				int tMoney = 0;
				for (int i = 0; i < c.getBuildList().length; i++) {
					System.out.println(ply.get(userNum).name+"걸림2");
					System.out.println("리스트"+c.getBuildList()[i]);
					if (c.getBuildList()[i] == true){
						tMoney += c.getPrice()[i];
						System.out.println(i+"//"+tMoney+"//"+c.getPrice()[i]);
					}else{
						System.out.println("false"+i+"//"+tMoney+"//"+c.getPrice()[i]);
					}
					System.out.println(tMoney);
				}
				System.out.println("걸림3");
				ply.get(userNum).money -= tMoney * 2;
				for(Player t: ply){
					if(t.name.equals(c.getCollecterName())==true)
						t.money+=tMoney * 2;
				}
				System.out.println("걸림4");
				//if (userNum < ply.size())
				//	userNum++;
				//else
					userNum = 0;
				trunOver = false;
				
			}  else {
				//if (userNum < 1)
				//	userNum++;
				//else
					userNum = 0;
				trunOver = false;
			}
		} catch (Exception e) {
		}
	}




}