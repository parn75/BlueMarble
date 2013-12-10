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
	
	public BufferedImage playerImage() { //�Ӹ� �̹���
		URL url = null;
		
		url = this.getClass().getResource("player 32 54.jpg");
		Image tmp = new ImageIcon(url).getImage();		
		BufferedImage bi = new BufferedImage(tmp.getWidth(null),tmp.getHeight(null), BufferedImage.TYPE_INT_ARGB); 
		
		Graphics2D ig2 = bi.createGraphics();		
		ig2.drawImage(tmp, 0, 0, null);
		
		//����� ����
		int exColor = -657418; //�ؿ� �ܼ���� �ּ��� ����� ��µǴ� ���� Ȯ���ؾ� �ȴ� �ƴϸ� getRGB(x, y)�� �ȼ��� �����ؼ� �����ؾ��Ѵ�.
		for (int x = 0; x < bi.getWidth(); x++) {
			for (int y = 0; y < bi.getHeight(); y++) {
				//System.out.print(bi.getRGB(x, y));
				if(y%20 == 0) System.out.println();
				if (bi.getRGB(x, y) == exColor)
					bi.setRGB(x, y, 0);  //����� ���� (BufferedImage ��ü������ ��밡��)
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
