package view;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class BMBuilding {
	
	public BufferedImage eiffelTower() {
		URL url = null;
		
		url = this.getClass().getResource("Eiffel Tower 94.94.jpg");
		Image tmp = new ImageIcon(url).getImage();		
		BufferedImage bi = new BufferedImage(tmp.getWidth(null),tmp.getHeight(null), BufferedImage.TYPE_INT_ARGB); 
		
		Graphics2D ig2 = bi.createGraphics();		
		ig2.drawImage(tmp, 0, 0, null);
		
		//����� ����
		int exColor = -1; //�ؿ� �ܼ���� �ּ��� ����� ��µǴ� ���� Ȯ���ؾ� �ȴ� �ƴϸ� getRGB(x, y)�� �ȼ��� �����ؼ� �����ؾ��Ѵ�.
		for (int x = 0; x < bi.getWidth(); x++) {
			for (int y = 0; y < bi.getHeight(); y++) {
				//System.out.print(bi.getRGB(x, y));
				if(y%20 == 0) System.out.println();
				if (bi.getRGB(x, y) == exColor || (bi.getRGB(x, y) >= -194000 && bi.getRGB(x,y) <= 199000))
					bi.setRGB(x, y, 0);  //����� ���� (BufferedImage ��ü������ ��밡��)
			}
		}
		
	    return bi;
	}
	
	public static void main(String[] args) {
		BMBuilding bmb = new BMBuilding();
		JFrame f = new JFrame("test");
		ImageIcon ii = new ImageIcon(bmb.eiffelTower());
		JLabel lbl = new JLabel(ii);
		f.add(lbl);
		f.setSize(500,500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);		
	}
	
}
