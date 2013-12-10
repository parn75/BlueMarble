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
		
		//투명색 적용
		int exColor = -1; //밑에 콘솔출력 주석을 지우면 출력되는 값을 확인해야 된다 아니면 getRGB(x, y)로 픽셀을 선택해서 대입해야한다.
		for (int x = 0; x < bi.getWidth(); x++) {
			for (int y = 0; y < bi.getHeight(); y++) {
				//System.out.print(bi.getRGB(x, y));
				if(y%20 == 0) System.out.println();
				if (bi.getRGB(x, y) == exColor || (bi.getRGB(x, y) >= -194000 && bi.getRGB(x,y) <= 199000))
					bi.setRGB(x, y, 0);  //투명색 지정 (BufferedImage 객체에서만 사용가능)
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
