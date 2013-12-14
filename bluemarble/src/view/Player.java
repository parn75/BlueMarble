package view;


public class Player {
	public int x, y, thisXY, money;  // 플레이어의 캐릭터의 현제위치 x, y, 진행된 배열의 값(math.random형태? // 차후에는 주사위를 굴렸는지 아닌지를 Server로 .. ), 플레이어의 소지금. 
	public int userTurn;
	public enum status {
		WAIT, RUN, BROKE, DOUBLE;   // 플레이어가 현제 움직이고 있는지, 대기 상태인지, 플레이 불가 상태인지 판단, 주사위더블인지 등의 이벤트 판단값.
	};
	public String name;  // 내가 누군지 먼저 알아야 되니 DB에서 로그인값을 바탕으로 가지고 오는게 맞지 않을까?
	private Boolean host; // 플레이어가 게임의 호스트인지 아닌지를 판단하는 값.
	
	public Player(String name, Boolean host) {
		this.userTurn = 1;  // 유저 1 2 3 4
		this.money = 100000;  // 기본 소지
		this.thisXY = 0;  // 테이블상의 칸(현제 있는 도시?의 위치)
		this.x = 600;  // 프레임상의 절값 위치
		this.y = 380;  // 프레임상의 절대값 위
		this.name = name;  // 유저이
		this.host = host;  // 유저가 방의 host인지 아닌지의 값. 
		// 턴의 순서는 배열 1 2 3 4 순서로 시작한다.
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

 
