package com.example.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;

import com.example.base.BaseScene;
import com.example.managers.ResourcesManager;
import com.example.managers.SceneManager;
import com.example.managers.SceneManager.SceneType;
import com.example.testingand.GameActivity;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener{
	
	private MenuScene menuChildScene;
//	private final int MENU_PLAY = 0;
//	private final int MENU_OPTIONS = 1;

	@Override
	public void createScene() {
		createBackground();
		createMenuChildScene();
		playSounds();
		
		setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(Scene pScene,
					TouchEvent pSceneTouchEvent) {
				SceneManager.getInstance().loadGameScene(engine);
				return false;
			}
	    });
	}

	private void playSounds() {
		ResourcesManager.getInstance().its_me_mario_sound.play();		
	}

	@Override
	public void onBackKeyPressed() {
		 System.exit(0);		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}
	
	private void createBackground() {
		
    	final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
    	setBackground(autoParallaxBackground);

    	final Sprite parallaxLayerBackSprite = new Sprite(0, 0, resourcesManager.menu_background_region, vbom);
		parallaxLayerBackSprite.setOffsetCenter(0, 0);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, parallaxLayerBackSprite));
		
		final Sprite parallaxLayerMidSprite = new Sprite(0, GameActivity.CAMERA_HEIGHT - resourcesManager.menu_background_clouds_region.getHeight() - 80, resourcesManager.menu_background_clouds_region, vbom);
		parallaxLayerMidSprite.setOffsetCenter(0, 0);
		parallaxLayerMidSprite.setScale(1.0f);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-5.0f, parallaxLayerMidSprite));
		
	    attachChild(new Sprite(400, 300, resourcesManager.menu_background_logo_region, vbom)
	    {
	        @Override
	        protected void preDraw(GLState pGLState, Camera pCamera) 
	        {
	            super.preDraw(pGLState, pCamera);
	            pGLState.enableDither();
	        }
	    });
	    
	    Sprite revenge = new Sprite(400, 200, resourcesManager.menu_background_revenge_region, vbom)
	    {
	        @Override
	        protected void preDraw(GLState pGLState, Camera pCamera) 
	        {
	            super.preDraw(pGLState, pCamera);
	            pGLState.enableDither();  
	        }        
	    };
	    
	    final LoopEntityModifier scaleModifier = new LoopEntityModifier(new SequenceEntityModifier(new ScaleModifier(0.8f, 1.1f, 1)));	    
	    revenge.registerEntityModifier(scaleModifier);
	    
	    attachChild(revenge);
	}
	
	private void createMenuChildScene()
	{
	    menuChildScene = new MenuScene(camera);
	    menuChildScene.setPosition(400, 240);
	    
//	    final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.2f, 1);
//	    final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS, resourcesManager.options_region, vbom), 1.2f, 1);
//	    menuChildScene.addMenuItem(playMenuItem);
//	    menuChildScene.addMenuItem(optionsMenuItem);
	    
	    menuChildScene.buildAnimations();
	    menuChildScene.setBackgroundEnabled(false);
	    
//	    playMenuItem.setPosition(0, 0);
//	    optionsMenuItem.setPosition(0, -120);
	    
	    Text pressAnyKeyText = new Text(400, 50, resourcesManager.menu_font, "touch screen to play...", vbom);
	    
	    final LoopEntityModifier blinkModifier = new LoopEntityModifier(
	    	    new SequenceEntityModifier(new FadeOutModifier(0.25f), new FadeInModifier(0.25f)));
	    
	    pressAnyKeyText.registerEntityModifier(blinkModifier);
	    
	    attachChild(pressAnyKeyText);
	    
	    menuChildScene.setOnMenuItemClickListener(this);
	   
	    setChildScene(menuChildScene);
	}
	
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
	{
//		switch(pMenuItem.getID())
//	    {
//	        case MENU_PLAY:
//	            //Load Game Scene!
//	            SceneManager.getInstance().loadGameScene(engine);
//	            return true;
//	        case MENU_OPTIONS:
//	            return true;
//	        default:
//	            return false;
//	    }
		
		return false;
	}

}
