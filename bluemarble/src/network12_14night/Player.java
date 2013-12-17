package network12_14night;


public class Player {
	public int x, y, thisXY, money, playerCerruntPosition, userDics;  
	// �÷��̾��� ĳ������ cerruntPosition ������ġ�� �迭��ȣ �ֻ��� ������.
	// x, y, ����� �迭�� ��(math.random����? 
	// ���Ŀ��� �ֻ����� ���ȴ��� �ƴ����� Server�� .. ), �÷��̾��� ������. 
	public int userTurn;
	enum status {
		WAIT, RUN, BROKE, DOUBLE;   
		// �÷��̾ ���� �����̰� �ִ���, ��� ��������, �÷��� �Ұ� �������� �Ǵ�, �ֻ����������� ���� �̺�Ʈ �Ǵܰ�.
	};
	public String name;  // ���� ������ ���� �˾ƾ� �Ǵ� DB���� �α��ΰ��� �������� ������ ���°� ���� ������?
	private Boolean host; // �÷��̾ ������ ȣ��Ʈ���� �ƴ����� �Ǵ��ϴ� ��.
	
	public Player(String name, Boolean host) {
		this.userTurn = 1;  // ���� 1 2 3 4
		//status. = status.WAIT;
		this.money = 100000;  // �⺻ ����
		this.thisXY = 0;  // ���̺���� ĭ(���� �ִ� ����?�� ��ġ)
		this.x = 530;  // �����ӻ��� ���� ��ġ
		this.y = 570;  // �����ӻ��� ���밪 ��
		this.name = name;  // �����̸�
		this.host = host;  // ������ ���� host���� �ƴ����� ��. 
		// ���� ������ �迭 1 2 3 4 ������ �����Ѵ�.
		this.playerCerruntPosition = 0;
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

	public int getPlayerCerruntPosition() {
		return playerCerruntPosition;
	}

	public void setPlayerCerruntPosition(int playerCerruntPosition) {
		this.playerCerruntPosition = playerCerruntPosition;
	}

	public int getUserDics() {
		return userDics;
	}

	public void setUserDics(int userDics) {
		this.userDics = userDics;
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

	@Override
	public String toString(){ 
		return "x, y, thisXY, money, playerCerruntPosition, userDics : \n" + x+" , "+y+" , "+thisXY+" , "+money+" , "+playerCerruntPosition+" , "+userDics+"\n"+
				"userTurn : " + userTurn +"\n"+
				//"status(WAIT, RUN, BROKE, DOUBLE) : "+name+"\n"+ 
				"name : "+name +"\n"+
				"host : "+host+""
				;
	} 

} // end Player class

 
