package bacnkup1129;

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
		String[] str= new String[serverUI.listModel.size()];
		serverUI.listModel.copyInto(str);
		Set<String> s = server.clientMap.keySet();
		String[] clist = new String[s.size()]; 
		s.toArray(clist);
		for(int i=0;i<clist.length;i++) {
			server.clientMap.get(clist[i]).send(str);			
		}
	}
}
