package org.androidaalto.antifrog;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import android.graphics.Typeface;
import android.util.Log;

/*
 * Anti Frog Game 
 *
 * @author Android Aalto
*/

public class AntiFrogActivity extends SimpleBaseGameActivity {
	static final int displayWidth = 720;
	static final int displayHeight = 480;

	public Camera mCamera;
	public Font mFont;
	public Font sFont;
	
	public Scene mCurrentScene;
	public static AntiFrogActivity instance;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private BitmapTextureAtlas mAntiFroggerBackgroundTexture;
	private BitmapTextureAtlas mBitmapTextureFrogger;
	private BitmapTextureAtlas sFontT;
	
	TiledTextureRegion mCarTextureRegion;
	ITextureRegion mFroggerLayerMid;
	ITextureRegion mFrogger;
	
	
	public static AntiFrogActivity getSharedInstance() {
		return instance;
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		instance = this;
		mCamera = new Camera(0, 0, displayWidth, displayHeight);
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(displayWidth, displayHeight), mCamera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 150, 150, TextureOptions.BILINEAR);
		this.mCarTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "carhd.png", 0, 0, 2, 1);
		this.mBitmapTextureAtlas.load();

		this.mAntiFroggerBackgroundTexture = new BitmapTextureAtlas(this.getTextureManager(), 720, 480);
		this.mFroggerLayerMid = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAntiFroggerBackgroundTexture, this, "roadhd.png", 0, 0);
		this.mAntiFroggerBackgroundTexture.load();
		
		this.mBitmapTextureFrogger = new BitmapTextureAtlas(this.getTextureManager(), 125, 100, TextureOptions.BILINEAR);
		this.mFrogger = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureFrogger, this, "Frog.png", 101, 0);
		this.mBitmapTextureFrogger.load();
		
		mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		mFont.load();
		sFontT = new BitmapTextureAtlas(this.getTextureManager(),256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		sFont = new Font(this.getFontManager(), sFontT, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, true, Color.WHITE);
		sFont.load();
	}

	@Override
	public Scene onCreateScene() {	
		mEngine.registerUpdateHandler(new FPSLogger());
		mCurrentScene = new SplashScene();
		return mCurrentScene;
	}
	
	// to change the current main scene
	public void setCurrentScene(Scene scene) {
		mCurrentScene = null;
		mCurrentScene = scene;
		getEngine().setScene(mCurrentScene);
	}

	@Override
	public void onBackPressed() {
		Log.v("AntiFrogger", "BaseActivity BackPressed " + mCurrentScene.toString());
		if (mCurrentScene instanceof AntiFrogScene)((AntiFrogScene) mCurrentScene).detach();

		mCurrentScene = null;
		SensorListener.instance = null;
		super.onBackPressed();
	}
}
