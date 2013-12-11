package networkbackup_12_11_morning;

import java.io.Serializable;

public class ChatData implements Serializable{
	private static final long serialVersionUID = -1940449923475179341L;		
	
	public ChatType type;
	public String[] to;
	public Object data;
	public String from;

	public ChatData(ChatType type, Object data) { //모두에게 data를		
		this.type = type;
		this.from = null;
		this.to = null;
		this.data = data;
	}	

	public ChatData(ChatType type, String[] to, Object data) { //to에게 data를		
		this.type = type;
		this.from = null;
		this.to = to;
		this.data = data;
	}
	
	public ChatData(ChatType type, String from, Object data) { //from이 모두에게 data를
		this.type = type;
		this.from = from;
		this.to = null;
		this.data = data;
	}
	
	public ChatData(ChatType type, String from, String[] to, Object data) { //from이 to에게 data를
		this.type = type;
		this.to = to;
		this.from = from;
		this.data = data;
	}
}
