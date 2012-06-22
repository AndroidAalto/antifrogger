package org.androidaalto.antifrog;

import org.andengine.entity.sprite.Sprite;

public class Frog {
	public Sprite sprite;

	public Frog() {
		sprite = new Sprite(AntiFrogActivity.displayWidth-330, AntiFrogActivity.displayHeight/2-90, 
				AntiFrogActivity.getSharedInstance().mFrogger, AntiFrogActivity.getSharedInstance().getVertexBufferObjectManager());
		init();
	}

	public void init() {
		
	}

	public void clean() {
		sprite.clearEntityModifiers();
		sprite.clearUpdateHandlers();
	}

	public boolean gotHit() {
		return false;
	}
}
