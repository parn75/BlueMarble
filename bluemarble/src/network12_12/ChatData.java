package network12_12;

import java.io.Serializable;

public class ChatData implements Serializable{
	private static final long serialVersionUID = -1940449923475179341L;		
	
	public ChatType type;
	public String[] to;
	public Object data;
	public ChatType getType() {
		return type;
	}

	public void setType(ChatType type) {
		this.type = type;
	}

	public String[] getTo() {
		return to;
	}

	public void setTo(String[] to) {
		this.to = to;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

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
