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
			timer.schedule(scl, 1000, 5000); //5초에 한번씩 접속자 명단 전송
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
			if(l.contains("To All")) {  //To All이 선택되어 있으면 전체 메세지
				nSvr.broadcast("[Broadcast] : " + msg);
				return;
			} else ServerUI.nSvr.send(str, msg, "Server: ");
			if(l.size()>0) {  //특정인을 선택하여 메세지를 전송하는 경우
				ServerUI.txtarLog.append("to ");	
				for(int i=0;i<str.length;i++) {
					ServerUI.txtarLog.append(str[i] + ","); //to Str추가
				}			
				ServerUI.txtarLog.append(":"+ msg + "\n");
			}
			ServerUI.fldBroadcast.setText("");
		}
	}
	
	public ServerAction() {
	
	}
	
}

