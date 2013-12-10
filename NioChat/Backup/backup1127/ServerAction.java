package backup1127;

import static backup1127.ServerUI.btnSvrStart;
import static backup1127.ServerUI.fldBroadcast;
import static backup1127.ServerUI.nSvr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerAction implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==ServerUI.btnSvrStart) {
			nSvr = new Server();
			nSvr.start();
			btnSvrStart.setText("Server Has Been Started!");
			btnSvrStart.setEnabled(false);
		}else if(e.getSource()==ServerUI.btnBroadcast) {
			nSvr.send("[Broadcast] : " + fldBroadcast.getText());
			fldBroadcast.setText("");		
		}else if(e.getSource()==ServerUI.btnPersonalMsg) {
			String[] str= new String[20];
			ServerUI.listModel.copyInto(str);		
			if(str!=null) nSvr.send(str);
			System.out.println("listModel has been sent.");
			fldBroadcast.setText("");		
		}
	}
	
	public ServerAction() {
	
	}
	
}

