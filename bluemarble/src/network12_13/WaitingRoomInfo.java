package network12_13;

import java.io.Serializable;

public class WaitingRoomInfo implements Serializable{
	int roomNum=-1;
	int playerNum=0;	
	String roomName;	
	String[] PlayerNames = new String[4];
	boolean isStarted = false;
	
	public boolean isStarted() {
		return isStarted;
	}

	public void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}

	public WaitingRoomInfo() {
		for(int i=0;i<PlayerNames.length;i++) PlayerNames[i] = "None";
		roomName="";
	}
	
	public int getRoomNum() {
		return roomNum;
	}
	public int getPlayerNum() {
		return playerNum;
	}

	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}

	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}
	public int getRoomName() {
		return roomNum;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String[] getPlayerNames() {
		return PlayerNames;
	}
	public void setPlayerNames(String[] PlayerNames) {
		this.PlayerNames = PlayerNames;
	}
	public void addPlayer(String player) {
		this.PlayerNames[playerNum] = player;
		playerNum++;
	}
	
    public int joinRoom(String playerName) {	//플레이어가 참가한 경우 ServerThread에서만 호출
		if(playerNum<=4) {
			PlayerNames[playerNum] = playerName;
			System.out.println(PlayerNames[playerNum]);				
			if(playerNum <4) playerNum++;		
			return playerNum;
		}
	
		if(playerNum>4) playerNum = 4;
		return playerNum;
	}
	
	public int deletePlayer(String player) {
		for(int i=0;i<PlayerNames.length;i++) {
			if(PlayerNames[i].equals(player)) {
				for(int j=i;j<PlayerNames.length-1;j++) 
					if(PlayerNames[j+1] != null || PlayerNames[j+1] !="None") PlayerNames[j] = PlayerNames[j+1];
					else PlayerNames[j] = "None";
				playerNum--;				
			}
		}
		return playerNum;
	}
}
