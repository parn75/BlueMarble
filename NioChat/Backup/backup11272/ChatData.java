package backup11272;

import java.io.Serializable;

public class ChatData implements Serializable{
	public String[] to;
	public String data;
	
	public ChatData(String[] to, String data) {
		this.to = to;
		this.data = data;
	}
}
