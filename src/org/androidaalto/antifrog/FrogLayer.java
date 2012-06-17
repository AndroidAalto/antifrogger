package org.androidaalto.antifrog;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveXModifier;

public class FrogLayer extends Entity {
	private LinkedList<Frog> frogs;
	public static FrogLayer instance;
	public int frogCount;
	int posX;
	int posY;
	
	public static FrogLayer getSharedInstance() {
		return instance;
	}

	public static boolean isEmpty() {
		if (instance.frogs.size() == 0)
			return true;
		return false;
	}

	public static Iterator<Frog> getIterator() {
		return instance.frogs.iterator();
	}

	public void purge() {
		detachChildren();
		for (Frog e : frogs) {
			FrogPool.sharedEnemyPool().recyclePoolItem(e);
		}
		frogs.clear();
	}

	public FrogLayer(int x) {
		frogs = new LinkedList<Frog>();
		instance = this;
		frogCount = x;
	}

	public void restart() {
		frogs.clear();
		clearEntityModifiers();
		clearUpdateHandlers();
				
		for (int i = 0; i < frogCount; i++) {
			Frog e = FrogPool.sharedEnemyPool().obtainPoolItem();

			Random r = new Random();
			posX = (i*100) - r.nextInt(100);
			posY =  r.nextInt(Math.round(AntiFrogActivity.displayHeight-e.sprite.getWidth() * 3));
			e.sprite.setPosition(posX, posY);
			e.sprite.setVisible(true);
			e.sprite.setRotation(-90);
			attachChild(e.sprite);

			frogs.add(e);
		}
		setVisible(true);
		setPosition(-frogCount*100, 0);

		MoveXModifier movRight = new MoveXModifier(Math.round(frogCount*1.3), -frogCount*100, (frogCount*100)+450);

		registerEntityModifier(movRight);
	}

	public static void purgeAndRestart() {
		instance.purge();
		instance.restart();
	}

	@Override
	public void onDetached() {
		purge();
		clearUpdateHandlers();
		super.onDetached();
	}

}