package view;



public class City {
	private int cityNum;
	private String cityName; // ��Ƽ�� �̸�(Ű��?)
	private int[] buildPrice = new int[4];
	private String collecterName=null;  // ��Ƽ�� ������
	private Boolean[] buildList = new Boolean[4];
	public City(int cityNum,int[] buildPrice,String cityName) {
		this.cityNum=cityNum;
		this.buildPrice = buildPrice;  // ��Ƽ�� ��(��, �ܵ�, ����, ȣ��)
		this.cityName =cityName; // ��Ƽ�� �̸�(Ű��?)
	}

	public int[] getPrice() {
		return buildPrice;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCollecterName() {
		return collecterName;
	} 
	
	public void setCollecterName(String collecterName){
		this.collecterName = collecterName;
	}

	public Boolean[] getBuildList() {
		return buildList;
	}

	public void setBuildList(Boolean[] buildList) {
		this.buildList = buildList;
	}

	
}



  