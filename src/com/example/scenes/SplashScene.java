package com.example.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.example.base.BaseScene;
import com.example.managers.SceneManager.SceneType;

public class SplashScene extends BaseScene
{
	
	private Sprite splash;	
	
    @Override
    public void createScene()
    {
    	splash = new Sprite(0, 0, resourcesManager.splash_region, vbom) {
    	    @Override
    	    protected void preDraw(GLState pGLState, Camera pCamera) 
    	    {
    	       super.preDraw(pGLState, pCamera);
    	       pGLState.enableDither();
    	    }
    	};
    	        
    	splash.setScale(1.5f);
    	splash.setPosition(400, 240);
    	//splash.setPosition((GameActivity.getScreenWidth() / 2) - (splash_width / 2), (GameActivity.getScreenHeight() / 2) - (splash_height / 2));
    	attachChild(splash);
    }

    @Override
    public void onBackKeyPressed()
    {

    }

    @Override
    public SceneType getSceneType()
    {
    	return SceneType.SCENE_SPLASH;
    }

    @Override
    public void disposeScene()
    {
    	splash.detachSelf();
        splash.dispose();
        this.detachSelf();
        this.dispose();
    }
}