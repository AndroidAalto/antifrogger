package org.androidaalto.antifrog;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;

public class Car {
	public AnimatedSprite sprite;
	public static Car instance;
	Camera mCamera;
	boolean moveable;

	public static Car getSharedInstance() {
		if (instance == null)
			instance = new Car();
		return instance;
	}

	private Car() {
		final AnimatedSprite player = new AnimatedSprite(AntiFrogActivity.displayWidth-110, AntiFrogActivity.displayHeight/2-75, AntiFrogActivity.getSharedInstance().mCarTextureRegion, AntiFrogActivity.getSharedInstance().getVertexBufferObjectManager());
		player.setRotation(-90);
		player.animate(new long[]{200, 200}, 0, 1, true);
		
		sprite = player;
		
		mCamera = AntiFrogActivity.getSharedInstance().mCamera;
		//sprite.setPosition(mCamera.getWidth() / 2 - sprite.getWidth() / 2, mCamera.getHeight() - sprite.getHeight() - 10);

		moveable = true;
		instance = this;
	}

	public void moveCar(float accelerometerSpeedY) {
		if (!moveable)
			return;
		
		if (accelerometerSpeedY != 0) {

			int lL = 0;
			int rL = (int) (mCamera.getHeight() - (int) sprite.getHeight());

			float newY;

			// Calculate New X,Y Coordinates within Limits
			if (sprite.getY() >= lL)
				newY = sprite.getY() + accelerometerSpeedY;
			else
				newY = lL;
			if (newY <= rL)
				newY = sprite.getY() + accelerometerSpeedY;
			else
				newY = rL;

			// Double Check That New X,Y Coordinates are within Limits
			if (newY < lL)
				newY = lL;
			else if (newY > rL)
				newY = rL;

			sprite.setPosition(sprite.getX(), newY);
		}
	}
}