package view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Background {
	private ArrayList<BMCity> cities = new ArrayList<BMCity>();
	private ArrayList<BMPlayer> players = new ArrayList<BMPlayer>();
	private int XSize, YSize;
	
	
	public BufferedImage backgroundImage() { //머리 이미지
		BufferedImage bi = new BufferedImage(BMFrame.XSIZE, BMFrame.YSIZE, BufferedImage.TYPE_INT_ARGB); 
		Graphics2D ig2 = bi.createGraphics();	    
		
		
	    ig2.drawImage(cities.get(0).cityImg, 0, 0, null);
	    return bi;
	}
	
	public Background() {
		XSize = BMFrame.XSIZE/11;
		YSize = BMFrame.YSIZE/11;
		cities.add(new BMCity());
	}
	
}
