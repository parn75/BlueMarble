package blue_Merged2;
/*package mini_project_blumable;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTextArea;



class Table extends Canvas implements Runnable, MouseListener {
	// 이미지 생성
	protected final Image board = Toolkit.getDefaultToolkit().getImage(
			"./img/board.jpg");
	protected final Image user = Toolkit.getDefaultToolkit().getImage(
			"./img/user.png");
	protected final Image diceBtn = Toolkit.getDefaultToolkit().getImage(
			"./img/diceon.png");
	protected final Image diceRoll = Toolkit.getDefaultToolkit().getImage(
			"./img/dice3.png");
	protected final Image cityImg = Toolkit.getDefaultToolkit().getImage(
			"./img/city.png");
	protected final int[][] mapXY = { { 600, 380 }, { 500, 380 }, { 370, 380 },
			{ 250, 380 }, { 250, 310 }, { 250, 240 }, { 250, 150 },
			{ 350, 150 }, { 450, 150 }, { 600, 150 }, { 600, 220 },
			{ 600, 300 } };


	private Image offscreenImage;
	private Graphics offscreenGraphics;
	private Boolean f = false;
	private int dice1, dice2;
	private int mEventX, mEventY;
	private Rectangle diceBtnRect = new Rectangle(420, 250, 120, 80);
	private Rectangle mouseEventRect;
	private Purchase inCity;
	private ArrayList<Building> buildingList = new ArrayList<Building>();
	private ArrayList<Player> ply = new ArrayList<Player>();
	
	private int userNum = 0;
	private int totPrice = 0;
	Player dPlayer;
	Boolean trunOver = false;
	JTextArea lb = new JTextArea();

	public Table() { 
		TableInit();
		ply.add(new Player("유저 1", false));
		ply.add(new Player("유저 2", false));
		inCity = new Purchase();
		addMouseListener(this);
		lb.setEditable(false);
		Thread t = new Thread(this);
		t.start();

	}

	// paint
	public void paint(Graphics g) {
		Dimension currentSize = getSize();
		if (offscreenGraphics == null) {
			offscreenImage = createImage(currentSize.width, currentSize.height);

			if (offscreenImage == null)
				System.out.println("오프스크린 버퍼 생성 실패");
			else
				offscreenGraphics = offscreenImage.getGraphics();
			return;
		}
		offscreenGraphics.setColor(Color.WHITE);
		offscreenGraphics.fillRect(0, 0, currentSize.width, currentSize.height);
		offscreenGraphics.drawImage(board, 0, 0, this);
		offscreenGraphics.drawImage(user, ply.get(0).x, ply.get(0).y, this);
		offscreenGraphics
				.drawImage(user, ply.get(1).x + 10, ply.get(1).y, this);
		offscreenGraphics.drawImage(diceBtn, 420, 250, this);
		offscreenGraphics.drawImage(diceRoll, 420, 550, 420 + 63, 550 + 60,
				63 * dice1, 0, 63 * dice1 + 60, 60, this);
		offscreenGraphics.drawImage(diceRoll, 530, 550, 530 + 63, 550 + 60,
				63 * dice2, 0, 63 * dice2 + 60, 60, this);
		for (Building t : buildingList) {
			for(int j=0; j<t.buildCnt;j++){
			offscreenGraphics.drawImage(cityImg, mapXY[t.thisXY][0]+j*5,
					mapXY[t.thisXY][1], this);
			}
			// offscreenGraphics.drawImage(cityImg, mapXY[t.thisXY][0],
			// mapXY[t.thisXY][1],mapXY[t.thisXY][0]+10*t.buildCnt,mapXY[t.thisXY][1]+20,
			// 0,0,10*t.buildCnt+20,20,this);
		}
		// offscreenGraphics.drawImage(diceRoll, 420,250, this);
		// offscreenGraphics.drawImage(diceRoll, 420,250, this);
		offscreenGraphics.setColor(new Color(0));
		offscreenGraphics.drawString("1p 가진돈 : " + ply.get(0).money, 20, 20);
		offscreenGraphics.drawString("2p 가진돈 : " + ply.get(1).money, 20, 40);
		g.drawImage(offscreenImage, 0, 0, this);
	}// paint END
		// update

	public void update(Graphics g) {
		paint(g);
	}// updateEND

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			
			 * isInterrupted() 메소드를 while 문 조건으로 사용 이 스레스의 인스턴스가 interrupt()
			 * 메소드를 호출하면 isInterruped() 메소드는 true 를 반환
			 
			while (!Thread.currentThread().isInterrupted()) {
				repaint();
				animation();
				mouseEventCk();
			}
			Thread.sleep(1);
		} catch (InterruptedException e) {

		} finally {
			System.out.println("충돌스레드사망 !!");
		}
	}

	public void mouseEventCk() {
		if (mouseEventRect != null && trunOver == false) {
			if (diceBtnRect.intersects(mouseEventRect)) {
				System.out.println(mouseEventRect);
				f = true;
				trunOver = true;
			}
		}
		mouseEventRect = null;
	}

	public void animation() {
		dPlayer = ply.get(userNum);
		try {
			if (f == true) {
				for (int i = 0; i < 10; i++) {

					repaint();
					dice1 = (int) (Math.random() * 5 + 1);
					dice2 = (int) (Math.random() * 5 + 1);
					Thread.sleep(100);

				}
				dice1 = (int) (Math.random() * 5 + 1);
				dice2 = (int) (Math.random() * 5 + 1);
				userMove();
			}
		} catch (Exception e) {
		}
	}// func END

	public void userMove() {
		try {
			Thread.sleep(300);
			f = false;
			int diceRst = dice1 + dice2;
			// 움직임
			for (int i = 1; i < diceRst + 3; i++) {
				dPlayer.thisXY++;
				if (dPlayer.thisXY >= 12)
					dPlayer.thisXY = 0;
				dPlayer.x = mapXY[dPlayer.thisXY][0];
				dPlayer.y = mapXY[dPlayer.thisXY][1];
				repaint();
				System.out.println(dPlayer.thisXY+"/"+(diceRst+3));
				Thread.sleep(300);
			}
			// 이동 이벤트처리
			City c = null;
			if (ply != null)
				c = cityMap.get(cityNameList[ply.get(userNum).thisXY]);
			// 도착한 위치에 주인이없으면 건물을 사는것을 호출하고 있으면 돈을 지불하고 턴을 넘긴다
			System.out.println(dPlayer.name+"/"+dPlayer.thisXY+"/"+c.getCityName());
			if (c != null && c.getCollecterName() == null) {
				if (ply != null)
					c = (City) cityMap
							.get(cityNameList[ply.get(userNum).thisXY]);
				if (c != null) {
					int[] p = c.getPrice();
					System.out.println(p[0] + "/" + p[1]);
					lb.setText("도시이름 " + c.getCityName() + "\n건물 가격 :" + p[0]);
				}
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
				if (userNum < 1)
					userNum++;
				else
					userNum = 0;
				trunOver = false;
				
			}  else {
				if (userNum < 1)
					userNum++;
				else
					userNum = 0;
				trunOver = false;
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		mEventX = e.getX();
		mEventY = e.getY();
		mouseEventRect = new Rectangle(mEventX, mEventY, 10, 10);
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

}*/