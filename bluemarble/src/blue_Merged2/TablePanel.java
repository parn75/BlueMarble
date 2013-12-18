package blue_Merged2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class TablePanel extends JPanel implements MouseListener, Runnable {
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
	protected final Rectangle BTNRECT = new Rectangle(555, 290, 200, 200);
	protected final int[][] mapXY = { { 530, 570 }, { 480, 528 }, { 425, 485 },
			{ 365, 445 }, { 315, 405 }, { 265, 365 }, { 215, 325 },
			{ 165, 285 }, { 105, 245 }, { 180, 215 }, { 235, 190 },
			{ 295, 160 }, { 355, 135 }, { 410, 105 }, { 465, 85 }, { 515, 55 },
			{ 585, 20 }, { 655, 60 }, { 705, 90 }, { 760, 125 }, { 815, 155 },
			{ 870, 185 }, { 933, 220 }, { 1000, 260 }, { 1085, 310 },
			{ 1005, 352 }, { 940, 390 }, { 870, 420 }, { 805, 455 },
			{ 748, 490 }, { 690, 515 }, { 630, 550 } };

	protected final int[][] buildingXY = {
			{ mapXY[1][0] + 35, mapXY[1][1] - 15 },
			{ mapXY[1][0] + 55, mapXY[1][1] },
			{ mapXY[1][0] + 75, mapXY[1][1] + 15 },
			{ mapXY[3][0] + 40, mapXY[3][1] - 12 },
			{ mapXY[3][0] + 57, mapXY[3][1] },
			{ mapXY[3][0] + 75, mapXY[3][1] + 10 },
			{ mapXY[4][0] + 33, mapXY[4][1] - 16 },
			{ mapXY[4][0] + 52, mapXY[4][1] },
			{ mapXY[4][0] + 73, mapXY[4][1] + 13 },
			{ mapXY[5][0] + 33, mapXY[5][1] - 15 },
			{ mapXY[5][0] + 48, mapXY[5][1] },
			{ mapXY[5][0] + 65, mapXY[5][1] + 12 },
			{ mapXY[6][0] + 35, mapXY[6][1] - 10 },
			{ mapXY[6][0] + 50, mapXY[6][1] + 5 },
			{ mapXY[6][0] + 65, mapXY[6][1] + 15 },
			{ mapXY[7][0] + 40, mapXY[7][1] },
			{ mapXY[7][0] + 55, mapXY[7][1] + 10 },
			{ mapXY[7][0] + 70, mapXY[7][1] + 20 },
			{ mapXY[9][0] - 30, mapXY[9][1] + 5 },
			{ mapXY[9][0] - 15, mapXY[9][1] - 2 },
			{ mapXY[9][0], mapXY[9][1] - 10 },
			{ mapXY[10][0] - 30, mapXY[10][1] + 5 },
			{ mapXY[10][0] - 13, mapXY[10][1] - 2 },
			{ mapXY[10][0] + 6, mapXY[10][1] - 10 },
			{ mapXY[11][0] - 29, mapXY[11][1] + 6 },
			{ mapXY[11][0] - 12, mapXY[11][1] - 1 },
			{ mapXY[11][0] + 5, mapXY[11][1] - 9 },
			{ mapXY[12][0] - 33, mapXY[12][1] + 6 },
			{ mapXY[12][0] - 15, mapXY[12][1] - 3 },
			{ mapXY[12][0] + 7, mapXY[12][1] - 12 },
			{ mapXY[13][0] - 28, mapXY[13][1] + 6 },
			{ mapXY[13][0] - 11, mapXY[13][1] - 3 },
			{ mapXY[13][0] + 5, mapXY[13][1] - 10 },
			{ mapXY[15][0] - 30, mapXY[15][1] + 6 },
			{ mapXY[15][0] - 14, mapXY[15][1] - 1 },
			{ mapXY[15][0] + 1, mapXY[15][1] - 7 },
			{ mapXY[17][0] + 42, mapXY[17][1] - 6 },
			{ mapXY[17][0] + 58, mapXY[17][1] + 3 },
			{ mapXY[17][0] + 75, mapXY[17][1] + 12 },
			{ mapXY[18][0] + 42, mapXY[18][1] - 6 },
			{ mapXY[18][0] + 58, mapXY[18][1] + 3 },
			{ mapXY[18][0] + 75, mapXY[18][1] + 12 },
			{ mapXY[19][0] + 38, mapXY[19][1] - 10 },
			{ mapXY[19][0] + 56, mapXY[19][1] - 1 },
			{ mapXY[19][0] + 75, mapXY[19][1] + 9 },
			{ mapXY[20][0] + 38, mapXY[20][1] - 10 },
			{ mapXY[20][0] + 56, mapXY[20][1] - 1 },
			{ mapXY[20][0] + 75, mapXY[20][1] + 9 },
			{ mapXY[22][0] + 33, mapXY[22][1] - 10 },
			{ mapXY[22][0] + 56, mapXY[22][1] + 3 },
			{ mapXY[22][0] + 77, mapXY[22][1] + 15 },
			{ mapXY[23][0] + 33, mapXY[23][1] - 10 },
			{ mapXY[23][0] + 56, mapXY[23][1] + 3 },
			{ mapXY[23][0] + 77, mapXY[23][1] + 15 },
			{ mapXY[25][0] - 45, mapXY[25][1] + 6 },
			{ mapXY[25][0] - 23, mapXY[25][1] - 6 },
			{ mapXY[25][0] - 3, mapXY[25][1] - 18 },
			{ mapXY[26][0] - 40, mapXY[26][1] + 2 },
			{ mapXY[26][0] - 21, mapXY[26][1] - 8 },
			{ mapXY[26][0] - 3, mapXY[26][1] - 18 },
			{ mapXY[28][0] - 40, mapXY[28][1] + 3 },
			{ mapXY[28][0] - 21, mapXY[28][1] - 7 },
			{ mapXY[28][0] - 3, mapXY[28][1] - 17 },
			{ mapXY[30][0] - 39, mapXY[30][1] + 2 },
			{ mapXY[30][0] - 20, mapXY[30][1] - 5 },
			{ mapXY[30][0] - 1, mapXY[30][1] - 15 },
			{ mapXY[31][0] - 36, mapXY[31][1] - 4 },
			{ mapXY[31][0] - 19, mapXY[31][1] - 10 },
			{ mapXY[31][0], mapXY[31][1] - 19 } };
	protected final int[][] profileXY = { { 20, 20 }, { 20, 600 },
			{ 1100, 600 }, { 1100, 20 } };
	public JButton btn = new JButton();
	private JPanel bottomPanel = new JPanel();
	public JTextArea chatView;
	public JTextField chatInput;
	private JScrollPane chetScroll;
	private JSplitPane chetResize;
	private JLabel chetDummyLB = new JLabel();
	private JPanel btPanel = new JPanel();
	private Purchase inCity=new Purchase();
	private String currentUser = null;

	
	public static ArrayList<Building> buildingList = new ArrayList<Building>();
	public static Boolean trunOver = false;
	private int dice1, dice2;
	public static Boolean f = false;
	Player dPlayer;
	WaitingRoomUI waitingRoom;

	public TablePanel(WaitingRoomUI waitingRoom) {
		this.waitingRoom = waitingRoom;
		control.setWaitingRoom(waitingRoom);
		this.isDoubleBuffered();
		StringBuffer[] sb = waitingRoom.myRoomInfo.getPlayerNames();
		// 스트링배열 로받아
		String[] names = new String[4];
		for (int i = 0; i < sb.length; i++) {
			if (sb[i] != null)
				names[i] = sb[i].toString();
			System.out.println("초기화" + names[i]);
		}

		control.setNickName(names);
		//JOptionPane.showMessageDialog(null, waitingRoom.client.getMyName());
		control.setIAM(waitingRoom.client.getMyName());// 클라이언트 자신의 이름
		System.out.println("자신" + waitingRoom.client.getMyName());
		System.out.println("체크" + control.getNickName()[0]);
		control.playerAdd();
		// TableInit();
		// ply.add(new Player("유저 1", false));
		// ply.add(new Player("유저 2", false));
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
		chetResize = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chetDummyLB,
				chetScroll);
		chetResize.setDividerLocation(300);
		chetResize.setBounds(310, 350, 620, 360);
		chetResize.setOpaque(false);
		chetResize.setBorder(null);
		
		bottomPanel.addMouseListener(this);
		chetResize.addMouseListener(this);
		addMouseListener(this);
		// add(chetScroll);
		
		inCity.setBounds(510,200, 200, 250);
		inCity.addMouseListener(this);
		add(inCity);
		inCity.setVisible(false);
		add(chatInput);
		add(chetResize);
		

	}

	/*
	 * public void TableInit() { int[] priceInt = { 1000, 2000, 3000, 4000 };
	 * for (int i = 0; i < cityNameList.length; i++) {
	 * cityMap.put(cityNameList[i], new City(i + 1, priceInt, cityNameList[i]));
	 * } }
	 */
	public BufferedImage drawRoom(String playerName,int money, int firstColor, int medianColor) {		
		int roomX = 200, roomY = 100;
		int fontSize = 30;		
		BufferedImage bi = new BufferedImage(roomX, roomY, BufferedImage.TYPE_INT_ARGB); 
		Graphics2D ig2 = bi.createGraphics();
		int c = 255/roomY;
		for(int i=0;i<=roomY;i++) {			
			Color color = new Color(c*i,firstColor , medianColor);
			ig2.setPaint(color);
			ig2.drawLine(0, i, roomX, i);
		}

	    ig2.setPaint(Color.black);
	    Font f = new Font("굴림", Font.BOLD, fontSize);
	    ig2.setFont(f);
	    int fx = (roomX - fontSize*playerName.length()/2)/2;
	    ig2.drawString(playerName, 30, 40);
	    f = new Font("굴림", Font.BOLD, 15);
	    ig2.setFont(f);
	    ig2.drawString("소유금: " + money, 30, 80);
	    return bi;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		BufferedImage bi = new BufferedImage(this.getWidth(), this.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g2 = bi.getGraphics();
		g2.drawImage(backGroundImg, 0, 0, 1240, 760, this);
		//g2.drawImage(diceBtn, 555, 290, this);
		
		
		if (control.getIAM().equals(control.getNickName()[control.getTurnCP()]) && trunOver == false ) g2.drawImage(diceBtn, 555, 290, this);

		for (int i = 0; i < control.getNickName().length; i++) {
			if (!control.getNickName()[i].equals("None"))
				g2.drawImage(
						user,
						control.playerData.get(control.getNickName()[i]).getX(),
						control.playerData.get(control.getNickName()[i]).getY(),
						this);
		}
		g2.setColor(new Color(0));
		for (Building t : buildingList) {
			for (int j = 0; j < t.buildCnt; j++) {
				g2.drawImage(building, mapXY[t.thisXY][0] + j * 5,
						mapXY[t.thisXY][1], this);
			}
		}
		for(int i=0; i<control.turnMax;i++){
		if(control.getNickName()[i].equals(control.getIAM()))
			g2.drawString("자신"+control.getIAM(),control.playerData.get(control.getNickName()[i]).getX(),
					control.playerData.get(control.getNickName()[i]).getY());
		else
			g2.drawString((i+1)+"피", 
					control.playerData.get(control.getNickName()[i]).getX(),
					control.playerData.get(control.getNickName()[i]).getY());
		}

		
		g2.drawImage(drawRoom(control.getNickName()[0], control.playerData.get(control.getNickName()[0]).getMoney(),
				100, 100), 20, 20, this);
		g2.drawImage(drawRoom(control.getNickName()[1], control.playerData.get(control.getNickName()[0]).getMoney(),
				50, 100), 1000, 20, this);
		g2.drawImage(drawRoom(control.getNickName()[2], control.playerData.get(control.getNickName()[0]).getMoney(),
				150, 100), 20, 600, this);
		g2.drawImage(drawRoom(control.getNickName()[3], control.playerData.get(control.getNickName()[0]).getMoney(),
				200, 100), 1000, 600, this);
		// 좌표위치 테스트
		// for(int i=0; i<mapXY.length; i++){
		//	 g2.setColor(Color.black);
		// g2.drawRect(mapXY[i][0], mapXY[i][1],50, 50);
		// }
		g.drawImage(bi, 0, 0, this);
		
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
		chetScroll
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chetScroll.setVisible(true);
		chetScroll.setBounds(310, 550, 620, 160);

		chatInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyPressed(e);
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// chatView.append(chatInput.getText() + "\n");
					ChatData cd = new ChatData(ChatType.WaitingRoomChat,
							chatInput.getText());
					waitingRoom.client.send(cd);
					chatInput.setText("");
				}
			}
		});// KeyAdapter END
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	 	// TODO Auto-generated method stub
	Rectangle mouseEventRect = new Rectangle(e.getX(),
				e.getY(), 10, 10);
	System.out.println("버튼반응 현제턴 "+control.getNickName()[control.getTurnCP()]+"자신"+control.getIAM()+"마우스리스너 온"+trunOver);
		if (trunOver!=true&&control.getIAM().equals(control.getNickName()[control.getTurnCP()])
				&&mouseEventRect.intersects(BTNRECT)) {
			System.out.println("눌림");
			control.event=true;
			f = true;
			trunOver = true;
			System.out.println("나의 아이디:" + control.getIAM()+"턴값"+control.getTurnCP()
					+"턴인사람 이름"+control.getNickName()[control.getTurnCP()]);
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
				// 에니메이션용 쓰레드
				//repaint();
				currentUser = control.currentUserName();
				if (chatInput != null && trunOver != true)
					//chatInput.requestFocus();
					synchronized (this) {
						
					
				if(control.event==true){
					System.out.println("여까진옴");
				animation();
				f=true;
				}
					}
			//	else
					//System.out.println("으아악"+control.event);

			}

			
			Thread.sleep(100);

		} catch (InterruptedException e) {

		} finally {
			System.out.println("쓰레드 사망");
		}
	}

	public void animation() {
		// if(ply.size()!=0)

		System.out.println("여까진옴2");
		dPlayer = (Player) control.playerData.get
				(control.getNickName()[control.getTurnCP()]); // 지금 플레이중인
		System.out.println("턴"+control.getTurnCP()+"/"+control.event+"/가진 주사위값"+dPlayer.userDics);
		//3 유저.
	//Syste3m.out.println("애니메이션 입장 :"+dPlayer.name);
		try {
//			if (f == true) {
//				for (int i = 0; i < 10; i++) {
//
//					repaint();
//					dice1 = (int) (Math.random() * 5 + 1);
//					dice2 = (int) (Math.random() * 5 + 1);
//					Thread.sleep(100);
//
//				}
//				
//			}
			userMove();
		} catch (Exception e) {
		}
		repaint();
	}// func END

	public void userMove() {
		try {
			System.out.println("무브"); 
			System.out.println("턴인놈 이름"+dPlayer.name);
			System.out.println (control.event+"이벤트"+dPlayer.userDics);
			Thread.sleep(300);
			f = false;
			control.GAME_CONTROL(1);
			
			//int diceRst = control.getFirstDics() + control.getSecondDics();
			int diceRst = dPlayer.userDics;
			System.out.println(dPlayer+"유저");
			System.out.println("턴값"+control.getTurnCP()+"다이스값" + diceRst);
			// 움직임
			for (int i = 0; i < diceRst ; i++) {
				dPlayer.thisXY++;
				if (dPlayer.thisXY >= 32)
					dPlayer.thisXY = 0;
				dPlayer.x = mapXY[dPlayer.thisXY][0];
				dPlayer.y = mapXY[dPlayer.thisXY][1];
				repaint();
				System.out.println(dPlayer.thisXY + "/" + (diceRst));
				Thread.sleep(300);
			}
			System.out.println(dPlayer.name + "/" + dPlayer.thisXY);
			// 이동 이벤트처리
			City c = null;
			System.out.println("1");

			c = (City) control.cityMap
					.get(control.getCityNameList()[control.playerData
							.get(control.getIAM()).thisXY]);
			// System.out.println(cityMap.get(cityNameList[ply.get(userNum).thisXY]).getCityName());
			// System.out.println("2//"+ply.get(userNum).thisXY);
			// 도착한 위치에 주인이없으면 건물을 사는것을 호출하고 있으면 돈을 지불하고 턴을 넘긴다
			// System.out.println(c.getCityName());

			System.out.println("3");
			if (dPlayer.name.equals(control.getIAM()) && c != null
					&& c.getCollecterName() == null) {
				if (control.playerData != null)
					c = (City) control.getCityMap().get(
							control.getCityNameList()[control.playerData
									.get(control.getIAM()).thisXY]);
				if (c != null) {
					int[] p = c.getPrice();
					System.out.println(p[0] + "/" + p[1]);
					inCity.setCityData(c,c.getCityName(), p[0]);

				}
				System.out.println("지금 도시이름"+c.getCityName());
				//inCity.setCityData(c,c.getCityName(), 5000);
				System.out.println("구매");
				inCity.updateUI();
				inCity.setVisible(true);
				//control.GAME_CONTROL(100);
				//f=false;
				//trunOver = false;
			} else if (control.playerData.get(control.getIAM()).money < 0) {
				System.exit(0);
			} else if (c != null
					&& c.getCollecterName() != null
					&& !c.getCollecterName().equals(
							control.playerData.get(control.getIAM()).name)) {
				System.out.println("걸림");
				int tMoney = 0;
				for (int i = 0; i < c.getBuildList().length; i++) {
					// System.out.println(ply.get(userNum).name+"걸림2");
					// System.out.println("리스트"+c.getBuildList()[i]);
					if (c.getBuildList()[i] == true) {
						tMoney += c.getPrice()[i];
						System.out.println(i + "//" + tMoney + "//"
								+ c.getPrice()[i]);
					} else {
						System.out.println("false" + i + "//" + tMoney + "//"
								+ c.getPrice()[i]);
					}
					System.out.println(tMoney);
					
				}
				System.out.println("걸림3");
				control.playerData.get(control.getIAM()).money -= tMoney * 2; // 현제유저의
																				// 소지금(계산)
				control.playerData.get(c.getCollecterName()).money += tMoney * 2;
				/*
				 * for(Player t: control.playerData()){
				 * if(t.name.equals(c.getCollecterName())==true) t.money+=tMoney
				 * * 2; }
				 */
				System.out.println("걸림4");
				// if (userNum < ply.size())
				// userNum++;
				// else
				// userNum = 0;
				control.turnOver();
				control.GAME_CONTROL(100);
				trunOver = false;
				f=false;
				

			} else {
				// if (userNum < 1)
				// userNum++;
				// else
				// userNum = 0;
				f=false;
				control.turnOver();
				control.GAME_CONTROL(100);
				trunOver = false;
				
			}
		} catch (Exception e) {
		}
	}

	 
}