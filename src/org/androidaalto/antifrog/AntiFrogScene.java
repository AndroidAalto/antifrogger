package org.androidaalto.antifrog;

import java.util.Iterator;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import org.andengine.entity.particle.emitter.PointParticleEmitter;

import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.RotationParticleModifier;
import org.andengine.entity.primitive.Rectangle;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

/*
 * Anti Frog Game 
 *
 * @author Android Aalto
*/

public class AntiFrogScene  extends Scene implements IOnSceneTouchListener {
	public Car f_car;
	Camera mCamera;
	public float accelerometerSpeedY;
	SensorManager sensorManager;
	public int frogCount;
	public int missCount;
	public Text sc_score;
	
	public AntiFrogScene() {		
		AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		VertexBufferObjectManager vertexBufferObjectManager = AntiFrogActivity.getSharedInstance().getVertexBufferObjectManager();
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(18.0f, new Sprite(0, (AntiFrogActivity.displayHeight - AntiFrogActivity.getSharedInstance().mFroggerLayerMid.getHeight())/2, AntiFrogActivity.getSharedInstance().mFroggerLayerMid, vertexBufferObjectManager)));
		setBackground(autoParallaxBackground );

		attachChild(new FrogLayer(24));
		
		mCamera = AntiFrogActivity.getSharedInstance().mCamera;
		f_car = Car.getSharedInstance();
		f_car.sprite.detachSelf();

		attachChild(f_car.sprite);
		f_car.sprite.setVisible(true);		

		AntiFrogActivity.getSharedInstance().setCurrentScene(this);
		sensorManager = (SensorManager) AntiFrogActivity.getSharedInstance().getSystemService(BaseGameActivity.SENSOR_SERVICE);
		SensorListener.getSharedInstance();
		sensorManager.registerListener(SensorListener.getSharedInstance(), sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);
		setOnSceneTouchListener(this);
		
		resetValues();
	}
	
	public void resetValues() {
		missCount = 0;
		FrogLayer.purgeAndRestart();
		clearChildScene();
		registerUpdateHandler(new GameLoopUpdateHandler());
	}
	
	public void detach() {
		clearUpdateHandlers();

		detachChildren();
		Car.instance = null;
		FrogPool.instance = null;
	}
	
	public void moveCar() {
		f_car.moveCar(accelerometerSpeedY);
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void cleaner() {
		synchronized (this) {
			if (FrogLayer.isEmpty()) {
				Log.v("Jimvaders", "GameScene Cleaner() cleared");
				clearUpdateHandlers();
			}
			Iterator<Frog> eIt = FrogLayer.getIterator();
			while (eIt.hasNext()) {
				Frog e = eIt.next();
				if (f_car.sprite.collidesWith(e.sprite)) {

					if (!e.gotHit()) {
						createExplosion(e.sprite.getX(), e.sprite.getY(),
								e.sprite.getParent(),
								AntiFrogActivity.getSharedInstance());
						FrogPool.sharedEnemyPool().recyclePoolItem(e);
						frogCount++;
						showScore(frogCount);
						eIt.remove();
					}
					break;
				}
			}
		}
	}
	
	private void showScore(int score){
		detachChild(sc_score);
		sc_score = new Text(0, 0, AntiFrogActivity.getSharedInstance().sFont, String.valueOf(score) ,
				AntiFrogActivity.getSharedInstance().getVertexBufferObjectManager());

		sc_score.setPosition(sc_score.getWidth(), AntiFrogActivity.displayHeight-sc_score.getHeight());
		sc_score.setRotation(-90);
		attachChild(sc_score);
	}
	
	private void createExplosion(final float posX, final float posY,
			final IEntity target, final SimpleBaseGameActivity activity) {

		int mNumPart = 15;
		int mTimePart = 2;

		PointParticleEmitter particleEmitter = new PointParticleEmitter(posX, posY);
		IEntityFactory<Rectangle> recFact = new IEntityFactory<Rectangle>() {

			@Override
			public Rectangle create(float pX, float pY) {
				Rectangle rect = new Rectangle(posX, posY, 10, 10,
						activity.getVertexBufferObjectManager());
				rect.setColor(Color.GREEN);
				return rect;
			}
		};
		
		final ParticleSystem<Rectangle> particleSystem = new ParticleSystem<Rectangle>(
				recFact, particleEmitter, 500, 500, mNumPart);

		particleSystem
				.addParticleInitializer(new VelocityParticleInitializer<Rectangle>(-50, 50, -50, 50));
		particleSystem
				.addParticleModifier(new AlphaParticleModifier<Rectangle>(0, 0.6f * mTimePart, 1, 0));
		particleSystem
				.addParticleModifier(new RotationParticleModifier<Rectangle>(0, mTimePart, 0, 360));

		target.attachChild(particleSystem);
		target.registerUpdateHandler(new TimerHandler(mTimePart,
				new ITimerCallback() {
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler) {
						particleSystem.detachSelf();
						target.sortChildren();
						target.unregisterUpdateHandler(pTimerHandler);
					}
				}));
	}
}
