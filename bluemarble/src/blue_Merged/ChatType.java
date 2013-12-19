package blue_Merged;

public enum ChatType {
	
	/*	Broadcast(전체 메세지), Whisper(귓속말), Image(사진), File(파일), ID(최초 접속시 ID), ConnectorList(접속자 명단), Join(대기실 접속), RoomStatus(대기실 전체 정보), 
	 * GameData(게임 데이터), WaitingRoomStatus(사용안함), WaitingRoomChat(대기실 대화), WaitingRoomExit(대기실 나감), Exit(프로그램 나감);	  
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
