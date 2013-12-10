package backup1128;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Timer;

public class ServerAction implements ActionListener{
	private ServerUI serverUI;
	Server server;
	
	private void btnSvrStartAction() { //���� ���� ��ư �׼� ó��
		server = new Server(serverUI);
		server.start();
		SendConnectorList scl = new SendConnectorList(server, serverUI);
		Timer timer = new Timer();
		timer.schedule(scl, 1000, 5000); //5�ʿ� �ѹ��� ������ ��� ����
		serverUI.btnSvrStart.setText("Server Has Been Started!");
		serverUI.btnSvrStart.setEnabled(false);
	}
	
	private void btnPersonalMsgAction() { //�ӼӸ� ��ư �׼� ó��
		List<String> l = serverUI.lstConnector.getSelectedValuesList();			
		String[] str = new String[l.size()];
		l.toArray(str);
		String msg = serverUI.fldChat.getText();
		if(l.contains("To All")) {  //To All�� ���õǾ� ������ ��ü �޼��� ���� �� ����
			server.broadcast("[Broadcast] : " + msg);
			return;
		} else server.send(str, msg, "Server: ");
		if(l.size()>0) {
			serverUI.txtChat.append("to ");	
			for(int i=0;i<str.length;i++) {
				serverUI.txtChat.append(str[i] + ","); //to Str�߰�, Ex: to client1, client2, ...
			}			
			serverUI.txtChat.append(":"+ msg + "\n");
		}
		serverUI.fldChat.setText("");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==serverUI.btnSvrStart) {
			btnSvrStartAction();
		}else if(e.getSource()==serverUI.btnBroadcast) {
			server.broadcast("[Broadcast] : " + serverUI.fldChat.getText());
			serverUI.fldChat.setText("");		
		}else if(e.getSource()==serverUI.btnPersonalMsg) {
			btnPersonalMsgAction();
		}
	}
	
	public ServerAction(ServerUI serverUI) {
		this.serverUI = serverUI;
	}
	
}

