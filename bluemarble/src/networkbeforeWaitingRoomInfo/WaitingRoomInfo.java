package networkbeforeWaitingRoomInfo;

import java.io.Serializable;

public class WaitingRoomInfo implements Serializable{
	private static final long serialVersionUID = -1896495121315888442L;
	int roomNum=-1;
	int playerNum=0;	
	String roomName;	
	String[] playerNames = new String[4];
	boolean isStarted = false;
	
	public void printRoom() {
		System.out.println("Room Name: " + roomName + ", RoomNum: " + roomNum + ", Number of Players: " + playerNum);
		for(int i=0;i<playerNames.length;i++)System.out.print(playerNames[i] + " ");
		System.out.println();
	}
	
	public boolean getIsStarted() {
		return isStarted;
	}

	public void setIsStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}

	public WaitingRoomInfo() {
		for(int i=0;i<playerNames.length;i++) playerNames[i] = "None";
		roomName="";
	}
	

	public int getPlayerNum() {
		return playerNum;
	}

	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}
	public int getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String[] getPlayerNames() {
		return playerNames;
	}
	public void setPlayerNames(String[] playerNames) {
		this.playerNames = playerNames;
	}
    public int joinRoom(String playerName) {	//플레이어가 참가한 경우 ServerThread에서만 호출   
		if(playerNum<4) {
			playerNames[playerNum] = playerName;
			System.out.println(playerNames[playerNum]);				
			if(playerNum <4) playerNum++;		
			return playerNum;
		}
		return -1;
	}
	
	public int deletePlayer(String player) {
		for(int i=0;i<playerNames.length;i++) {
			if(playerNames[i].equals(player)) {
				for(int j=i;j<playerNames.length-1;j++) 
					if(playerNames[j+1] != null || playerNames[j+1] !="None") playerNames[j] = playerNames[j+1];
					else playerNames[j] = "None";
				playerNum--;				
			}
		}
		return playerNum;
	}
}
