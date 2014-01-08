package com.example.testingand;


import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;

import org.andengine.ui.activity.BaseGameActivity;

import android.view.KeyEvent;

import com.example.managers.ResourcesManager;
import com.example.managers.SceneManager;

public class GameActivity extends BaseGameActivity {
	
	private BoundCamera camera;
	private static ResourcesManager resourcesManager;
	private static int CAMERA_WIDTH = 800;
	private static int CAMERA_HEIGHT = 480;		

	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
	    EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), this.camera);
	    engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
	    engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
	    engineOptions.getTouchOptions().setNeedsMultiTouch(true); // supporting multi-touch
	    return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException
	{
	    ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
	    resourcesManager = ResourcesManager.getInstance();
	    pOnCreateResourcesCallback.onCreateResourcesFinished();
	}
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
	    return new LimitedFPSEngine(pEngineOptions, 60);
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);		
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
	{
	    mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
	    {
	        public void onTimePassed(final TimerHandler pTimerHandler) 
	        {
	            mEngine.unregisterUpdateHandler(pTimerHandler);
	            SceneManager.getInstance().createMenuScene();
	        }
	    }));
	    pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
//	private void getSystemResolution() {
//		DisplayMetrics displayMetrics = new DisplayMetrics();
//		// the results will be higher than using the activity context object or the getWindowManager() shortcut
//		WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); 
//		wm.getDefaultDisplay().getMetrics(displayMetrics);
//		WIDTH = displayMetrics.widthPixels;
//		HEIGHT = displayMetrics.heightPixels;
//		System.out.println(WIDTH + "  " + HEIGHT);
//	}
//	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	    System.exit(0);	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	        SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
	    }
	    return false; 
	}

	public static ResourcesManager getResourcesManager() {
		return resourcesManager;
	}

	public static void setResourcesManager(ResourcesManager resourcesManager) {
		GameActivity.resourcesManager = resourcesManager;
	}	
}
