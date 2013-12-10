package nchat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientAction implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==ClientUI.btnConnect) {
			ClientUI.client = new NChatClient();
			ClientUI.fldID.setEnabled(false);
		}else if(e.getSource()==ClientUI.btnPersonalMsg) {
			try {
				ClientUI.client.send(ClientUI.fldChat.getText()+"\n");
				ClientUI.fldChat.setText("");
			} catch (IOException e1) {			
				e1.printStackTrace();
			}
		}		
		
	}

}
