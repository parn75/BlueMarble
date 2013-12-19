package blue_Merged;

import java.io.Serializable;
import java.util.HashMap;

class GameData implements Serializable{
	HashMap<String, City> cityMap;
	HashMap<String, Player> playerData;
	Boolean event = false;
	int dicetot=0;
	public GameData(HashMap<String, City> cityMap,
			HashMap<String, Player> playerData,	Boolean event,int dicetot ) {
		this.event=event;
		this.dicetot=dicetot;
		this.cityMap = cityMap;
		this.playerData = playerData;
		System.out.println("ผผลอ"+event);
	}
}
