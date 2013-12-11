package networkbackup_12_11_morning;

import java.io.Serializable;

public class ChatData implements Serializable{
	private static final long serialVersionUID = -1940449923475179341L;		
	
	public ChatType type;
	public String[] to;
	public Object data;
	public String from;

	public ChatData(ChatType type, Object data) { //��ο��� data��		
		this.type = type;
		this.from = null;
		this.to = null;
		this.data = data;
	}	

	public ChatData(ChatType type, String[] to, Object data) { //to���� data��		
		this.type = type;
		this.from = null;
		this.to = to;
		this.data = data;
	}
	
	public ChatData(ChatType type, String from, Object data) { //from�� ��ο��� data��
		this.type = type;
		this.from = from;
		this.to = null;
		this.data = data;
	}
	
	public ChatData(ChatType type, String from, String[] to, Object data) { //from�� to���� data��
		this.type = type;
		this.to = to;
		this.from = from;
		this.data = data;
	}
}
