package nchatbackup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ServerAction implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==ServerUI.btnSvrStart) {
			ServerUI.nSvr = new NChatSvr();
			ServerUI.nSvr.start();
			ServerUI.btnSvrStart.setText("Server Has Been Started!");
			ServerUI.btnSvrStart.setEnabled(false);
		}else if(e.getSource()==ServerUI.btnBroadcast) {
			try {
				ServerUI.nSvr.send(ServerUI.fldBroadcast.getText());
				ServerUI.fldBroadcast.setText("");
				System.out.println("sending msg:" + ServerUI.fldBroadcast.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public ServerAction() {
	
	}
	
}

