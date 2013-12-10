package backup1126;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientAction implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==ClientUI.btnConnect) {
			ClientUI.client = new Client();
			ClientUI.client.start();
			ClientUI.fldID.setEnabled(false);
		}else if(e.getSource()==ClientUI.btnPersonalMsg) {
			ClientUI.client.send(ClientUI.fldChat.getText()+"\n");
			ClientUI.fldChat.setText("");
		}		
		
	}

}
