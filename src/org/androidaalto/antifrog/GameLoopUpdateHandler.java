package org.androidaalto.antifrog;

import org.andengine.engine.handler.IUpdateHandler;

public class GameLoopUpdateHandler implements IUpdateHandler{
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		((AntiFrogScene)AntiFrogActivity.getSharedInstance().mCurrentScene).moveCar();
		((AntiFrogScene)AntiFrogActivity.getSharedInstance().mCurrentScene).cleaner();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
