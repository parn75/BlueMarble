package view;

import java.awt.image.BufferStrategy;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class BMFrame extends JFrame{
	final static int XSIZE = 1800;   //������ ũ��
	final static int YSIZE = 1050;	
	private MenuPane menu;
	JButton startB = new JButton("����");
	BufferStrategy bs;
	BMBoard board;


	
	private void start() {		
	}

	private void init() {		
		menu = new MenuPane();
		this.setJMenuBar(menu);
		
		this.add(startB,"South");
		
		board = new BMBoard();
		this.add(board, "Center");		
		
	}

	public BMFrame(String title) {
		super(title);		
		this.setSize(XSIZE, YSIZE);
		
		init();
		start();
		
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		board.setVisible(true);
		
		board.createBufferStrategy(2);
		bs = board.getBufferStrategy();
		board.setBufferStrategy(bs);
		
		board.updateBoard();
	}
	
	public static void main(String[] args) {
		new BMFrame("Test");
	}

}

class MenuPane extends JMenuBar {
	private JMenu ConnectM;
	private JMenuItem startHostM,connectM;

	public MenuPane(){
		ConnectM = new JMenu("����");
		startHostM = new JMenuItem("ȣ��Ʈ �����ϱ�");
		connectM = new JMenuItem("�����ϱ�");		
		ConnectM.add(startHostM);
		ConnectM.add(connectM);	
		this.add(ConnectM);
	}
	
	public JMenuItem getStartHostM() { return startHostM; }
	public JMenuItem getConnectM() { return connectM; }

}



