package backup1205;

public enum ChatType {
	Broadcast(1), Whisper(2), Image(3), File(4), ID(5), ConnectorList(6);
	
	final int num;
	
	private ChatType(int num){
		this.num = num;
	}
	
	public int getNum() {
		return num;
	}
	
}
