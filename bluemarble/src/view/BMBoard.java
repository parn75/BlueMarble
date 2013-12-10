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
		BufferedImage screen = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB); //화면 크기만큼의 버퍼 이미지에
		Graphics2D screenG = (Graphics2D) screen.getGraphics();
		screenG.drawImage(background, 0, 0, this); //배경을 먼저 그리고
		
		g.drawImage(screen, 0,0,this);	//모든 이미지가 완성되었으면 이제 화면에 표시해 준다.
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
