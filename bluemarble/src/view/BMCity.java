package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BMCity {
	private int City_SizeX, City_SizeY;
	private ArrayList<BufferedImage> buildings = new ArrayList<BufferedImage>();
	private String owner;
	private String name;
	BufferedImage cityImg;
	
	public BufferedImage CityImage() { //머리 이미지
		BufferedImage bi = new BufferedImage(City_SizeX, City_SizeY, BufferedImage.TYPE_INT_ARGB); 
		Graphics2D ig2 = bi.createGraphics();		
	    ig2.setPaint(Color.green);	    
	    ig2.fillRect(1, 1, City_SizeX-1, City_SizeY-1);
	    ig2.setPaint(Color.black);	
		ig2.drawRect(0, 0, City_SizeX, City_SizeX);
	    ig2.drawImage(buildings.get(0), 0, 0, null);
	    return bi;
	}
	
	public BMCity() {
		City_SizeX = BMFrame.XSIZE/11;
		City_SizeY = BMFrame.YSIZE/11;
		buildings.add(new BMBuilding().eiffelTower());
		cityImg = CityImage();
	}

}
