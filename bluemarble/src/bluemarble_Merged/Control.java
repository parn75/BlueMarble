package bluemarble_Merged;
// �ο��̰� �����Ѱ�~
import java.util.HashMap;

public class Control {
	public HashMap<String, City> cityMap = new HashMap<String, City>();  // ������ ��
	public HashMap<String, Player> playerData = new HashMap<String, Player>();  // �÷��̾�����.<Ű, ��ü>
	public int turnMax=0;  //  ��ü �÷��̾��� �� (�÷��̾��� �ִ밪�̹Ƿ� ������ ���ڰ� ���ƾ� ������ ���� ��)
	private String[] cityNameList = new String[32]; // ��Ƽ�� ����
	private String[] playerSequenceList = new String[32];  // �÷��̾��� ���� �����Ȳ
	private int firstDics, secondDics;  // �ֻ��� 1 2 
	private int playerCP;  // �÷��̾��� ���� ��ġ
	private int turnCP = 0;  // ������ �������� ��Ÿ���� ��.
	private int gameTurnCP = 0;  // ���簡 ���°�� �������� ��.
	private String IAM;  // Ŭ���̾�Ʈ�� ������ ������?
	private String[] nickName = new String[4];  // ������ ����� ���
	public boolean event = false;
	public WaitingRoomUI waitingRoom;
	
	public Control() {
		tableInit();  // ������� Ȯ�� ȣ��
		//playerAdd();  // ������� Ȯ�� ȣ��
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
			// �ֻ��� ������ , ������ ���� ��ġ�� ���� �� �迭�� ���� �� ��� Ȯ��.
			// �÷��̾�1 �κи� �׽�Ʈ. 2 3 4�� �г����� �Ѱܹ޾Ƽ� ó�� or Com.
			System.out.println(nickName[turnCP]+"�÷��̾��� ���� ��ġ : " + playerData.get(nickName[turnCP]).getPlayerCerruntPosition());
			if(getIAM().equals(getNickName()[getTurnCP()])){
				
			firstDics = this.random(6);   // ������ ����ϸ� �� �ֻ����� ���ÿ� ����������..
			secondDics = this.random(6);  // �̰� �������� ������ ����� �� �ƴѵ�.
			playerData.get(nickName[turnCP]).setUserDics(firstDics + secondDics);
			}
			System.out.println("�ֻ��� 1 : " + firstDics + 
							   "\n�ֻ��� 2 : " + secondDics+ 
							   "\n�ֻ��� �� : " + playerData.get(nickName[turnCP]).getUserDics());
			
			
			playerData.get(nickName[turnCP]).setPlayerCerruntPosition(playerData.get(nickName[turnCP]).getPlayerCerruntPosition() + firstDics + secondDics);
			
/*			playTrunEvent();   // �÷��̾ �����°���� Ȯ����, �������� ����. 
			if(TablePanel.trunOver==true){  // ������ �ο� ������ ������ ���ܹ߻�..	
				turnCP++; 
			}else{ turnCP=0; 
			}  // end turnCP �������� ����. 
			System.out.println(nickName[turnCP]+" �ֻ��� ������ ��ġ : " + playerData.get(nickName[turnCP]).getPlayerCerruntPosition());
			System.out.println(gameTurnCP+" ����° ____"+nickName[turnCP]+"�÷��̾���  �� <<_________ \n\n");
*/			

			ChatData cd = new ChatData();
			cd.setType(ChatType.GameData);
			System.out.println("������ "+event);
			event=true;
			System.out.println("������ 2"+event);
			GameData gd= new GameData(cityMap, playerData,event);
			cd.setData(gd);
			System.out.println("�ҷ�"+gd.event);
			System.out.println(gd.playerData.toString());
			System.out.println(gd.playerData.toString());
			System.out.println("�������̽�:"+gd.playerData.get(IAM).userDics);
			 event=false; 
			 if(IAM.equals(nickName[turnCP]))
			 waitingRoom.client.send(cd);
				
			break;
		case 2: // ����� �ΰ� 
			System.out.println("����� ó����");
			tollhouse(nickName[turnCP]);
			break;
		case 3: // �ǹ� ����, �ǹ��� �μ�
			System.out.println("�ǹ� ����, �ǹ� �μ�");
			/*buildPrice(String nickName, String cityName,
					String collecterName, int playerMoney, int buileList,
					boolean buildBoolean)*/
			
			break;
		case 4: // 
			System.out.println("�̺�Ʈ �̱��� ����");
			break;
		case 5:
			System.out.println("���� �̺�Ʈ �߻� �̱��� ����");
			break;
		case 6:// Ŭ���̾�Ʈ -> ������ ����Ÿ ����.
			System.out.println("������ ����Ÿ ���� �޼ҵ� ȣ��");
		case 100:// ���� �÷��̾�� �� �ѱ��
			System.out.println("�� �ѱ��");
			 turnCP++;
			 if(turnCP!=1&&turnCP >= turnMax-1) turnCP = 0;
			System.out.println("�ϸ߽� "+turnMax);
			System.out.println("������ :"+nickName[turnCP]);
			break;
		}
	}

	public void tableInit() {
		// �⺻���� ��ȣ, �ǹ����� ���, �̸�, ����
		String[] cityName = {"�����","����","��ī��","����¡","����",
      "Ÿ������","�ι���","ī�̷�","���ε�","�߸�",
      "����","�Ͽ���","�õ��","���Ŀ��","����ī��",
      "����","�����ⱸ","������","Ǫ��","������",
      "���ڹ�","����ī��","�θ�","���׹�","���迩��",
      "Ÿ��Ƽ","�ĸ�","����ī��","����","����","����"};

		int[] priceInt = { 1000, 2000, 3000, 4000 };
		
		for (int i = 0; i < cityName.length; i++) {
			cityMap.put(cityName[i], new City(i + 1, priceInt, cityName[i]));
			cityNameList[i] = cityName[i];
		}
		
		// City HashMap ���� �ʱ� ��°�
		System.out.println("cityMap.size() " + cityMap.size());		
		for (int i =0; i<cityMap.size(); i++ ){
		System.out.println("cityName[i] : " + cityMap.get(cityName[i]).toString());
		}
	}	
		
	public void playerAdd() {
		// �÷��� ���۽� �÷��̾� ����. 
		//this.nickName = {"player1","player2","player3","player4"};
		// Ŭ���̾�� ���� -> ����� �߰� ���� �̱���. �⺻������ player1���� ���� �ִ� 4����.
		
		for (int i = 0; i < nickName.length; i++) {
			boolean host = false;
			//this.nickName = TablePanel.sb;
			if(i==0) host = true;
			if(!nickName[i].equals("None")){
			playerData.put(nickName[i], new Player(nickName[i], host));
			playerSequenceList[i] = nickName[i];
			System.out.println("�߰��� ��"+nickName[i]);
			turnMax++;
			}
		}
		
		// Player HashMap �Ժ� �ʱⰪ ���
		System.out.println("playerData.size() " + playerData.size());		
		for (int i =0; i<playerData.size(); i++ ){
		System.out.println("playerData[i] : " + playerData.get(nickName[i]).toString());
		}
	}

	public int random(int inputDicsNum) {
		// inputDics �߻����� �Է�
		return (int) (Math.random() * inputDicsNum) + 1;
	}

	public String currentUserName(){
		return nickName[turnCP];
	}
	public int tollhouse(String nickName) {
		// �ǹ������� �߻��� ����Ḧ �����ְ� �ƴ� �������� ������ �ݾ� ������ ��ȯ.

		int passPrice = 0;
		if (nickName.equals("�ƹ���")) {   // ���ŷ��� ���濹�� (13.12.17 // 12:06��)
			System.out.println("������ ������ �Դϴ� ����� 0");
		} else {
			// ����� ������ 150�� ����.
			passPrice = 150;
		}
		return passPrice;
	}

	public int buildPrice(String nickName, String cityName,
			String collecterName, int playerMoney, int buileList,
			boolean buildBoolean) {
		// ���� ���� Object(City)Ÿ������ ��ȯ����, �ǹ� �����߻� ���� �ʿ�.
		// buildPrice(�����̸�, ��Ƽ�̸�, ����������, �ǹ���������, �ǹ��������ÿ���)
		int result = 0;

		if (nickName == cityName) {
			System.out.println("City ������ �ǹ����� ����.");
			if (playerMoney >= 3000 && buileList == 3) {
				if (buildBoolean == true) {
					// �ǹ� �������� �Է�, city������ ������ ����.
					result = 3000;
					System.out.println("1��° �������� - ���强��");
				}
			} // �ǹ� ���� 3�ܰ�
			if (playerMoney >= 2000 && buileList == 2) {
				if (buildBoolean == true) {
					result = 2000;
					System.out.println("2��° �������� - ���强��");
				}
			} // �ǹ� ���� 2�ܰ�
			if (playerMoney >= 1000 && buileList == 1) {
				if (buildBoolean == true) {
					// �ǹ� �������� �Է�, city������ ������ ����.
					System.out.println("3��° �������� - ���强��");
				}
				result = 1000; // �ǹ� ���� ����� �ʱⰪ 1000������ ����.
			} // �ǹ� ���� 1�ܰ�

			// city ������ �ϰ���� ���� ����

		} else if (nickName == collecterName && buildBoolean == true) {
			if (buileList == 1) {
				result = 1500;
				// ������ �������� �̱���.
			}
			if (buileList == 2) {
				result = 3000;
				// ������ �������� �̱���.
			}
			if (buileList == 3) {
				result = 4500;
				// ������ �������� �̱���.
			}

			// City�����ְ� �ƴ� nickName�� �ش��ϴ� ���Է���.
			// �⺻ �ǹ� ���� ����� 1.5���� �ݾ��� ���Ա����� ����
			// �ǹ� �������� ���� �� ���Ա� �ջ� ����.
			// �ǹ����� �������� ���� �ʱⰪ�� 1.5��
		}
		return result;
	} // end buildPrice

	public void playTurnOff(String nickName) {
		// ���� �÷��̾� ���� ������ŷ
		// �÷��̾� �� ������ ������ ����Ÿ ����, ��������.
		System.out.println(nickName + "�� ���� ���� �Ǿ����ϴ�.");
	} // playTurnOff end
	
	public void playTrunEvent(){
		if(playerData.get(nickName[turnCP]).getPlayerCerruntPosition() > 32){
			playerData.get(nickName[turnCP]).setPlayerCerruntPosition(playerData.get(nickName[turnCP]).getPlayerCerruntPosition()-32);
			playerData.get(nickName[turnCP]).setUserTurn(playerData.get(nickName[turnCP]).getUserTurn() + 1);
			gameTurnCP++;  // ������ 32���� ������ �� �������� ������ ������ ��������? 
			System.out.println("�÷��̾� �� ���� �̺�Ʈ ����");
		}
	}
	
	public void setIAM(String myName){
		if(myName!=null) IAM = myName;  // ���� �÷������� �����̸�.
		System.out.println(myName);
	}
	
	public String getIAM(){
		return IAM;  // ���� �÷������� �����̸� get.
	}

	public void setConstUserName(String[] name){
		if(name[0] == null) nickName = name;  // ��ü �÷��̾� �̸�.
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

	public int getTurnCP() {  // ���� ������ �� 
		return turnCP;
	}

	public void setTurnCP(int turnCP) {
		this.turnCP = turnCP;
	} 
 
	public String[] getNickName() {
		//return this.nickName[turnCP];  // �������� ������ �г���
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
					System.out.println("������ ���̽�"+playerData.
							get(nickName[turnCP]).userDics);
				 synchronized (this) {
					
					
				event=gd.event;
				}
				System.out.println("werwer"+event);
				System.out.println("�������̽�"+gd.playerData.get(from).userDics);
				System.out.println("�ڽ� ���� ��ǥ "+gd.playerData.get(IAM).getThisXY());
			}
		}
		
	}


} // end class

