package com.mario.revenge.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.mario.revenge.base.BaseScene;
import com.mario.revenge.managers.ResourcesManager;
import com.mario.revenge.managers.SceneManager.SceneType;

public class SplashScene extends BaseScene
{
	
	private Sprite andengineLogo;
	
    @Override
    public void createScene()
    {
    	
    	andengineLogo = new Sprite(400, 240, ResourcesManager.getInstance().splash_background_andengine_region, vbom)
	    {
	        @Override
	        protected void preDraw(GLState pGLState, Camera pCamera) 
	        {
	            super.preDraw(pGLState, pCamera);
	            pGLState.enableDither();  
	        }        
	    };
	    
	    andengineLogo.setScale(0.5f, 0.5f);
	    final SequenceEntityModifier scaleModifier = new SequenceEntityModifier(new SequenceEntityModifier(new ScaleModifier(0.8f, 0.5f, 0.8f)));	    
	    andengineLogo.registerEntityModifier(scaleModifier);	    

	    attachChild(andengineLogo);
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
    	andengineLogo.detachSelf();
    	andengineLogo.dispose();
        this.detachSelf();
        this.dispose();
    }
}