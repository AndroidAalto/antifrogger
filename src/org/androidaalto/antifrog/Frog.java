package org.androidaalto.antifrog;

public class Frog {

	private int x;
	private int y;
	private int leapLenght;
	
	public Frog(int x, int y, int leapLenght) {
		this.x = x;
		this.y = y;
		this.leapLenght = leapLenght;
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
	public int getLeapLenght() {
		return leapLenght;
	}
	public void setLeapLenght(int leapLenght) {
		this.leapLenght = leapLenght;
	}

	
}
