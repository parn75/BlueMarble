package blue_Merged;

public enum ChatType {
	
	/*	Broadcast(��ü �޼���), Whisper(�ӼӸ�), Image(����), File(����), ID(���� ���ӽ� ID), ConnectorList(������ ���), Join(���� ����), RoomStatus(���� ��ü ����), 
	 * GameData(���� ������), WaitingRoomStatus(������), WaitingRoomChat(���� ��ȭ), WaitingRoomExit(���� ����), Exit(���α׷� ����);	  
	 */
	
	Broadcast(1), Whisper(2), Image(3), File(4), ID(5), ConnectorList(6), Join(7), RoomStatus(8), GameData(9),
	WaitingRoomStatus(10), WaitingRoomChat(11), WaitingRoomExit(12), Exit(13), GameStart(14);
	
	
	final int num;
	
	private ChatType(int num){
		this.num = num;
	}
	
	public int getNum() {
		return num;
	}
	
}
