package org.androidaalto.antifrog;

import org.andengine.util.adt.pool.GenericPool;

public class FrogPool extends GenericPool<Frog> {

	public static FrogPool instance;

	public static FrogPool sharedEnemyPool() {

		if (instance == null)
			instance = new FrogPool();
		return instance;

	}

	private FrogPool() {
		super();
	}

	@Override
	protected Frog onAllocatePoolItem() {
		return new Frog();
	}

	@Override
	protected void onHandleObtainItem(Frog pItem) {
		pItem.init();
	}

	protected void onHandleRecycleItem(final Frog e) {
		e.sprite.setVisible(false);
		e.sprite.detachSelf();
		e.clean();

	}
}