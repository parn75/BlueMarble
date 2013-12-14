package view;


public class Player {
	public int x, y, thisXY, money;  // �÷��̾��� ĳ������ ������ġ x, y, ����� �迭�� ��(math.random����? // ���Ŀ��� �ֻ����� ���ȴ��� �ƴ����� Server�� .. ), �÷��̾��� ������. 
	public int userTurn;
	public enum status {
		WAIT, RUN, BROKE, DOUBLE;   // �÷��̾ ���� �����̰� �ִ���, ��� ��������, �÷��� �Ұ� �������� �Ǵ�, �ֻ����������� ���� �̺�Ʈ �Ǵܰ�.
	};
	public String name;  // ���� ������ ���� �˾ƾ� �Ǵ� DB���� �α��ΰ��� �������� ������ ���°� ���� ������?
	private Boolean host; // �÷��̾ ������ ȣ��Ʈ���� �ƴ����� �Ǵ��ϴ� ��.
	
	public Player(String name, Boolean host) {
		this.userTurn = 1;  // ���� 1 2 3 4
		this.money = 100000;  // �⺻ ����
		this.thisXY = 0;  // ���̺���� ĭ(���� �ִ� ����?�� ��ġ)
		this.x = 600;  // �����ӻ��� ���� ��ġ
		this.y = 380;  // �����ӻ��� ���밪 ��
		this.name = name;  // ������
		this.host = host;  // ������ ���� host���� �ƴ����� ��. 
		// ���� ������ �迭 1 2 3 4 ������ �����Ѵ�.
	}


	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getThisXY() {
		return thisXY;
	}
	public void setThisXY(int thisXY) {
		this.thisXY = thisXY;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getUserTurn() {
		return userTurn;
	}
	
	public void setUserTurn(int userTurn) {
		this.userTurn = userTurn;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getHost() {
		return host;
	}
	public void setHost(Boolean host) {
		this.host = host;
	}

}

 
