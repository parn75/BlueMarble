package newNetwork;

public enum ChatType {
	Broadcast(1), Whisper(2), Image(3), File(4), ID(5), ConnectorList(6), Join(7), RoomStatus(8), GameData(9),
	WaitingRoomStatus(10);
	
	final int num;
	
	private ChatType(int num){
		this.num = num;
	}
	
	public int getNum() {
		return num;
	}
	
}
