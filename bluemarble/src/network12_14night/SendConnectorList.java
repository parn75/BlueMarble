package network12_14night;

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
	RoomListPanel roomList;
	
	public SendRoomStatus(Server server, ServerUI serverUI) {
		this.server = server;
		this.serverUI = serverUI;
		roomList = RoomListPanel.getInstance();
	}
	
	@Override
	public void run() {	
		
		WaitingRoomInfo[] wrs = roomList.cloneWholeRoomInfo(); 
		//WaitingRoomInfo[] wrs = roomList.getWholeRoomInfo(); //로는 안됨? why? -_-;; 미치미치미치
		/*
		for(int i=0;i<wrs.length;i++) {
			wrs[i] = new WaitingRoomInfo();
			wrs[i].setPlayerNames(roomList.roomList[i].roomInfo.getPlayerNames().clone());
			wrs[i].setPlayerNum(roomList.roomList[i].roomInfo.getPlayerNum());
			wrs[i].setRoomNum(roomList.roomList[i].roomInfo.getRoomNum());
			wrs[i].setRoomName(roomList.roomList[i].roomInfo.getRoomName());
			wrs[i].setIsStarted(roomList.roomList[i].roomInfo.getIsStarted());
		}		
		*/		
		//System.out.println(wrs[0].getRoomName().toString());
		ChatData cd = new ChatData(ChatType.RoomStatus, wrs);			
		if(server.clientMap.size()>0) {
			server.broadcast(cd);		
		}
		
	}
}
