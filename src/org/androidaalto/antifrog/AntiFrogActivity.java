package org.androidaalto.antifrog;


import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.view.Display;

/**
 * Anti Frog Game
 *
 * @author Android Aalto
 */
public class AntiFrogActivity extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static int displayWidth = 720;
	private static int displayHeight = 480;
	private static final int NUMBER_OF_FROGS = 1;


	// ===========================================================
	// Game logic
	// ===========================================================
	private List<Frog> frogList;
	 
	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mPlayerTextureRegion;
	private BitmapTextureAtlas mAutoParallaxBackgroundTexture;
	
	private ITextureRegion mParallaxLayerBack;
	private ITextureRegion mParallaxLayerMid;
	
	private BitmapTextureAtlas mBitmapTextureFrogger;
	private ITextureRegion mFroggerCar;
	private ITextureRegion mFrogger;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	//@Override
	
	public EngineOptions onCreateEngineOptions() {
		
		// obtain the screen resolution of the device
		Display display = getWindowManager().getDefaultDisplay(); 
		this.displayHeight = display.getHeight();
		this.displayWidth = display.getWidth();
		
		// generate the set of frogs
		this.frogList = new ArrayList<Frog>();
		for (int i=0; i<= NUMBER_OF_FROGS; i++) {
			this.frogList.add(new Frog(10, 5, 2)); //make it random x(0-100), y(0-10) an leaplength(0-10)
		}
		
		final Camera camera = new Camera(0, 0, displayWidth, displayHeight);
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(displayWidth, displayHeight), camera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "player.png", 73, 0, 3, 4);
		this.mBitmapTextureAtlas.load();

		this.mAutoParallaxBackgroundTexture = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024);
		this.mParallaxLayerBack = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "StillBG.png", 0, 0);
		this.mParallaxLayerMid = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "MovingBG.png", 0, 481);
		this.mAutoParallaxBackgroundTexture.load();
		
		this.mBitmapTextureFrogger = new BitmapTextureAtlas(this.getTextureManager(), 125, 100, TextureOptions.BILINEAR);
		this.mFroggerCar = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureFrogger, this, "Car.png", 0, 0);
		this.mFrogger = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureFrogger, this, "Frog.png", 101, 0);
		this.mBitmapTextureFrogger.load();
	}

	@Override
	public Scene onCreateScene() {	
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, new Sprite(0, displayHeight - this.mParallaxLayerBack.getHeight(), this.mParallaxLayerBack, vertexBufferObjectManager)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(7.0f, new Sprite(0, (displayHeight - this.mParallaxLayerMid.getHeight())/2, this.mParallaxLayerMid, vertexBufferObjectManager)));
		scene.setBackground(autoParallaxBackground);

		
		/* Calculate the coordinates for the face, so its centered on the camera. */
		//final float playerX = (displayWidth - this.mPlayerTextureRegion.getWidth()) / 2;
		//final float playerY = displayHeight - this.mPlayerTextureRegion.getHeight() - 5;

		/* Create two sprite and add it to the scene. */
		//final AnimatedSprite player = new AnimatedSprite(playerX, playerY, this.mPlayerTextureRegion, vertexBufferObjectManager);
		//player.setScaleCenterY(this.mPlayerTextureRegion.getHeight());
		//player.setScale(2);
		//player.animate(new long[]{200, 200, 200}, 3, 5, true);

		final Sprite car = new Sprite(displayWidth-130, displayHeight/2-50, this.mFroggerCar, vertexBufferObjectManager);
		car.setScale(1.5f);
		car.setRotation(-90);
		final Sprite frog = new Sprite(displayWidth-330, displayHeight/2-90, this.mFrogger, vertexBufferObjectManager);
		
		final Path path = new Path(5).to(10, 10).to(180, 155).to(380, 155).to(460,255).to(640, 255);

		PathModifier modifier = new PathModifier(16, path);
		
		frog.registerEntityModifier(modifier);
		

		scene.attachChild(car);
		scene.attachChild(frog);

		scene.registerTouchArea(car);
		
		//scene.registerTouchArea(mySecondSprite);
		//scene.setTouchAreaBindingEnabled(true);
		
		return scene;
	}
	

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
