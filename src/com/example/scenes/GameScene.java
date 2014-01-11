package com.example.scenes;

import java.io.IOException;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.example.base.BaseScene;
import com.example.extras.LevelCompleteWindow;
import com.example.extras.LevelCompleteWindow.StarsCount;
import com.example.managers.ResourcesManager;
import com.example.managers.SceneManager;
import com.example.managers.SceneManager.SceneType;
import com.example.object.SimpleCoin;
import com.example.object.Player;
import com.example.testingand.GameActivity;

public class GameScene extends BaseScene implements IOnSceneTouchListener
{
	
	private HUD gameHUD;
	private Text totalCoinsText;
	private Text totalScoreText;
	private Text marioTitleText;
	private Text mapLevelText;
	private Text timeTitleText;
	
	public static int coins = 0;
	public static int totalScore = 0;
	private PhysicsWorld physicsWorld;
	
	// Variables for game
	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
	    
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BRICK_FLOOR = "brick_floor";
	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MOUNTAIN_ONE = "mountain_one";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TRIPLE_BUSH = "triple_bush";
	
	
//	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 = "platform1";
//	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 = "platform2";
//	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3 = "platform3";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN = "coin";
	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "mario";
	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE = "levelComplete";
    
	private Player player;
//	private BoxCoin boxCoin;
	private SimpleCoin simpleCoin;
	
	private int amountOfCoinsGrabbed = 0;
	private static final int TOTAL_AMOUNT_OF_COINS = 3;
	
//	private boolean firstTouch = false;
	
	// Handle GAME OVER
	private Text gameOverText;
	private boolean gameOverDisplayed = false;
	
	// Handle Complete Game
	private LevelCompleteWindow levelCompleteWindow;
	
	@Override
	public void createScene()
	{
		initializeVars();
	    createBackground();
	    createHUD();
	    createControls();
	    createPhysics();
	    loadLevel(1);
	    loadMusic();
	    createGameOverText();
	    setOnSceneTouchListener(this);
	    levelCompleteWindow = new LevelCompleteWindow(vbom);
	}

	private void initializeVars() {
		coins = 0;
		totalScore = 0;
	}

	@Override
	public void onBackKeyPressed()
	{
		if(ResourcesManager.getInstance().mario_song_music.isPlaying()) {
    		ResourcesManager.getInstance().mario_song_music.stop();
    	}
		ResourcesManager.getInstance().mario_game_over_sound.stop();
		ResourcesManager.getInstance().level_completed_sound.stop();
		SceneManager.getInstance().loadMenuScene(engine);
	}

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
        camera.setHUD(null);
        camera.setCenter(400, 240);
        // TODO code responsible for disposing scene
        // removing all game scene objects.
        camera.setChaseEntity(null);
    }
    
    private void createBackground()
    {
    	final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
    	setBackground(autoParallaxBackground);
    	
    	final Sprite parallaxLayerBackSprite = new Sprite(0, 0, ResourcesManager.getInstance().parallax_game_background_region, vbom);
		parallaxLayerBackSprite.setOffsetCenter(0, 0);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, parallaxLayerBackSprite));
		
		final Sprite parallaxLayerMidSprite = new Sprite(0, GameActivity.CAMERA_HEIGHT - ResourcesManager.getInstance().parallax_game_background_clouds_region.getHeight() - 80, ResourcesManager.getInstance().parallax_game_background_clouds_region, vbom);
		parallaxLayerMidSprite.setOffsetCenter(0, 0);
		parallaxLayerMidSprite.setScale(1.0f);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-5.0f, parallaxLayerMidSprite));
		
    }
    
    private void createHUD()
    {
        gameHUD = new HUD();
        
        // CREATE SCORE TEXT
        marioTitleText = new Text(20, 430, resourcesManager.font, "MARIO", new TextOptions(HorizontalAlign.LEFT), vbom);
        marioTitleText.setAnchorCenter(0, 0);
        marioTitleText.setText("MARIO");
        
        totalScoreText = new Text(20, 400, resourcesManager.font, "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        totalScoreText.setAnchorCenter(0, 0);
        totalScoreText.setText("000000");
        
        final AnimatedSprite coinHUDSprite = new AnimatedSprite(240, 410, resourcesManager.menu_hud_coin_region, vbom);
        coinHUDSprite.setScale(1.2f);
        final long[] ANIMATE_COIN_HUD = {900,900,900};
        coinHUDSprite.animate(ANIMATE_COIN_HUD, 0, 2, true);
        
        totalCoinsText = new Text(250, 400, resourcesManager.font, "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        totalCoinsText.setAnchorCenter(0, 0);
        totalCoinsText.setText("x0" + coins);
        
        mapLevelText = new Text(380, 430, resourcesManager.font, "WORLD", new TextOptions(HorizontalAlign.LEFT), vbom);
        mapLevelText.setAnchorCenter(0, 0);
        mapLevelText.setText("WORLD");
        
        timeTitleText = new Text(550, 430, resourcesManager.font, "TIME", new TextOptions(HorizontalAlign.LEFT), vbom);
        timeTitleText.setAnchorCenter(0, 0);
        timeTitleText.setText("TIME");
        
        gameHUD.attachChild(totalCoinsText);
        gameHUD.attachChild(coinHUDSprite);
        gameHUD.attachChild(marioTitleText);
        gameHUD.attachChild(totalScoreText);
        gameHUD.attachChild(mapLevelText);
        gameHUD.attachChild(timeTitleText);
        
        camera.setHUD(gameHUD);        
    }
    
//    private void addToScore(int i)
//    {
//        score += i;
//        scoreText.setText("Score: " + score);        
//        ResourcesManager.getInstance().grab_coin_sound.setVolume(1.0f);
//        ResourcesManager.getInstance().grab_coin_sound.play();
//    }
    
    private void createPhysics()
    {
    	physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false); 
    	physicsWorld.setContactListener(contactListener());
        registerUpdateHandler(physicsWorld);
    }
    
    private void loadLevel(int levelID)
    {
        final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);
        
        final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
        
        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL)
        {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException 
            {
                final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
                final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
                
                camera.setBounds(0, 0, width, height); // here we set camera bounds
                camera.setBoundsEnabled(true);

                return GameScene.this;
            }
        });
        
        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY)
        {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
            {
                final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
                final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
                final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);
                
                final Sprite levelObject;
                
                if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BRICK_FLOOR))
                {
                    levelObject = new Sprite(x, y, resourcesManager.brick_floor_region, vbom);
                    PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF).setUserData("brick_floor");
                }
                
//                if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1))
//                {
//                    levelObject = new Sprite(x, y, resourcesManager.dirt_region, vbom);
//                    PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF).setUserData("platform1");
//                } 
//                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2))
//                {
//                    levelObject = new Sprite(x, y, resourcesManager.platform2_region, vbom);
//                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
//                    body.setUserData("platform2");
//                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
//                }
//                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3))
//                {
//                    levelObject = new Sprite(x, y, resourcesManager.platform3_region, vbom);
//                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
//                    body.setUserData("platform3");
//                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
//                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MOUNTAIN_ONE)) {
                	levelObject = new Sprite(x, y, resourcesManager.mountain_one_region, vbom);
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TRIPLE_BUSH)) {
                	levelObject = new Sprite(x, y, resourcesManager.triple_bush_region, vbom);
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN))
                {               	
                	
                	simpleCoin = new SimpleCoin(x, y, vbom, camera, physicsWorld)
                	{
                	    @Override
                	    protected void onManagedUpdate(float pSecondsElapsed) 
                	    {                	    	              	    	
                	        super.onManagedUpdate(pSecondsElapsed);
                	        if (player.collidesWith(this))
                	        {                	        	
                	            simpleCoin.addToScore(totalCoinsText, totalScoreText);                	            
                	            simpleCoin.pickedFromGroundShowPointsAnimation(x, y);
                	            this.setVisible(false);
                	            this.setIgnoreUpdate(true);
                	            amountOfCoinsGrabbed++;
                	        }
                	    }
                	};
                	levelObject = simpleCoin;
//                  levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER))
                {
                    player = new Player(x, y, vbom, camera, physicsWorld)
                    
                    {                   	
                    	@Override
                    	public void onDie()
                    	{
                    	    if (!gameOverDisplayed)
                    	    {
                    	    	// Stopping mario song
                    	    	if(ResourcesManager.getInstance().mario_song_music.isPlaying()) {
                    	    		ResourcesManager.getInstance().mario_song_music.stop();
                    	    	}
                    	    	
                    	    	player.dieAnimation();
                    	    	
                    	    	ResourcesManager.getInstance().mario_game_over_sound.play();
                    	        displayGameOverText();
                    	    }
                    	}

                    };
                    levelObject = player;
                    levelObject.setScale(1.5f);
                } 
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE))
                {
                    levelObject = new Sprite(x, y, resourcesManager.complete_stars_region, vbom)
                    {
                        @Override
                        protected void onManagedUpdate(float pSecondsElapsed) 
                        {
                            super.onManagedUpdate(pSecondsElapsed);

                            if (player.collidesWith(this))
                            {
                            	// Removing player from screen to avoid losing and winning at the same time lol
                            	player.detachSelf();
                            	physicsWorld.unregisterPhysicsConnector(physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(player));
                            	
                            	// Checking how many stars the player has earned based on how many he grabbed
                            	float starCount = (float) amountOfCoinsGrabbed / TOTAL_AMOUNT_OF_COINS;
                            	StarsCount count = null;
                            	
                            	if(starCount <= 0.34f) {
                            		count = StarsCount.ONE;
                            	}else if(starCount <= 0.67f) {
                            		count = StarsCount.TWO;
                            	}else if(starCount == 1.0f){
                            		count = StarsCount.THREE;
                            	}else{
                            		count = StarsCount.ONE;
                            	}
                            	
                                levelCompleteWindow.display(count, GameScene.this, camera);
                                this.setVisible(false);
                                this.setIgnoreUpdate(true);
                                
                                // Stopping mario song
                                if(ResourcesManager.getInstance().mario_song_music.isPlaying()) {
                    	    		ResourcesManager.getInstance().mario_song_music.stop();
                    	    	}
                                ResourcesManager.getInstance().level_completed_sound.play();
                            }
                        }
                    };
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }
                else
                {
                    throw new IllegalArgumentException();
                }

                levelObject.setCullingEnabled(true);

                return levelObject;
            }
        });

        levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
    }
    
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
    {
    	if (pSceneTouchEvent.isActionDown())
        {
           // Do nothing for now
        }
        return false;
    }
    
    private void createGameOverText()
    {
        gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", vbom);
    }

    private void displayGameOverText()
    {
        camera.setChaseEntity(null);
        gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
        attachChild(gameOverText);
        gameOverDisplayed = true;
    }
    
    private ContactListener contactListener()
    {
        ContactListener contactListener = new ContactListener()
        {
            public void beginContact(Contact contact)
            {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();

                if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
                {
                    if (x2.getBody().getUserData().equals("mario"))
                    {
                        player.increaseFootContacts();
                    }
                }
                if (x1.getBody().getUserData().equals("platform3") && x2.getBody().getUserData().equals("mario"))
                {
                    x1.getBody().setType(BodyType.DynamicBody);
                }
                if (x1.getBody().getUserData().equals("platform2") && x2.getBody().getUserData().equals("mario"))
                {
                    engine.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback()
                    {                                    
                        public void onTimePassed(final TimerHandler pTimerHandler)
                        {
                            pTimerHandler.reset();
                            engine.unregisterUpdateHandler(pTimerHandler);
                            x1.getBody().setType(BodyType.DynamicBody);
                        }
                    }));
                }
            }

            public void endContact(Contact contact)
            {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();

                if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
                {
                    if (x2.getBody().getUserData().equals("mario"))
                    {
                        player.decreaseFootContacts();
//                        if(player.getFootContacts() > 0) {
//                        	player.jumpingEnd();
//                        }
                    }
                    
                }
            }

            public void preSolve(Contact contact, Manifold oldManifold)
            {

            }

            public void postSolve(Contact contact, ContactImpulse impulse)
            {

            }
        };
        return contactListener;
    } 
    
    private void createControls() {
    	
		final Sprite leftArrowButton = new Sprite(21, 21, ResourcesManager.getInstance().control_left_arrow_region, vbom) {
		
		        @Override
		        public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
		                        final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
	                if (pSceneTouchEvent.isActionDown()) {
                        if (!player.isAnimationRunning())
                            player.setFlippedHorizontal(true);
                        	player.setRunningLeft();
	
	                } else if (pSceneTouchEvent.isActionUp()) {		                        
                        if (player.isJumping()){
                            player.stopAnimation(5);
                        }else{
                            player.stopAnimation(0);
                    		player.stopMoving();
                        }
                }		
                return true;
	        };
		};
		
		final Sprite rightArrowButton = new Sprite(110, 21, ResourcesManager.getInstance().control_right_arrow_region, vbom) {
		        @Override
		        public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
		                        final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		
		                if (pSceneTouchEvent.isActionDown()) {
	                        if (!player.isAnimationRunning())
	                        	if(player.isFlippedHorizontal()) {
	                        		player.setFlippedHorizontal(false);
	                        	}
	                        	player.setRunningRight();	                        	
		
		                } else if (pSceneTouchEvent.isActionUp()) {		                        
	                        if (player.isJumping()){
                                player.stopAnimation(5);
	                        }else{
                                player.stopAnimation(0);
                        		player.stopMoving();
	                        }
	                }		
	                return true;
		        };
		};
		
		final Sprite aButton = new Sprite(700, 50, ResourcesManager.getInstance().control_a_button_region, vbom) {
			
		        @Override
		        public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
		                        final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		        	
		        	if (pSceneTouchEvent.isActionDown()) {
//			        	if (player.getFootContacts() > 0)
//		        		player.getPlayerBody().applyLinearImpulse(new Vector2(0, 8), player.getPlayerBody().getPosition());
	                    player.jump();
			        } else if (pSceneTouchEvent.isActionUp()) {
			        				        		
			        }
			        return true;
		        };
		};
		
		leftArrowButton.setScale(0.5f);
		rightArrowButton.setScale(0.5f);
		aButton.setScale(0.5f);
		
		gameHUD.attachChild(aButton);
		gameHUD.attachChild(leftArrowButton);
		gameHUD.attachChild(rightArrowButton);		

		gameHUD.registerTouchArea(leftArrowButton);
		gameHUD.registerTouchArea(rightArrowButton);
		gameHUD.registerTouchArea(aButton);
    	//final DigitalOnScreenControl control = new DigitalOnScreenControl(20, camera.getHeight() - ResourcesManager.getInstance().control_base_region.getHeight(), 
//    	final DigitalOnScreenControl control = new DigitalOnScreenControl(300, 300,
//    			camera, ResourcesManager.getInstance().control_base_region, ResourcesManager.getInstance().control_knob_region, 
//    			0.1f, vbom, new IOnScreenControlListener() {					
//					public void onControlChange(BaseOnScreenControl pBaseOnScreenControl, float x, float y) {
//						if(player.getPlayerBody().getLinearVelocity().x > -8 && player.getPlayerBody().getLinearVelocity().x < 8) {
//							if(x > 0) { // right
//								player.getPlayerBody().setLinearVelocity(8.0f, player.getPlayerBody().getLinearVelocity().y);
//							}else if (x < 0) { // left
//								player.getPlayerBody().setLinearVelocity(-8.0f, player.getPlayerBody().getLinearVelocity().y);
//							}else { // stop moving
//								player.getPlayerBody().setLinearVelocity(0.0f, player.getPlayerBody().getLinearVelocity().y);
//							}
//						}
//						if(y > 0) {
//							player.jump();
//						}
//					}
//				});
//    	
//    	control.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//    	control.getControlBase().setAlpha(0.5f);
//    	control.getControlBase().setScaleCenter(0, 128);
//    	control.getControlBase().setScale(1.25f);
//    	control.getControlKnob().setScale(1.25f);
//    	control.refreshControlKnobPosition();
//    	setChildScene(control);
    }   
    
	private void loadMusic() {
		ResourcesManager.getInstance().mario_song_music.play();	
		ResourcesManager.getInstance().mario_song_music.setVolume(0.5f);
		ResourcesManager.getInstance().mario_song_music.setLooping(true);	
	}
	
}

