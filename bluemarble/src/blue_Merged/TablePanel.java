package blue_Merged;



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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


public class TablePanel extends JPanel implements MouseListener,Runnable{
	public static Control control = new Control();
	
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

	protected final int[][] buildingXY = {{mapXY[1][0]+35,mapXY[1][1]-15},{mapXY[1][0]+55,mapXY[1][1]},{mapXY[1][0]+75,mapXY[1][1]+15},
	{mapXY[3][0]+40,mapXY[3][1]-12},{mapXY[3][0]+57,mapXY[3][1]},{mapXY[3][0]+75,mapXY[3][1]+10},
	{mapXY[4][0]+33,mapXY[4][1]-16},{mapXY[4][0]+52,mapXY[4][1]},{mapXY[4][0]+73,mapXY[4][1]+13},
	{mapXY[5][0]+33,mapXY[5][1]-15},{mapXY[5][0]+48,mapXY[5][1]},{mapXY[5][0]+65,mapXY[5][1]+12},
	{mapXY[6][0]+35,mapXY[6][1]-10},{mapXY[6][0]+50,mapXY[6][1]+5},{mapXY[6][0]+65,mapXY[6][1]+15},
	{mapXY[7][0]+40,mapXY[7][1]},{mapXY[7][0]+55,mapXY[7][1]+10},{mapXY[7][0]+70,mapXY[7][1]+20},
	{mapXY[9][0]-30,mapXY[9][1]+5},{mapXY[9][0]-15,mapXY[9][1]-2},{mapXY[9][0],mapXY[9][1]-10},
	{mapXY[10][0]-30,mapXY[10][1]+5},{mapXY[10][0]-13,mapXY[10][1]-2},{mapXY[10][0]+6,mapXY[10][1]-10},
	{mapXY[11][0]-29,mapXY[11][1]+6},{mapXY[11][0]-12,mapXY[11][1]-1},{mapXY[11][0]+5,mapXY[11][1]-9},
	{mapXY[12][0]-33,mapXY[12][1]+6},{mapXY[12][0]-15,mapXY[12][1]-3},{mapXY[12][0]+7,mapXY[12][1]-12},
	{mapXY[13][0]-28,mapXY[13][1]+6},{mapXY[13][0]-11,mapXY[13][1]-3},{mapXY[13][0]+5,mapXY[13][1]-10},
	{mapXY[15][0]-30,mapXY[15][1]+6},{mapXY[15][0]-14,mapXY[15][1]-1},{mapXY[15][0]+1,mapXY[15][1]-7},
	{mapXY[17][0]+42,mapXY[17][1]-6},{mapXY[17][0]+58,mapXY[17][1]+3},{mapXY[17][0]+75,mapXY[17][1]+12},
	{mapXY[18][0]+42,mapXY[18][1]-6},{mapXY[18][0]+58,mapXY[18][1]+3},{mapXY[18][0]+75,mapXY[18][1]+12},
	{mapXY[19][0]+38,mapXY[19][1]-10},{mapXY[19][0]+56,mapXY[19][1]-1},{mapXY[19][0]+75,mapXY[19][1]+9},
	{mapXY[20][0]+38,mapXY[20][1]-10},{mapXY[20][0]+56,mapXY[20][1]-1},{mapXY[20][0]+75,mapXY[20][1]+9},
	{mapXY[22][0]+33,mapXY[22][1]-10},{mapXY[22][0]+56,mapXY[22][1]+3},{mapXY[22][0]+77,mapXY[22][1]+15},
	{mapXY[23][0]+33,mapXY[23][1]-10},{mapXY[23][0]+56,mapXY[23][1]+3},{mapXY[23][0]+77,mapXY[23][1]+15},
	{mapXY[25][0]-45,mapXY[25][1]+6},{mapXY[25][0]-23,mapXY[25][1]-6},{mapXY[25][0]-3,mapXY[25][1]-18},
	{mapXY[26][0]-40,mapXY[26][1]+2},{mapXY[26][0]-21,mapXY[26][1]-8},{mapXY[26][0]-3,mapXY[26][1]-18},
	{mapXY[28][0]-40,mapXY[28][1]+3},{mapXY[28][0]-21,mapXY[28][1]-7},{mapXY[28][0]-3,mapXY[28][1]-17},
	{mapXY[30][0]-39,mapXY[30][1]+2},{mapXY[30][0]-20,mapXY[30][1]-5},{mapXY[30][0]-1,mapXY[30][1]-15},
	{mapXY[31][0]-36,mapXY[31][1]-4},{mapXY[31][0]-19,mapXY[31][1]-10},{mapXY[31][0],mapXY[31][1]-19}
	};
	
	private JPanel bottomPanel = new JPanel();
	public JTextArea chatView;
	public JTextField chatInput;
	private JScrollPane chetScroll; 
	private JSplitPane chetResize;  
	private JLabel chetDummyLB = new JLabel();
	private Purchase inCity;
	private String currentUser = null;
/*
	//�ㅽ뀒�깆쓣 �덉벐怨��섏졇以�諛⑸쾿���앷컖
	public static int userNum = 0;
	public static ArrayList<Player> ply = new ArrayList<Player>();
	public static HashMap<Object, City> cityMap = new HashMap<Object, City>();	
	public static final String[] cityNameList = {"異쒕컻吏�,"諛⑹퐬","留덉뭅��,"踰좎씠吏�,"�낅룄","��씠�섏씠",
		"�먮컮��,"移댁씠濡�,"臾댁씤��,"諛쒕━","�꾩퓙","�섏���,"�쒕뱶��,"�곹뙆�몃줈","李ъ뒪移대뱶","�섎깹","蹂듭�湲곌뎄","�꾨씪��,"�몄폆",
		"踰좊�由�,"紐⑥뒪肄붾컮","李ъ뒪移대뱶","濡쒕쭏","�쒕꽕諛�,"�멸퀎�ы뻾","��씠��,"�뚮━","李ъ뒪移대뱶","�곕뜕","�쒖슱","�댁슃"};
	*/
	public static ArrayList<Building> buildingList = new ArrayList<Building>();
	public static Boolean trunOver = false;
	private int dice1, dice2;
	private Boolean f = false;
	Player dPlayer;
	WaitingRoomUI waitingRoom;

	public TablePanel(WaitingRoomUI waitingRoom) {
		this.waitingRoom = waitingRoom;
		this.isDoubleBuffered();
		StringBuffer[] sb = waitingRoom.myRoomInfo.getPlayerNames();
		//RoomListPanel.getInstance().g
		for(StringBuffer s:sb) System.out.println(s);
		//�ㅽ듃留곷같��濡쒕컺��
		String[] names = new String[1];
		
		//for(int i=0; i<sb.length ; i++){
			//if(sb[i]!=null) names[i] = sb[i].toString();
		//}
		names[0] = "player1";
		System.out.println(names[0]);
		control.setConstUserName(names);
		
		//control.setConstUserName(names); 
		//waitingRoom.client.getName();//�대씪�댁뼵���먯떊���대쫫
		control.setIAM(names[0]);
		//TableInit();
		//ply.add(new Player("�좎� 1", false));
		//ply.add(new Player("�좎� 2", false));
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
/*	public void TableInit() {
		int[] priceInt = { 1000, 2000, 3000, 4000 };
		for (int i = 0; i < cityNameList.length; i++) {
			cityMap.put(cityNameList[i], new City(i + 1, priceInt, cityNameList[i]));
		}
	} */

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		BufferedImage bi = new BufferedImage(this.getWidth(),
								this.getHeight(),  BufferedImage.TYPE_INT_ARGB );
		Graphics g2 = bi.getGraphics();
		g2.drawImage(backGroundImg, 0, 0, 1240, 760, this);
		g2.drawImage(diceBtn,555,290,this);
		g2.drawImage(user,control.playerData.get("player1").getX(),control.playerData.get("player1").getY(),this);
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

		g2.drawRect(mapXY[5][0]+30,mapXY[5][1]-15,20,20);
		g2.drawRect(mapXY[5][0]+50,mapXY[5][1],20,20);
		g2.drawRect(mapXY[5][0]+70,mapXY[5][1]+15,20,20);
		g2.setColor(Color.white);
		g2.drawString("1p 媛�쭊��: " + control.playerData.get("player1").getMoney(),20,20);
		//醫뚰몴�꾩튂 �뚯뒪��
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
			System.out.println("�뚮┝");
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
			//�먮땲硫붿씠�섏슜 �곕젅��
			repaint();
			currentUser = control.currentUserName();
			if(chatInput!=null&&trunOver!=true)
			animation();
			}
			Thread.sleep(100);
			
		} catch (InterruptedException e) {

		} finally {
			System.out.println("�곕젅���щ쭩");
		}
	}
	

	public void animation() {
		//if(ply.size()!=0)
		dPlayer = (Player)control.playerData.get(currentUser);   // 吏�툑 �뚮젅�댁쨷���좎�.
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
			System.out.println("臾대툕");
			Thread.sleep(300);
			f = false;
			int diceRst = dice1 + dice2;
			// ��쭅��
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
			// �대룞 �대깽�몄쿂由�
			City c = null;
			System.out.println("1");
			
			c = (City)control.cityMap.get(control.getCityNameList()
					[control.playerData.get(control.getIAM()).thisXY]);
			//System.out.println(cityMap.get(cityNameList[ply.get(userNum).thisXY]).getCityName());
			//System.out.println("2//"+ply.get(userNum).thisXY);
			// �꾩갑���꾩튂��二쇱씤�댁뾾�쇰㈃ 嫄대Ъ���щ뒗寃껋쓣 �몄텧�섍퀬 �덉쑝硫��덉쓣 吏�텋�섍퀬 �댁쓣 �섍릿��
			//System.out.println(c.getCityName());

			System.out.println("3");
			if (currentUser.equals(control.getIAM()) && c != null && c.getCollecterName() == null) {
				if (control.playerData != null)
					c = (City) control.getCityMap()
							.get(control.getCityNameList()
									[control.playerData.get(control.getIAM()).thisXY]);
				if (c != null) {
					int[] p = c.getPrice(); 
					System.out.println(p[0] + "/" + p[1]);
					inCity=new Purchase(c.getCityName(), p[0]);
				
				}
				System.out.println("援щℓ");
				inCity.setVisible(true);
			} else if (control.playerData.get(control.getIAM()).money < 0) {
				System.exit(0);
			}else if (c != null && c.getCollecterName() != null
					&& !c.getCollecterName().equals(control.playerData.get(control.getIAM()).name)) {
				System.out.println("嫄몃┝");
				int tMoney = 0;
				for (int i = 0; i < c.getBuildList().length; i++) {
					//System.out.println(ply.get(userNum).name+"嫄몃┝2");
					//System.out.println("由ъ뒪��+c.getBuildList()[i]);
					if (c.getBuildList()[i] == true){
						tMoney += c.getPrice()[i];
						System.out.println(i+"//"+tMoney+"//"+c.getPrice()[i]);
					}else{
						System.out.println("false"+i+"//"+tMoney+"//"+c.getPrice()[i]);
					}
					System.out.println(tMoney);
				}
				System.out.println("嫄몃┝3");
				control.playerData.get(control.getIAM()).money -= tMoney * 2;   // �꾩젣�좎����뚯�湲�怨꾩궛)
				control.playerData.get(c.getCollecterName()).money+=tMoney * 2;
				/*
				for(Player t: control.playerData()){
					if(t.name.equals(c.getCollecterName())==true)
						t.money+=tMoney * 2;
				}
				*/
				System.out.println("嫄몃┝4");
				//if (userNum < ply.size())
				//	userNum++;
				//else
					//userNum = 0;
				control.GAME_CONTROL(100);
				trunOver = false;
				
			}  else {
				//if (userNum < 1)
				//	userNum++;
				//else
					//userNum = 0;
				control.GAME_CONTROL(100);
				trunOver = false;
			}
		} catch (Exception e) {
		}
	}

}