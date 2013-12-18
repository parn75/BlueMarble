package bluemarble_Merged;
// 민욱이가 수정한거~
import java.util.HashMap;

public class Control {
	public HashMap<String, City> cityMap = new HashMap<String, City>();  // 도시의 맵
	public HashMap<String, Player> playerData = new HashMap<String, Player>();  // 플레이어정보.<키, 객체>
	public int turnMax=0;  //  전체 플레이어의 수 (플레이어의 최대값이므로 마지막 주자가 돌아야 한턴이 돌게 됨)
	private String[] cityNameList = new String[32]; // 시티의 정보
	private String[] playerSequenceList = new String[32];  // 플레이어의 현제 진행상황
	private int firstDics, secondDics;  // 주사위 1 2 
	private int playerCP;  // 플레이어의 현제 위치
	private int turnCP = 0;  // 누구의 턴인지를 나타내는 턴.
	private int gameTurnCP = 0;  // 현재가 몇번째의 턴인지의 값.
	private String IAM;  // 클라이언트의 주인이 누구냐?
	private String[] nickName = new String[4];  // 접속한 사람의 목록
	public boolean event = false;
	public WaitingRoomUI waitingRoom;
	
	public Control() {
		tableInit();  // 정상출력 확인 호출
		//playerAdd();  // 정상출력 확인 호출
		System.out.println("_-_-_-_-_-_ Control loding complete _-_-_-_-_-_-");
	}
	
	public void setWaitingRoom(WaitingRoomUI waitingRoom) {
		this.waitingRoom = waitingRoom;
	}

	public final void GAME_CONTROL(int controlNum) {
		switch (controlNum) {
		default:
		case 1:
			firstDics=0; 
			secondDics=0;
			// 주사위 굴리기 , 유저의 현제 위치를 도시 맵 배열에 대입 후 결과 확인.
			// 플레이어1 부분만 테스트. 2 3 4는 닉네임은 넘겨받아서 처리 or Com.
			System.out.println(nickName[turnCP]+"플레이어의 시작 위치 : " + playerData.get(nickName[turnCP]).getPlayerCerruntPosition());
			if(getIAM().equals(getNickName()[getTurnCP()])){
				
			firstDics = this.random(6);   // 쓰레드 사용하면 두 주사위가 동시에 구르겠지만..
			secondDics = this.random(6);  // 이거 굴리려고 쓰레스 사용은 좀 아닌듯.
			playerData.get(nickName[turnCP]).setUserDics(firstDics + secondDics);
			}
			System.out.println("주사위 1 : " + firstDics + 
							   "\n주사위 2 : " + secondDics+ 
							   "\n주사위 합 : " + playerData.get(nickName[turnCP]).getUserDics());
			
			
			playerData.get(nickName[turnCP]).setPlayerCerruntPosition(playerData.get(nickName[turnCP]).getPlayerCerruntPosition() + firstDics + secondDics);
			
/*			playTrunEvent();   // 플레이어가 몇바퀴째인지 확인후, 게임턴을 증가. 
			if(TablePanel.trunOver==true){  // 유저의 인원 수보다 많으면 예외발생..	
				turnCP++; 
			}else{ turnCP=0; 
			}  // end turnCP 증가로직 종료. 
			System.out.println(nickName[turnCP]+" 주사위 굴린후 위치 : " + playerData.get(nickName[turnCP]).getPlayerCerruntPosition());
			System.out.println(gameTurnCP+" 바퀴째 ____"+nickName[turnCP]+"플레이어의  턴 <<_________ \n\n");
*/			

			ChatData cd = new ChatData();
			cd.setType(ChatType.GameData);
			System.out.println("보낼때 "+event);
			event=true;
			System.out.println("보낼때 2"+event);
			GameData gd= new GameData(cityMap, playerData,event);
			cd.setData(gd);
			System.out.println("불렌"+gd.event);
			System.out.println(gd.playerData.toString());
			System.out.println(gd.playerData.toString());
			System.out.println("보낼다이스:"+gd.playerData.get(IAM).userDics);
			 event=false; 
			 if(IAM.equals(nickName[turnCP]))
			 waitingRoom.client.send(cd);
				
			break;
		case 2: // 통행료 부과 
			System.out.println("통행료 처리중");
			tollhouse(nickName[turnCP]);
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
			 turnCP++;
			 if(turnCP!=1&&turnCP >= turnMax-1) turnCP = 0;
			System.out.println("턴멕스 "+turnMax);
			System.out.println("현제턴 :"+nickName[turnCP]);
			break;
		}
	}

	public void tableInit() {
		// 기본도시 번호, 건물생성 비용, 이름, 생성
		String[] cityName = {"출발지","방콕","마카오","베이징","독도",
      "타이페이","두바이","카이로","무인도","발리",
      "도쿄","하와이","시드니","상파울로","찬스카드",
      "퀘벡","복지기구","프라하","푸켓","베를린",
      "모스코바","찬스카드","로마","제네바","세계여행",
      "타이티","파리","찬스카드","런던","서울","뉴욕"};

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
		//this.nickName = {"player1","player2","player3","player4"};
		// 클라이언드 대기실 -> 입장시 추가 로직 미구현. 기본값으로 player1부터 시작 최대 4까지.
		
		for (int i = 0; i < nickName.length; i++) {
			boolean host = false;
			//this.nickName = TablePanel.sb;
			if(i==0) host = true;
			if(!nickName[i].equals("None")){
			playerData.put(nickName[i], new Player(nickName[i], host));
			playerSequenceList[i] = nickName[i];
			System.out.println("추가중 닉"+nickName[i]);
			turnMax++;
			}
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
		if (nickName.equals("아무게")) {   // 구매로직 변경예정 (13.12.17 // 12:06분)
			System.out.println("도시의 소유자 입니다 통행료 0");
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
		if(playerData.get(nickName[turnCP]).getPlayerCerruntPosition() > 32){
			playerData.get(nickName[turnCP]).setPlayerCerruntPosition(playerData.get(nickName[turnCP]).getPlayerCerruntPosition()-32);
			playerData.get(nickName[turnCP]).setUserTurn(playerData.get(nickName[turnCP]).getUserTurn() + 1);
			gameTurnCP++;  // 유저가 32개의 말판을 다 돌았으니 게임의 한턴이 지났겠지? 
			System.out.println("플레이어 턴 증가 이벤트 종료");
		}
	}
	
	public void setIAM(String myName){
		if(myName!=null) IAM = myName;  // 지금 플레이중인 유저이름.
		System.out.println(myName);
	}
	
	public String getIAM(){
		return IAM;  // 지금 플레이중인 유저이름 get.
	}

	public void setConstUserName(String[] name){
		if(name[0] == null) nickName = name;  // 전체 플레이어 이름.
		System.out.println(name[0]);
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
 
	public String[] getNickName() {
		//return this.nickName[turnCP];  // 현제턴인 유저의 닉네임
		return this.nickName;
	}

	public void setNickName(String[] nickName) {
		this.nickName = nickName;
	}

	public void recieveData(String from, Object data) {
		GameData gd = (GameData)data;
		for(int i=0; i<nickName.length;i++){
			if(nickName[i].equals(from)){
				turnCP=i; 
				 if(turnCP!=1&&turnCP >= turnMax) turnCP = 0;

					playerData.get(nickName[turnCP]).setUserDics(gd.playerData.
							get(nickName[turnCP]).userDics);
					System.out.println("도착한 다이스"+playerData.
							get(nickName[turnCP]).userDics);
				 synchronized (this) {
					
					
				event=gd.event;
				}
				System.out.println("werwer"+event);
				System.out.println("받은다이스"+gd.playerData.get(from).userDics);
				System.out.println("자신 현제 좌표 "+gd.playerData.get(IAM).getThisXY());
			}
		}
		
	}


} // end class

