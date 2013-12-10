package backup1202_FileTransfer;

import java.io.Serializable;

public class ChatData implements Serializable{
	private static final long serialVersionUID = -1940449923475179341L;
	public String[] to;
	public Object data;
	
	public ChatData(String[] to, String data) {
		this.to = to;
		this.data = data;
	}
}
