package blue_Merged2;

import java.awt.*;
import java.awt.event.*;

public class TestView extends Frame implements ActionListener {

	private Button[] numButtons = null; // new NumButton[15];
	private Control control = new Control();
	
	public TestView() {

		String numStr[] = { "주사위", "통행료", "건물구매 인수", "다음턴"};

		numButtons = new Button[numStr.length];
		Panel numPanel = new Panel();
		
		for (int i = 0; i < numStr.length; i++) {
			numButtons[i] = new Button(numStr[i]);
			// numButtons[i].setForeground(Color.black);
			numPanel.add(numButtons[i]);
			numButtons[i].addActionListener(this);
		}

		// numPanel.setSize(300, 0);
		// textField. // backgroundColor(Color.CYAN);		add("North", textField);
		numPanel.setLayout(new GridLayout(7, 4, 5, 5)); // GridLayout(int
														// rows,int cols, int
														// hgap, int vgap)
		numPanel.setBackground(Color.lightGray);
		add("Center", numPanel);

		this.addWindowListener(new WindowAdapter() { // new Abstructor
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		setBounds(200, 200, 600, 400);
		setVisible(true);

	}

	// ActionListener Override
	public void actionPerformed(ActionEvent e) {
		// String s = e.getActionCommand();

		if (e.getSource() == numButtons[0]) {
			control.GAME_CONTROL(1);
			//textField.setText(playerData.get("player1").setPlayerCerruntPosition(firstDics + secondDics););
		} else if (e.getSource() == numButtons[1]) {
			control.GAME_CONTROL(2);
		} else if (e.getSource() == numButtons[2]) {
			control.GAME_CONTROL(3);
		} else if (e.getSource() == numButtons[3]) {
			control.GAME_CONTROL(100);
		} else if (e.getSource() == numButtons[4]) {
		}
/*		} else if (e.getSource() == this.numButtons[5]) {
			
		} else if (e.getSource() == this.numButtons[6]) {
			
		} else if (e.getSource() == this.numButtons[7]) {
			
		} else if (e.getSource() == this.numButtons[8]) {
			
		} else if (e.getSource() == this.numButtons[9]) {
			
		} else if (e.getSource() == this.numButtons[10]) {
			
		} else if (e.getSource() == this.numButtons[11]) {
			
		} else if (e.getSource() == this.numButtons[12]) {
			
		} else if (e.getSource() == this.numButtons[13]) {
			
		} else if (e.getSource() == this.numButtons[14]) {
		}
*/
	
	}
	public static void main(String args[]) {
		new TestView();
	}
}