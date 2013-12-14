package view;



public class City {
	private int cityNum;
	private String cityName; // 시티의 이름(키값?)
	private int[] buildPrice = new int[4];
	private String collecterName=null;  // 시티의 소유자
	private Boolean[] buildList = new Boolean[4];
	public City(int cityNum,int[] buildPrice,String cityName) {
		this.cityNum=cityNum;
		this.buildPrice = buildPrice;  // 시티의 값(땅, 콘도, 빌딩, 호텔)
		this.cityName =cityName; // 시티의 이름(키값?)
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



  