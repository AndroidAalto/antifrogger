package org.androidaalto.antifrog;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class SensorListener implements SensorEventListener {

	static SensorListener instance;
	AntiFrogScene scene;

	public static SensorListener getSharedInstance() {
		if (instance == null)
			instance = new SensorListener();
		return instance;
	}

	private SensorListener() {
		instance = this;
		scene = (AntiFrogScene) AntiFrogActivity.getSharedInstance().mCurrentScene;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				scene.accelerometerSpeedY = event.values[1];
				break;
			default:
				break;
			}
		}
	}
}