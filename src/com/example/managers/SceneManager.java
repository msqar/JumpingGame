package com.example.managers;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import com.example.base.BaseScene;
import com.example.scenes.GameOverScene;
import com.example.scenes.GameScene;
import com.example.scenes.InfoScene;
import com.example.scenes.LoadingScene;
import com.example.scenes.MainMenuScene;
import com.example.scenes.SplashScene;

public class SceneManager
{
    //---------------------------------------------
    // SCENES
    //---------------------------------------------
    
    private BaseScene splashScene;
    private BaseScene menuScene;
    private BaseScene gameScene;
    private BaseScene loadingScene;
    private BaseScene infoScene;
    private BaseScene gameOverScene;
    
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private static final SceneManager INSTANCE = new SceneManager();
    
    private SceneType currentSceneType = SceneType.SCENE_SPLASH;
    
    private BaseScene currentScene;
    
    private Engine engine = ResourcesManager.getInstance().engine;
    
    public enum SceneType
    {
        SCENE_SPLASH,
        SCENE_MENU,
        SCENE_GAME,
        SCENE_LOADING,
        SCENE_INFO,
        SCENE_GAMEOVER,
    }
    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------
    
    public void setScene(BaseScene scene)
    {
        engine.setScene(scene);
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }
    
    public void setScene(SceneType sceneType)
    {
        switch (sceneType)
        {
            case SCENE_MENU:
                setScene(menuScene);
                break;
            case SCENE_GAME:
                setScene(gameScene);
                break;
            case SCENE_SPLASH:
                setScene(splashScene);
                break;
            case SCENE_LOADING:
                setScene(loadingScene);
                break;
            case SCENE_INFO:
            	setScene(infoScene);
            case SCENE_GAMEOVER:
            	setScene(gameOverScene);
            default:
                break;
        }
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static SceneManager getInstance()
    {
        return INSTANCE;
    }
    
    public SceneType getCurrentSceneType()
    {
        return currentSceneType;
    }
    
    public BaseScene getCurrentScene()
    {
        return currentScene;
    }
    
    public void loadGameScene(final Engine mEngine)
    {
    	ResourcesManager.getInstance().loadGameResources();
    	getGameScene().getGameHUD().setVisible(false);
        setScene(loadingScene);
        ResourcesManager.getInstance().unloadMenuTextures();
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
        {        	
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler); 
				if(ResourcesManager.getInstance().lives > 0) {
					
					infoScene = new InfoScene();
					setScene(infoScene);
					
					mEngine.registerUpdateHandler(new TimerHandler(3.5f, new ITimerCallback() {

						@Override
						public void onTimePassed(TimerHandler pTimerHandler) {
							
							mEngine.unregisterUpdateHandler(pTimerHandler);
							loadMusic();
							gameScene = new GameScene();
			                setScene(gameScene);							
						}
						
					}));
				}else{
					gameOverScene = new GameOverScene();
					setScene(gameOverScene);
					ResourcesManager.getInstance().resetGameVariables();
					mEngine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback() {

						@Override
						public void onTimePassed(TimerHandler pTimerHandler) {
							
							mEngine.unregisterUpdateHandler(pTimerHandler);
							disposeGameOverScene();
							loadMenuScene(engine);							
						}
						
					}));
				}
                
            }
        }));
    }
    
    public void createMenuScene()
    {
    	ResourcesManager.getInstance().loadMenuResources();
        menuScene = new MainMenuScene();
        loadingScene = new LoadingScene();
        SceneManager.getInstance().setScene(menuScene);
        disposeSplashScene();
    }
    
    public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback)
    {
        ResourcesManager.getInstance().loadSplashScreen();
        splashScene = new SplashScene();
        currentScene = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }
    
    private void disposeSplashScene()
    {
        ResourcesManager.getInstance().unloadSplashScreen();
        splashScene.disposeScene();
        splashScene = null;
    }
    
    private void disposeGameOverScene() {
    	gameOverScene.dispose();
    	gameOverScene = null;
    }
    
    public void loadMenuScene(final Engine mEngine)
    {
        setScene(loadingScene);
        gameScene.disposeScene();
        ResourcesManager.getInstance().unloadGameTextures();
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadMenuTextures();
                setScene(menuScene);
            }
        }));
    }
    
    public GameScene getGameScene() {
    	return new GameScene();
    }

	public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	} 
	
	private void loadMusic() {
		ResourcesManager.getInstance().mario_song_music.play();	
		ResourcesManager.getInstance().mario_song_music.setVolume(0.5f);
		ResourcesManager.getInstance().mario_song_music.setLooping(true);	
	}
    
}
