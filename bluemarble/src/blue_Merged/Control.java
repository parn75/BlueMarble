package blue_Merged;

import java.util.HashMap;

public class Control {
	public HashMap<String, City> cityMap = new HashMap<String, City>();
	public HashMap<String, Player> playerData = new HashMap<String, Player>();
	private String[] cityNameList = new String[32];
	private String[] playerSequenceList = new String[32];
	private int firstDics, secondDics;
	private int playerCP;
	private int turnCP = 0;
	private String IAM;
	private String[] nickName = new String[4];
	public Control() {
		tableInit();  // 정상출력 확인 호출
		playerAdd();  // 정상출력 확인 호출
		
	}

	public final void GAME_CONTROL(int controlNum) {
		switch (controlNum) {
		default:
		case 1:
			System.out.println("player1 현제 위치 : " + playerData.get("player1").getPlayerCerruntPosition());
			turnCP = playerData.get("player1").getUserTurn();
			firstDics=0; 
			secondDics=0;
			// 주사위 굴리기 , 유저의 현제 위치를 도시 맵 배열에 대입 후 결과 확인.
			// 플레이어1 부분만 테스트. 2 3 4는 닉네임은 넘겨받아서 처리 or Com.
			firstDics = this.random(6);   // 쓰레드 사용하면 두 주사위가 동시에 구르겠지만..
			secondDics = this.random(6);  // 이거 굴리려고 쓰레스 사용은 좀 아닌듯.
			
			playerData.get("player1").setUserDics(firstDics + secondDics);
			System.out.println("주사위 1 : " + firstDics + 
							   "\n주사위 2 : " + secondDics+ 
							   "\n주사위 합 : " + playerData.get("player1").getUserDics());
			playerData.get("player1").setPlayerCerruntPosition(playerData.get("player1").getPlayerCerruntPosition() + firstDics + secondDics);
			playTrunEvent();   // 플레이어 턴 체크. 
			System.out.println("player1 주사위 굴린후 위치 : " + playerData.get("player1").getPlayerCerruntPosition());
			System.out.println("____________" + turnCP + " 턴 ____________ \n\n");
			break;
		case 2: // 통행료 부과 
			System.out.println("통행료 처리중");
			tollhouse("player1");
			break;
		case 3: // 건물 짓기, 건물및 인수
			System.out.println("건물 구매, 건물 인수");
			/*buildPrice(String nickName, String cityName,
					String collecterName, int playerMoney, int buileList,
					boolean buildBoolean)*/
			
			break;
		case 4: // 
			System.out.println("이벤트 미구현 로직");
			break;
		case 5:
			System.out.println("랜덤 이벤트 발생 미구현 로직");
			break;
		case 6:// 클라이언트 -> 서버로 데이타 전송.
			System.out.println("서버로 데이타 전송 메소드 호출");
		case 100:// 다음 플레이어로 턴 넘기기
			System.out.println("턴 넘기기");
			if(turnCP < nickName.length && nickName[turnCP]!=null) turnCP++;
			else turnCP = 0;
			break;
		
		}
	}

	public void tableInit() {
		// 기본도시 번호, 건물생성 비용, 이름, 생성
		String[] cityName = {"출발지","방콕","마카오","베이징","독도","타이페이",
				"두바이","카이로","무인도","발리","도쿄","하와이","시드니","상파울로","찬스카드","퀘벡","복지기구","프라하","푸켓",
				"베를린","모스코바","찬스카드","로마","제네바","세계여행","타이티","파리","찬스카드","런던","서울","뉴욕"};
		int[] priceInt = { 1000, 2000, 3000, 4000 };
		
		for (int i = 0; i < cityName.length; i++) {
			cityMap.put(cityName[i], new City(i + 1, priceInt, cityName[i]));
			cityNameList[i] = cityName[i];
		}
		
		// City HashMap 개별 초기 출력값
		System.out.println("cityMap.size() " + cityMap.size());		
		for (int i =0; i<cityMap.size(); i++ ){
		System.out.println("cityName[i] : " + cityMap.get(cityName[i]).toString());
		}
	}	
	
	public void playerAdd() {
		// 플레이 시작시 플레이어 생성. 
		String[] nickName = {"player1","player2","player3","player4"};
		// 클라이언드 대기실 -> 입장시 추가 로직 미구현. 기본값으로 player1부터 시작 최대 4까지.
		
		for (int i = 0; i < nickName.length; i++) {
			boolean host = false;
			
			if(i==0) host = true;
			playerData.put(nickName[i], new Player(nickName[i], host));
			playerSequenceList[i] = nickName[i];
		}
		
		// Player HashMap 게별 초기값 출력
		System.out.println("playerData.size() " + playerData.size());		
		for (int i =0; i<playerData.size(); i++ ){
		System.out.println("playerData[i] : " + playerData.get(nickName[i]).toString());
		}
	}
	

	public int random(int inputDicsNum) {
		// inputDics 발생범위 입력
		return (int) (Math.random() * inputDicsNum) + 1;
	}

	public String currentUserName(){
		return nickName[turnCP];
	}
	public int tollhouse(String nickName) {
		// 건물생성시 발생된 통행료를 소유주가 아닌 유저에게 수거할 금액 생성후 반환.

		int passPrice = 0;
		if (nickName.equals("player1")) {
			System.out.println("현위치의 소유자 입니다 통행료 0");
		} else {
			// 통행료 당장은 150원 고정.
			passPrice = 150;
		}
		return passPrice;
	}

	public int buildPrice(String nickName, String cityName,
			String collecterName, int playerMoney, int buileList,
			boolean buildBoolean) {
		// 받을 값을 Object(City)타잎으로 전환예정, 건물 생성발생 로직 필요.
		// buildPrice(유저이름, 시티이름, 유저소지금, 건물생성레벨, 건물생성선택여부)
		int result = 0;

		if (nickName == cityName) {
			System.out.println("City 소유주 건물생성 시작.");
			if (playerMoney >= 3000 && buileList == 3) {
				if (buildBoolean == true) {
					// 건물 생성변수 입력, city소유주 소지금 차감.
					result = 3000;
					System.out.println("1번째 생성레벨 - 빌드성공");
				}
			} // 건물 생성 3단계
			if (playerMoney >= 2000 && buileList == 2) {
				if (buildBoolean == true) {
					result = 2000;
					System.out.println("2번째 생성레벨 - 빌드성공");
				}
			} // 건물 생성 2단계
			if (playerMoney >= 1000 && buileList == 1) {
				if (buildBoolean == true) {
					// 건물 생성변수 입력, city소유주 소지금 차감.
					System.out.println("3번째 생성레벨 - 빌드성공");
				}
				result = 1000; // 건물 생성 비용은 초기값 1000원으로 고정.
			} // 건물 생성 1단계

			// city 소유주 일경우의 로직 종료

		} else if (nickName == collecterName && buildBoolean == true) {
			if (buileList == 1) {
				result = 1500;
				// 소유전 이전로직 미구현.
			}
			if (buileList == 2) {
				result = 3000;
				// 소유전 이전로직 미구현.
			}
			if (buileList == 3) {
				result = 4500;
				// 소유전 이전로직 미구현.
			}

			// City소유주가 아닌 nickName에 해당하는 매입로직.
			// 기본 건물 짓기 비용의 1.5배의 금액을 매입금으로 리턴
			// 건물 생성로직 레벨 별 매입금 합산 로직.
			// 건물생성 최종빌드 기준 초기값의 1.5배
		}
		return result;
	} // end buildPrice

	public void playTurnOff(String nickName) {
		// 다음 플레이어 턴의 정보패킹
		// 플레이어 턴 종료후 턴종료 데이타 전송, 로직구현.
		System.out.println(nickName + "의 턴이 종료 되었습니다.");
	} // playTurnOff end
	
	public void playTrunEvent(){
		if(playerData.get("player1").getPlayerCerruntPosition() > 32){
			playerData.get("player1").setPlayerCerruntPosition(playerData.get("player1").getPlayerCerruntPosition()-32);
			playerData.get("player1").setUserTurn(playerData.get("player1").getUserTurn() + 1);
			System.out.println("플레이어 턴 증가 이벤트 종료");
		}
	}
	
	public void setIAM(String myName){
		if(myName!=null) IAM = myName;  // 지금 플레이중인 유저이름.
	}
	
	public String getIAM(){
		return IAM;  // 지금 플레이중인 유저이름 get.
	}

	public void setConstUserName(String[] name){
		if(nickName[0] == null) nickName = name;  // 전체 플레이어 이름.
	}

	public HashMap<String, City> getCityMap() {
		return cityMap;
	}

	public void setCityMap(HashMap<String, City> cityMap) {
		this.cityMap = cityMap;
	}

	public HashMap<String, Player> getPlayerData() {
		return playerData;
	}

	public void setPlayerData(HashMap<String, Player> playerData) {
		this.playerData = playerData;
	}

	public String[] getCityNameList() {
		return cityNameList;
	}

	public void setCityNameList(String[] cityNameList) {
		this.cityNameList = cityNameList;
	}

	public String[] getPlayerSequenceList() {
		return playerSequenceList;
	}

	public void setPlayerSequenceList(String[] playerSequenceList) {
		this.playerSequenceList = playerSequenceList;
	}

	public int getFirstDics() {
		return firstDics;
	}

	public void setFirstDics(int firstDics) {
		this.firstDics = firstDics;
	}

	public int getSecondDics() {
		return secondDics;
	}

	public void setSecondDics(int secondDics) {
		this.secondDics = secondDics;
	}

	public int getPlayerCP() {
		return playerCP;
	}

	public void setPlayerCP(int playerCP) {
		this.playerCP = playerCP;
	}

	public int getTurnCP() {  // 현제 유저의 턴 
		return turnCP;
	}

	public void setTurnCP(int turnCP) {
		this.turnCP = turnCP;
	}

	public String getNickName() {
		return this.nickName[turnCP];  // 현제턴인 유저의 닉네임
	}

	public void setNickName(String[] nickName) {
		this.nickName = nickName;
	}

} // end class
