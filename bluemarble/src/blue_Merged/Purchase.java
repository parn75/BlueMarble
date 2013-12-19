package blue_Merged;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

class Purchase extends JPanel {
// �ǹ�����
	City c;
	private JRadioButton b1, b2, b3, b4;
	private Boolean[] buyType = { false, false, false, false };
	private JTextArea lb = new JTextArea();
	private int totPrice;
	String cityName=null;
	int price=0;
	public Purchase() {
		setSize(150, 200);
		// ���� ��ư �ʱ�ȭ
		b1 = new JRadioButton("��");
		b2 = new JRadioButton("�ܵ�");
		b3 = new JRadioButton("����");
		b4 = new JRadioButton("ȣ��"); 
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(4, 1));
		centerPanel.add(b1);
		centerPanel.add(b2);
		centerPanel.add(b3);
		centerPanel.add(b4);
		JButton btn = new JButton("����");
		lb.setText("�����̸� " + cityName + "\n�ǹ� ���� :" + price);

		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Boolean check = false;
				for (int i = 0; i < 4; i++) {
					if (buyType[i] == true)
						check = true;
				}
				if (TablePanel.control.playerData.get(TablePanel.control.getIAM()).money >= totPrice
						&& c.getCollecterName() == null && check == true) {
					Boolean[] bListIn = { false, false, false, false };
			
					int build = 0;
					for (int i = 0; i < buyType.length; i++) {
						if (buyType[i] == true) {
							bListIn[i] = true;
							build++;
			
						}
					}
			
					TablePanel.buildingList.add(new Building(TablePanel.control.playerData.
							get(TablePanel.control.getIAM()).thisXY,
							build));
					c.setBuildList(bListIn);
					c.setCollecterName(TablePanel.control.playerData.get(TablePanel.control.getIAM()).name);
					System.out.println(TablePanel.control.playerData.get(TablePanel.control.getIAM()).thisXY);
					
					TablePanel.control.playerData.get(TablePanel.control.getIAM()).money -= totPrice;
					SoundEffect.CONSTRUCT.play();
				
				} else if (TablePanel.control.playerData.get(TablePanel.control.getIAM()).money < totPrice){
					JOptionPane.showMessageDialog(getParent(), "���� �����մϴ�.",
							"�� ����.", JOptionPane.WARNING_MESSAGE);
				 TablePanel.control.turnOver();
			}else
					JOptionPane.showMessageDialog(getParent(), "�Ǽ��� ���� �����Ͻñ�",
							"�ϳ��� üũ�ؾ��Ѵ�.", JOptionPane.WARNING_MESSAGE);
				 TablePanel.control.turnOver();
				TablePanel.control.GAME_CONTROL(100);
				TablePanel.trunOver = false;
				TablePanel.f=false;
				setVisible(false);
			}

		});
		add("North", lb);
		add("Center", centerPanel);
		add("South", btn);
	
		b1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (b1.isSelected()) {
					if (buyType[0] == true) { 

						System.out.println("---");
						totPrice -= c.getPrice()[0];
					} else if (buyType[0] == false) {
						buyType[0] = true;
						totPrice += c.getPrice()[0];
					}
					System.out.println(buyType[0]);
				} else if (b1.isSelected() == false) {
					buyType[0] = false;
					totPrice -= c.getPrice()[0];
				}
				lb.setText("�����̸� " + c.getCityName() + "\n�ǹ� ���� :" + totPrice);
			}
		});
		b2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (b2.isSelected()) {
					if (buyType[1] == false) {
						buyType[1] = true;
						totPrice += c.getPrice()[1];
					}
				} else if (b2.isSelected() == false) {
					buyType[1] = false;
					totPrice -= c.getPrice()[1];
				}
				lb.setText("�����̸� " + c.getCityName() + "\n�ǹ� ���� :" + totPrice);
			}
		});
		b3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (b3.isSelected()) {
					if (buyType[2] == false) {
						buyType[2] = true;
						totPrice += c.getPrice()[2];
					}
				} else if (b3.isSelected() == false) {
					buyType[2] = false;
					totPrice -= c.getPrice()[2];
				}
				lb.setText("�����̸� " + c.getCityName() + "\n�ǹ� ���� :" + totPrice);
			}
		});
		b4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (b4.isSelected()) {
					if (buyType[3] == false) {
						buyType[3] = true;
						totPrice += c.getPrice()[3];
					}

				} else if (b4.isSelected() == false) {
					buyType[3] = false;
					totPrice -= c.getPrice()[3];
				}
				lb.setText("�����̸� " + c.getCityName() + "\n�ǹ� ���� :" + totPrice);
			}
		});
	}
	
	public void setCityData(City c,String cityName , int price){
		this.c=c;
		this.cityName=cityName;
		this.price=price;
		lb.setText("�����̸� " + cityName + "\n�ǹ� ���� :" + price);
	}

}// ���� Ŭ������
