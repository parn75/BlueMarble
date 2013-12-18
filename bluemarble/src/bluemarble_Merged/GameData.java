package bluemarble_Merged;

import java.io.Serializable;
import java.util.HashMap;

class GameData implements Serializable{
	HashMap<String, City> cityMap;
	HashMap<String, Player> playerData;
	Boolean event = false;
	public GameData(HashMap<String, City> cityMap,
			HashMap<String, Player> playerData,	Boolean event ) {
		this.event=event;
		this.cityMap = cityMap;
		this.playerData = playerData;
		System.out.println("ผผลอ"+event);
	}
}
