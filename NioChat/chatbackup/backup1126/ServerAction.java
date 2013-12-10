package backup1126;

import static backup1126.ServerUI.btnSvrStart;
import static backup1126.ServerUI.fldBroadcast;
import static backup1126.ServerUI.lstConnector;
import static backup1126.ServerUI.nSvr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ServerAction implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==ServerUI.btnSvrStart) {
			nSvr = new Server();
			nSvr.start();
			btnSvrStart.setText("Server Has Been Started!");
			btnSvrStart.setEnabled(false);
		}else if(e.getSource()==ServerUI.btnBroadcast) {
			nSvr.sendObj("[Broadcast] : " + fldBroadcast.getText());
			fldBroadcast.setText("");		
		}else if(e.getSource()==ServerUI.btnPersonalMsg) {
			List<String> selected = lstConnector.getSelectedValuesList();
			nSvr.sendObj(fldBroadcast.getText());
			fldBroadcast.setText("");		
		}
	}
	
	public ServerAction() {
	
	}
	
}

