package backup11272;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class ClientAction implements ActionListener{
	JFrame clientUI;
	
	public ClientAction(JFrame frame) {
		this.clientUI = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==ClientUI.btnConnect) {
			ClientUI.client = new Client();
			ClientUI.client.start();
			ClientUI.client.send("*****[ID:"+ClientUI.fldID.getText()+"]*****");
			ClientUI.fldID.setEnabled(false);
			ClientUI.btnConnect.setEnabled(false);
		}else if(e.getSource()==ClientUI.btnPersonalMsg) {
			List<String> l = ClientUI.lstConnector.getSelectedValuesList();
			String[] str = new String[l.size()];
			l.toArray(str);
			String msg = ClientUI.fldChat.getText();			
			ClientUI.client.send(new ChatData(str,msg));
			if(l.size()>0) {
				ClientUI.txtChat.append("to ");			
				for(int i=0;i<str.length;i++) {
					ClientUI.txtChat.append(str[i] + ","); //to StrÃß°¡
				}			
				ClientUI.txtChat.append(":"+ msg + "\n");
			}
			ClientUI.fldChat.setText("");
		}else if(e.getSource() == ClientUI.photoMenuItem) {
			FileDialog fd = new FileDialog(clientUI, "Select Image", FileDialog.LOAD);
			URL url = null;
		    fd.setVisible(true);
		    String filename = fd.getDirectory()+fd.getFile();
		    System.out.println(filename);
		    File f = new File(filename);
		    try {
				url = f.toURI().toURL();
			} catch (MalformedURLException exception) {			
				exception.printStackTrace();
			}
		    ImageIcon icon = new ImageIcon(url);
		    ClientUI.client.send(icon);
		}
		
	}

}
