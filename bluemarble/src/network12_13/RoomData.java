package network12_13;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class RoomData {
	private BufferedImage roomImg;
	private int roomNumber;
	private ArrayList<String> players;
	private boolean gameStarted;
	private Color color;
	public RoomData(BufferedImage room, int roomNumber,
			ArrayList<String> players, boolean gameStarted, Color color) {	
		this.roomImg = room;
		this.roomNumber = roomNumber;
		this.players = players;
		this.gameStarted = gameStarted;
		this.color = color;
	}
	public BufferedImage getRoomImg() {
		return roomImg;
	}
	public void setRoomImg(BufferedImage room) {
		this.roomImg = room;
	}
	public int getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	public ArrayList<String> getPlayers() {
		return players;
	}
	public void setPlayers(ArrayList<String> players) {
		this.players = players;
	}
	public boolean isGameStarted() {
		return gameStarted;
	}
	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}	
	
}
