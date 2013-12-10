package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class BMPlayer {
	private BufferedImage playerImg;
	private Color color;
	private int location;
	private int money;
	private int status;	
	
	public BufferedImage playerImage() { //머리 이미지
		URL url = null;
		
		url = this.getClass().getResource("player 32 54.jpg");
		Image tmp = new ImageIcon(url).getImage();		
		BufferedImage bi = new BufferedImage(tmp.getWidth(null),tmp.getHeight(null), BufferedImage.TYPE_INT_ARGB); 
		
		Graphics2D ig2 = bi.createGraphics();		
		ig2.drawImage(tmp, 0, 0, null);
		
		//투명색 적용
		int exColor = -657418; //밑에 콘솔출력 주석을 지우면 출력되는 값을 확인해야 된다 아니면 getRGB(x, y)로 픽셀을 선택해서 대입해야한다.
		for (int x = 0; x < bi.getWidth(); x++) {
			for (int y = 0; y < bi.getHeight(); y++) {
				//System.out.print(bi.getRGB(x, y));
				if(y%20 == 0) System.out.println();
				if (bi.getRGB(x, y) == exColor)
					bi.setRGB(x, y, 0);  //투명색 지정 (BufferedImage 객체에서만 사용가능)
			}
		}
		
	    return bi;
	}
	
	public static void main(String[] args) {
		BMPlayer bmb = new BMPlayer();
		JFrame f = new JFrame("test");
		ImageIcon ii = new ImageIcon(bmb.playerImage());
		JLabel lbl = new JLabel(ii);
		f.add(lbl);
		f.setSize(500,500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);		
	}
}
