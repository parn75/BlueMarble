package view;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class BMBoard extends Canvas{
	BufferStrategy bs;
	BufferedImage background;
	
	public void render(Graphics g) {
		BufferedImage screen = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB); //ȭ�� ũ�⸸ŭ�� ���� �̹�����
		Graphics2D screenG = (Graphics2D) screen.getGraphics();
		screenG.drawImage(background, 0, 0, this); //����� ���� �׸���
		
		g.drawImage(screen, 0,0,this);	//��� �̹����� �ϼ��Ǿ����� ���� ȭ�鿡 ǥ���� �ش�.
		screenG.dispose();
	}
	
	public void updateBoard() {
		Graphics g = bs.getDrawGraphics();
		render(g);
		g.dispose();
		bs.show();
		if(bs.contentsLost()) System.out.println("content lost!");
	}
	
	public void setBufferStrategy(BufferStrategy bs) {
		this.bs = bs;
		background = new Background().backgroundImage();		
	}
}
