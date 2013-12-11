package newNetwork;

import java.io.Serializable;
import java.util.Set;
import java.util.TimerTask;

//주기적으로 접속자 리스트를 클라이언트에 뿌리기 위한 스레드
class SendConnectorList extends TimerTask {
	Server server;
	ServerUI serverUI;
	
	public SendConnectorList(Server server, ServerUI serverUI) {
		this.server = server;
		this.serverUI = serverUI;
	}
	
	@Override
	public void run() {
		String[] to= new String[serverUI.listModel.size()];
		serverUI.listModel.copyInto(to);
		Set<String> s = server.clientMap.keySet();
		String[] clist = new String[s.size()]; 
		s.toArray(clist);
		ChatData cd = new ChatData(ChatType.ConnectorList, to);
		for(int i=0;i<clist.length;i++) {
			synchronized(server.clientMap) {
				if(server.clientMap.get(clist[i])!= null) server.clientMap.get(clist[i]).send(cd);		
			}
		}
	}
}

class SendRoomStatus extends TimerTask {
	Server server;
	ServerUI serverUI;
	
	public SendRoomStatus(Server server, ServerUI serverUI) {
		this.server = server;
		this.serverUI = serverUI;
	}
	
	@Override
	public void run() {
		RoomStatus rs = new RoomStatus();
		for(int i=0;i<12;i++) {
			rs.roomTitle[i] = serverUI.roomList.roomList[i].getTitle();
			rs.playerNum[i] = serverUI.roomList.roomList[i].getPlayerNum();
			rs.isStarted[i] = serverUI.roomList.roomList[i].isStarted();
		}
		ChatData cd = new ChatData(ChatType.RoomStatus, rs);
		if(server.clientMap.size()>0) {
			server.broadcast(cd);		
		}
	}
}

class RoomStatus implements Serializable{
	String[] roomTitle = new String[12];
	int[] playerNum = new int[12];
	boolean[] isStarted = new boolean[12];
}

class SendWaitingRoomStatus extends TimerTask {
	Server server;
	ServerUI serverUI;
	
	public SendWaitingRoomStatus(Server server, ServerUI serverUI) {
		this.server = server;
		this.serverUI = serverUI;
	}
	
	@Override
	public void run() {
		
		WaitingRoomStatus[] ws = new WaitingRoomStatus[12];
		for(int i=0;i<12;i++) {
			ws[i] = new WaitingRoomStatus();
			ws[i].playerNames = serverUI.roomList.roomList[i].getPlayerNames().clone();
			ws[i].host = ws[i].playerNames[0];
			ws[i].title = serverUI.roomList.roomList[i].getTitle();
		}
		ChatData cd = new ChatData(ChatType.WaitingRoomStatus, ws);
		
		String[] to= new String[serverUI.listModel.size()];
		serverUI.listModel.copyInto(to);
		Set<String> s = server.clientMap.keySet();
		String[] clist = new String[s.size()]; 
		s.toArray(clist);		
		for(int i=0;i<clist.length;i++) {
			if(server.clientMap.get(clist[i])!= null) server.clientMap.get(clist[i]).send(cd);		
		}
		
	}
}

class WaitingRoomStatus implements Serializable {
	String[] playerNames;
	String host;
	String title;	
}
