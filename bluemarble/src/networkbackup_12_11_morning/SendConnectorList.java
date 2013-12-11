package networkbackup_12_11_morning;

import java.util.Set;
import java.util.TimerTask;

//�ֱ������� ������ ����Ʈ�� Ŭ���̾�Ʈ�� �Ѹ��� ���� ������
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
			server.clientMap.get(clist[i]).send(cd);			
		}
	}
}
