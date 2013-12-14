package network12_14;

import java.io.Serializable;

public class WaitingRoomInfo implements Serializable{
	private static final long serialVersionUID = -1896495121315888442L;
	int roomNum=-1;
	int playerNum=0;	
	StringBuffer roomName;	
	StringBuffer[] playerNames = new StringBuffer[4];
	boolean isStarted = false;
	
	public void printRoom() {
		System.out.println("Room Name: " + roomName.toString() + ", RoomNum: " + roomNum + ", Number of Players: " + playerNum);
		for(int i=0;i<playerNames.length;i++)System.out.print(playerNames[i].toString() + " ");
		System.out.println();
	}
	
	public boolean getIsStarted() {
		return isStarted;
	}

	public void setIsStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}

	public WaitingRoomInfo() {
		for(int i=0;i<playerNames.length;i++) playerNames[i] = new StringBuffer("None");
		roomName = new StringBuffer("");
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
	public StringBuffer getRoomName() {
		return roomName;
	}
	public void setRoomName(StringBuffer roomName) {
		this.roomName = roomName;		
	}
	public StringBuffer[] getPlayerNames() {
		return playerNames;
	}
	public void setPlayerNames(StringBuffer[] playerNames) {
		this.playerNames = playerNames;
	}
    public int joinRoom(StringBuffer playerName) {	//플레이어가 참가한 경우 ServerThread에서만 호출   
		if(playerNum<4 && playerNum>=0) {
			for(int i=0; i<playerNames.length;i++) {				
				if(playerNames[i].toString().equals("None")) { 
					playerNames[i] = playerName;
					break;
				}				
			}
			playerNum++;		
			return playerNum;
		}
		return -1;
	}
	
	public int deletePlayer(String player) {
		for(int i=0;i<playerNames.length;i++) if(playerNames[i].toString().equals(player)) playerNames[i] = new StringBuffer("None");		
		playerNum--;
		return playerNum;
	}
}
