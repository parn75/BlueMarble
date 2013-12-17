package bluemarble12_17;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

class Purchase extends JFrame {
// �ǹ�����
	City c;
	private JRadioButton b1, b2, b3, b4;
	private Boolean[] buyType = { false, false, false, false };
	private JTextArea lb = new JTextArea();
	private int totPrice;
	public Purchase(String cityName , int price) {
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
				if (TablePanel.ply.get(TablePanel.userNum).money >= totPrice
						&& c.getCollecterName() == null && check == true) {
					Boolean[] bListIn = { false, false, false, false };
			
					int build = 0;
					for (int i = 0; i < buyType.length; i++) {
						if (buyType[i] == true) {
							bListIn[i] = true;
							build++;
			
						}
					}
			
					TablePanel.buildingList.add(new Building(TablePanel.ply.get(TablePanel.userNum).thisXY,
							build));
					c.setBuildList(bListIn);
					c.setCollecterName(TablePanel.ply.get(TablePanel.userNum).name);
					System.out.println(TablePanel.ply.get(TablePanel.userNum).thisXY);
					
					TablePanel.ply.get(TablePanel.userNum).money -= totPrice;
				} else if (TablePanel.ply.get(TablePanel.userNum).money < totPrice)
					JOptionPane.showMessageDialog(getParent(), "���� �����մϴ�.",
							"�� ����.", JOptionPane.WARNING_MESSAGE);
				else
					JOptionPane.showMessageDialog(getParent(), "�Ǽ��� ���� �����Ͻñ�",
							"�ϳ��� üũ�ؾ��Ѵ�.", JOptionPane.WARNING_MESSAGE);
				System.out.println("����" + totPrice);
				System.out.println("��ȣ" + TablePanel.userNum);
				System.out.println(TablePanel.ply.get(TablePanel.userNum).name);
				System.out.println("������" + TablePanel.ply.get(TablePanel.userNum).money);
				System.out.println(TablePanel.cityMap.get(c.getCityName()).getCityName());
				//if (TablePanel.userNum < 1)
				//	TablePanel.userNum++;
				//else
					TablePanel.userNum = 0;
				TablePanel.trunOver = false;
				setVisible(false);
			}

		});
		add("North", lb);
		add("Center", centerPanel);
		add("South", btn);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				super.windowActivated(e);
				if (TablePanel.ply != null)
					c = (City) TablePanel.cityMap.get(
							TablePanel.cityNameList[TablePanel.ply.get(TablePanel.userNum).thisXY]);
				totPrice = c.getPrice()[0];
				for (int i = 0; i < 4; i++)
					buyType[i] = false;
				buyType[0] = true;
				b1.setSelected(true);
				b2.setSelected(false);
				b3.setSelected(false);
				b4.setSelected(false);
				repaint();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				super.windowClosing(e);
				setVisible(false);
				//if (TablePanel.userNum < 1)
				//	TablePanel.userNum++;
				//else
					TablePanel.userNum = 0;
				TablePanel.trunOver = false;

			}

		});
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

}// ���� Ŭ������
