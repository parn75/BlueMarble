package backup11272;

import static backup11272.ServerUI.btnSvrStart;
import static backup11272.ServerUI.fldBroadcast;
import static backup11272.ServerUI.nSvr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Timer;

public class ServerAction implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==ServerUI.btnSvrStart) {
			nSvr = new Server();
			nSvr.start();
			SendConnectorList scl = new SendConnectorList(nSvr);
			Timer timer = new Timer();
			timer.schedule(scl, 1000, 5000); //5�ʿ� �ѹ��� ������ ��� ����
			btnSvrStart.setText("Server Has Been Started!");
			btnSvrStart.setEnabled(false);
		}else if(e.getSource()==ServerUI.btnBroadcast) {
			nSvr.broadcast("[Broadcast] : " + fldBroadcast.getText());
			fldBroadcast.setText("");		
		}else if(e.getSource()==ServerUI.btnPersonalMsg) {
			List<String> l = ServerUI.lstConnector.getSelectedValuesList();			
			String[] str = new String[l.size()];
			l.toArray(str);
			String msg = ServerUI.fldBroadcast.getText();
			if(l.contains("To All")) {  //To All�� ���õǾ� ������ ��ü �޼���
				nSvr.broadcast("[Broadcast] : " + msg);
				return;
			} else ServerUI.nSvr.send(str, msg, "Server: ");
			if(l.size()>0) {  //Ư������ �����Ͽ� �޼����� �����ϴ� ���
				ServerUI.txtarLog.append("to ");	
				for(int i=0;i<str.length;i++) {
					ServerUI.txtarLog.append(str[i] + ","); //to Str�߰�
				}			
				ServerUI.txtarLog.append(":"+ msg + "\n");
			}
			ServerUI.fldBroadcast.setText("");
		}
	}
	
	public ServerAction() {
	
	}
	
}

