package bluemarble12_17;

public class City {
	private int cityNum;
	private String cityName; // ��Ƽ�� �̸�(Ű��?) 
	private int[] buildPrice = new int[4]; // ��Ƽ�� ��(��, �ܵ�, ����, ȣ��)
	private String collecterName=null;  // ��Ƽ�� ������
	private Boolean[] buildList = new Boolean[4];
	
	public City(int cityNum,int[] buildPrice,String cityName) {
		this.cityNum=cityNum;
		this.buildPrice = buildPrice;  
		this.cityName =cityName;
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

	@Override
	public String toString(){ 
		return "cityNum : "+cityNum+"\n"+
				"cityName : "+cityName+"\n"+
				"builePrice[] : "+buildPrice+"\n"+
				"collecterName : "+collecterName+"\n"+
				"bulidList[] : "+buildList+"\n";
	} 

}



  