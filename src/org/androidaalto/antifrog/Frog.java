package org.androidaalto.antifrog;

import org.andengine.entity.sprite.AnimatedSprite;

public class Frog {

	private int x;
	private int y;
	private int leapLenght;
	private AnimatedSprite sprite;
	
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

	public AnimatedSprite getSprite() {
		return sprite;
	}

	public void setSprite(AnimatedSprite sprite) {
		this.sprite = sprite;
	}

	
}
