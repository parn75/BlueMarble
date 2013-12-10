package nchat;

import static nchat.ServerUI.btnSvrStart;
import static nchat.ServerUI.fldBroadcast;
import static nchat.ServerUI.lstConnector;
import static nchat.ServerUI.nSvr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class ServerAction implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==ServerUI.btnSvrStart) {
			nSvr = new NChatSvr();
			nSvr.start();
			btnSvrStart.setText("Server Has Been Started!");
			btnSvrStart.setEnabled(false);
		}else if(e.getSource()==ServerUI.btnBroadcast) {
			try {
				nSvr.broadcast("[Broadcast] : " + fldBroadcast.getText());
				fldBroadcast.setText("");
				System.out.println("sending msg:" + fldBroadcast.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}else if(e.getSource()==ServerUI.btnPersonalMsg) {
			try {
				List<String> selected = lstConnector.getSelectedValuesList();
				nSvr.send(selected, fldBroadcast.getText());
				fldBroadcast.setText("");
				System.out.println("sending msg:" + fldBroadcast.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public ServerAction() {
	
	}
	
}

